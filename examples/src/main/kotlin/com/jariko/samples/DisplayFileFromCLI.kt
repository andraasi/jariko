package com.jariko.samples

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.dspfparser.linesclassifier.DSPFFieldType
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File
import kotlin.system.exitProcess

// === Rendering functions === //

const val MAX_COLUMNS = 80
const val MAX_LINES = 24

private fun renderScreenLimits() {
    print("+")
    for (i in 0..MAX_COLUMNS) {
        print("-")
    }
    print("+")
}

private fun renderOutputFields(fields: List<DSPFField>) {
    var previousLineNo = 1
    var currentLineNo: Int
    var previousColumnNo: Int
    var currentColumnNo: Int
    var previousLength: Int

    renderScreenLimits()

    fields.groupBy { it.y }.toList().sortedBy { it.first }.forEach { group ->
        currentLineNo = group.first!!

        // handle print of empty lines
        for (i in previousLineNo..currentLineNo) {
            if (i == previousLineNo) {
                println()
            } else {
                print("|")
                for (j in 0..MAX_COLUMNS) {
                    print(" ")
                }
                println("|")
            }
        }

        // screen left margin
        print("|")

        previousColumnNo = 0
        previousLength = 0
        group.second.forEach { member ->
            val fakeConstField = "${member.name}: "
            val string = "$fakeConstField${(member.value as Value).asString().value}"

            // - 2 because in terminal x = 0 equals x = 1 in 5250, and the value of x is included (<= sign)
            currentColumnNo = member.x!! - 2
            previousLength = string.length

            for (i in previousColumnNo..currentColumnNo) {
                print(" ")
            }

            print(string)
            previousColumnNo = currentColumnNo + string.length + 1
        }
        for (i in previousColumnNo..MAX_COLUMNS) {
            print(" ")
        }

        // screen right margin
        print("|")
        previousLineNo = currentLineNo + 1
    }

    // complete screen fill with empty lines
    println()
    for (i in previousLineNo..MAX_LINES) {
        print("|")
        for (j in 0..MAX_COLUMNS) {
            print(" ")
        }
        println("|")
    }
    renderScreenLimits()
    if (IS_RUN_FROM_SOURCE) exitProcess(0)
}

private fun printFields(fields: List<DSPFField>) {
    Runtime.getRuntime().exec("clear")
    println()
    renderOutputFields(fields)
    println()
    print("INPUT fields: ")
    println("${fields.filter { it.type == DSPFFieldType.INPUT }.map { it.name }}")
    println()
    println("Insert input: ")
}

// === UI control functions === //

class UnknownVariable(name: String) : Exception("`$name`")
class WrongInputSyntax : Exception("Should be: `VAR1=VALUE;VAR2=23`")

private fun parseInput(input: String): Map<String, String> {
    try {
        val assignments = input.split(';')
        val variablesAndValues = mutableMapOf<String, String>()

        assignments.forEach {
            val tokens = it.split('=')
            variablesAndValues[tokens[0]] = tokens[1]
        }

        return variablesAndValues
    } catch (e: Exception) {
        throw WrongInputSyntax()
    }
}

private fun askInputFor(fields: List<DSPFField>): Map<String, Value> {
    val variablesAndValues = mutableMapOf<String, Value>()
    val line = readln()
    val updatedVariables = parseInput(line)

    updatedVariables.keys.forEach { variable ->
        fields.find { field -> field.name == variable } ?: throw UnknownVariable(variable)
    }

    fields.filter { it.type == DSPFFieldType.INPUT && updatedVariables[it.name] != null }.forEach {
        if (it.isNumeric && it.precision!! == 0)
            variablesAndValues[it.name] = IntValue(updatedVariables[it.name]!!.toLong())
        if (it.isNumeric && it.precision!! > 0)
            variablesAndValues[it.name] = DecimalValue(updatedVariables[it.name]!!.toBigDecimal())
        else if (!it.isNumeric)
            variablesAndValues[it.name] = StringValue(updatedVariables[it.name]!!)
    }

    return variablesAndValues
}

private fun onExfmt(fields: List<DSPFField>, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
    printFields(fields)
    while (true) {
        try {
            val variablesAndValues = askInputFor(fields)
            return OnExfmtResponse(runtimeInterpreterSnapshot, variablesAndValues)
        } catch (e: Exception) {
            e.printStackTrace()
            continue
        }
    }
}

// === Jariko call setup functions === //
const val IS_RUN_FROM_SOURCE = true

private fun createDspfConfig(): DspfConfig {
    val simpleDspfConfig = if (IS_RUN_FROM_SOURCE) {
        SimpleDspfConfig({ }.javaClass.getResource("/metadata")!!.path)
    } else {
        SimpleDspfConfig(".")
    }
    return DspfConfig(
        metadataProducer = simpleDspfConfig::getMetadata,
        dspfProducer = simpleDspfConfig::dspfProducer
    )
}

private fun createProgramFinder(): DirRpgProgramFinder {
    return if (IS_RUN_FROM_SOURCE) {
        DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg")!!.path))
    } else {
        DirRpgProgramFinder(File("."))
    }
}

private fun createJarikoCallback(): JarikoCallback {
    val jarikoCallback = JarikoCallback()
    jarikoCallback.onExfmt = ::onExfmt
    return jarikoCallback
}

private fun createConfig(): Configuration {
    return Configuration(
        dspfConfig = createDspfConfig(),
        jarikoCallback = createJarikoCallback()
    )
}

fun main(args: Array<String>) {
    val programSource = if (IS_RUN_FROM_SOURCE) "ADD01.rpgle" else args[0]
    val programFinders = listOf(createProgramFinder())
    val program = getProgram(nameOrSource = programSource, programFinders = programFinders)

    program.singleCall(emptyList(), configuration = createConfig())
}