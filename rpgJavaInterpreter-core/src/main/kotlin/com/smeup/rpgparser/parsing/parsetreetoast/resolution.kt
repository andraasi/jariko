package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.interpreter.DBInterface
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.enrichPossibleExceptionWith
import com.strumenta.kolasu.model.*

private fun CompilationUnit.findInStatementDataDefinitions() {
    // TODO could they be also annidated?
    this.allStatements().filterIsInstance(StatementThatCanDefineData::class.java).forEach {
        this.addInStatementDataDefinitions(it.dataDefinition())
    }
}

private fun CompilationUnit.allStatements(): List<Statement> {
    val result = mutableListOf<Statement>()
    result.addAll(this.main.stmts)
    this.subroutines.forEach {
        result.addAll(it.stmts)
    }
    return result
}

fun CompilationUnit.resolve(databaseInterface: DBInterface) {
    this.assignParents()

    this.findInStatementDataDefinitions()

    this.databaseInterface = databaseInterface

    this.specificProcess(DataRefExpr::class.java) { dre ->
        if (!dre.variable.resolved) {

            if (dre.variable.name.contains('.')) {
                val ds = dre.variable.name.substring(0, dre.variable.name.indexOf("."))

                val fieldName = dre.variable.name.substring(dre.variable.name.indexOf(".") + 1)

                val resField = this.allDataDefinitions.find { it.name.equals(fieldName, true) }
                dre.variable.referred = resField
            } else {

                require(dre.variable.tryToResolve(this.allDataDefinitions, caseInsensitive = true)) {
                    "Data reference not resolved: ${dre.variable.name} at ${dre.position}"
                }
            }
        }
    }
    this.specificProcess(ExecuteSubroutine::class.java) { esr ->
        if (!esr.subroutine.resolved) {
            require(esr.subroutine.tryToResolve(this.subroutines, caseInsensitive = true)) {
                "Subroutine call not resolved: ${esr.subroutine.name}"
            }
        }
    }
    this.specificProcess(QualifiedAccessExpr::class.java) { qae ->
        if (!qae.field.resolved) {
            if (qae.container is DataRefExpr) {
                val dataRef = qae.container
                val dataDefinition = dataRef.variable.referred!! as DataDefinition
                require(qae.field.tryToResolve(dataDefinition.fields, caseInsensitive = true)) {
                    "Field access not resolved: ${qae.field.name} in data definition ${dataDefinition.name}"
                }
            } else {
                TODO()
            }
        }
    }
    // replace FunctionCall with ArrayAccessExpr where it makes sense
    this.specificProcess(FunctionCall::class.java) { fc ->
        if (fc.args.size == 1) {
            val data = this.allDataDefinitions.firstOrNull { it.name == fc.function.name }
            if (data != null) {
                enrichPossibleExceptionWith(fc.position) {
                    fc.replace(ArrayAccessExpr(
                            array = DataRefExpr(ReferenceByName(fc.function.name, referred = data)),
                            index = fc.args[0],
                            position = fc.position))
                }
            }
        }
    }

    // replace equality expr in For init with Assignments
    this.specificProcess(ForStmt::class.java) { fs ->
        if (fs.init is EqualityExpr) {
            val ee = fs.init as EqualityExpr
            fs.init.replace(AssignmentExpr(ee.left as AssignableExpression, ee.right, ee.position))
        }
    }

    this.specificProcess(EvalStmt::class.java) { s ->
        if (s.expression is EqualityExpr) {
            s.expression.replace((s.expression as EqualityExpr).toAssignment())
        }
    }

    this.specificProcess(PlistParam::class.java) { pp ->
        if (!pp.param.resolved) {
            require(pp.param.tryToResolve(this.allDataDefinitions, caseInsensitive = true)) {
                "Plist Param not resolved: ${pp.param.name} at ${pp.position}"
            }
        }
    }
}

private fun EqualityExpr.toAssignment(): AssignmentExpr {
    return AssignmentExpr(
            this.left as AssignableExpression,
            this.right,
            this.position
    )
}
