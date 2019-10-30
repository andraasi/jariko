package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.MutesImmutableMap
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.utils.asLong
import com.strumenta.kolasu.model.Position
import org.antlr.v4.runtime.Token
import org.apache.commons.io.input.BOMInputStream
import java.util.*

data class MuteAnnotationExecutionLogEntry(override val programName: String, val annotation: MuteAnnotation, var result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return when (annotation) {
            is MuteComparisonAnnotation -> "executing MuteComparisonAnnotation: ${annotation.position} $result ${annotation.val1} ${annotation.comparison} ${annotation.val2} "
            is MuteFailAnnotation -> "executing MuteFail: ${annotation.position} - ${result.render()}"
            else -> this.toString()
        }
    }
}

fun MuteParser.MuteLineContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), position: Position? = null): MuteAnnotation {
    fun extractExpressionFrom(token: Token): Expression {
        return RpgParserFacade().createParser(
            BOMInputStream(
                ("".padStart(8) + token.text.substring(
                    1,
                    token.text.lastIndex
                )).byteInputStream(Charsets.UTF_8)
            ), errors = mutableListOf(), longLines = true
        ).expression().toAst(conf)
    }

    return when (val annotation = this.muteAnnotation()) {
        is MuteParser.MuteComparisonAnnotationContext -> {
            val val1 = extractExpressionFrom(annotation.val1)
            val val2 = extractExpressionFrom(annotation.val2)

            MuteComparisonAnnotation(val1, val2, Comparison.valueOf(annotation.cp.text.substring(1, annotation.cp.text.lastIndex)), position = position)
        }
        is MuteParser.MuteTypeAnnotationContext -> {
            // Type="NOXMI" annotation are not supported
            MuteTypeAnnotation(position = position)
        }
        is MuteParser.MuteTimeoutContext -> {
            MuteTimeoutAnnotation(annotation.intNumber().NUMBER().text.asLong(), position)
        }
        is MuteParser.MuteFailAnnotationContext -> {
            val message = extractExpressionFrom(annotation.msg)
            MuteFailAnnotation(message, position)
        }
        else -> TODO(this.text.toString())
    }
}

fun injectMuteAnnotationToStatements(
    statements: List<Statement>,
    start: Int,
    end: Int,
    map: Map<Int, MuteParser.MuteLineContext>
): List<MuteAnnotationResolved> {

    // Consider only the annotation in the scope
    val filtered: Map<Int, MuteParser.MuteLineContext> = map.filterKeys {
        it in start..end
    }
    // makes a consumable list of annotation
    val mutesToProcess: MutableMap<Int, MuteParser.MuteLineContext> = filtered.toSortedMap()
    val mutesResolved: MutableList<MuteAnnotationResolved> = mutableListOf()

    // Vist each statment
    statements.forEach {

        val resolved = it.accept(mutesToProcess, start, end)
        mutesResolved.addAll(resolved)

        resolved.forEach {
            mutesToProcess.remove(it.muteLine)
        }
    }
    // at the end the mutesToProcess collection should be empty
    // otherwise it means the remaining annotations can't be attached
    // to any statement
    mutesToProcess.forEach {
        print("Could not attach the annotation @line ${it.key}")
    }
    return mutesResolved
}
fun injectMuteAnnotationToDataDefinitions(definitions: List<DataDefinition>, map: Map<Int, MuteParser.MuteLineContext>):
        List<MuteAnnotationResolved> {

    val mutesResolved: MutableList<MuteAnnotationResolved> = mutableListOf()

    if (definitions.size > 0) {
        val start: Int = definitions.first().position!!.start.line
        val end: Int = definitions.last().position!!.end.line + 1

        // Consider only the annotation in the scope
        val filtered: Map<Int, MuteParser.MuteLineContext> = map.filterKeys {
            it in start..end
        }
        // makes a consumable list of annotation
        val mutesToProcess: MutableMap<Int, MuteParser.MuteLineContext> = filtered.toSortedMap()

        definitions.forEach {
            val resolved = it.accept(mutesToProcess, start, end)
            mutesResolved.addAll(resolved)

            resolved.forEach {
                mutesToProcess.remove(it.muteLine)
            }
        }
    }
    return mutesResolved
}

fun Statement.injectMuteAnnotation(mutes: Map<Int, MuteParser.MuteLineContext>): List<MuteAnnotationResolved> {

    val resolved: MutableList<MuteAnnotationResolved> = mutableListOf()
    // Process the main body statements
    val stmts = listOf(this)
    resolved.addAll(injectMuteAnnotationToStatements(stmts,
            this.position!!.start.line,
            this.position!!.end.line,
            mutes))

    return resolved
}

private fun expandStartLineWhenNeeded(startLine: Int, mutes: MutesImmutableMap): Int {
    var line = startLine
    while (line - 1 in mutes.keys) {
        line--
    }
    return line
}

fun CompilationUnit.injectMuteAnnotation(mutes: MutesImmutableMap): List<MuteAnnotationResolved> {
    addTimeoutAnnotation(this, mutes)

    val resolved: MutableList<MuteAnnotationResolved> = mutableListOf()
    // injectMuteAnnotationHelper( this.dataDefinitions,)
    resolved.addAll(injectMuteAnnotationToDataDefinitions(this.dataDefinitions, mutes))
    // Process the main body statements
    // There is an issue when annotations appear just above the first statement
    // so we want to expand the research area to cover preceding annotations
    resolved.addAll(injectMuteAnnotationToStatements(this.main.stmts,
            expandStartLineWhenNeeded(this.main.stmts.position()!!.start.line, mutes),
            this.main.stmts.position()!!.end.line,
            mutes))
    // Process subroutines body statements
    this.subroutines.forEach {
        resolved.addAll(injectMuteAnnotationToStatements(it.stmts,
                it.position!!.start.line,
                it.position.end.line,
                mutes))
    }

    return resolved
}

private fun addTimeoutAnnotation(compilationUnit: CompilationUnit, mutes: Map<Int, MuteParser.MuteLineContext>) {
    compilationUnit.timeouts =
        mutes.values.filter {
            it.muteAnnotation() is MuteParser.MuteTimeoutContext
        }.map {
            it.toAst() as MuteTimeoutAnnotation
        }
}

fun acceptBody(body: List<Statement>, mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = 0, end: Int): MutableList<MuteAnnotationResolved> {
    val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

    // Process the body statements
    body.forEach {
        val toRemove = it.accept(mutes, start, end)
        toRemove.forEach {
            mutes.remove(it.muteLine)
            muteAttached.add(it)
        }
    }

    return muteAttached
}
