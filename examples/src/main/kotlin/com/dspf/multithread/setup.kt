package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.execution.CommandLineProgram
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

private fun getProgramSource(args: Array<String>): String {
    return if (isRunAsJar) args[0] else "ADD01.rpgle"
}

private fun createDspfConfig(): DspfConfig {
    val simpleDspfConfig = if (isRunAsJar) {
        SimpleDspfConfig(".")
    } else {
        SimpleDspfConfig({ }.javaClass.getResource("/metadata")!!.path)
    }
    return DspfConfig(
        metadataProducer = simpleDspfConfig::getMetadata,
        dspfProducer = simpleDspfConfig::dspfProducer
    )
}

private fun createProgramFinders(): List<DirRpgProgramFinder> {
    return if (isRunAsJar) {
        listOf(DirRpgProgramFinder(File(".")))
    } else {
        listOf(DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg")!!.path)))
    }
}

private fun createJarikoCallback(): JarikoCallback {
    val jarikoCallback = JarikoCallback()
    jarikoCallback.onExfmt = onExfmt
    return jarikoCallback
}

private fun createConfig(): Configuration {
    return Configuration(
        dspfConfig = createDspfConfig(),
        jarikoCallback = createJarikoCallback()
    )
}

// setup
private var isRunAsJar = false

private var onExfmt: (
    fields: List<DSPFField>,
    runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse? = { _, _ -> null }

fun setup(
    args: Array<String>,
    onExfmtCallback: (
        fields: List<DSPFField>,
        runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse?
): Pair<CommandLineProgram, Configuration> {
    onExfmt = onExfmtCallback
    isRunAsJar = args.isNotEmpty()

    val programSource = getProgramSource(args)
    val programFinders = createProgramFinders()
    val program = getProgram(nameOrSource = programSource, programFinders = programFinders)
    val configuration = createConfig()

    return Pair(program, configuration)
}