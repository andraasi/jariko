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

private class CLIProgramSetup(
    private val args: Array<String>,
    private var onExfmt: (
        fields: List<DSPFField>,
        snapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse? = { _, _ -> null }
) {
    private val isRunAsJar: Boolean = this.args.isNotEmpty()

    private fun getProgramSource(): String {
        return if (isRunAsJar) this.args[0] else "ADD01.rpgle"
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

    fun create(): Pair<CommandLineProgram, Configuration> {
        val programSource = this.getProgramSource()
        val programFinders = this.createProgramFinders()
        val program = getProgram(programSource, programFinders = programFinders)
        val configuration = this.createConfig()

        return Pair(program, configuration)
    }
}

fun setup(
    args: Array<String>,
    onExfmtCallback: (
        fields: List<DSPFField>,
        snapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse?
): Pair<CommandLineProgram, Configuration> {
    val setup = CLIProgramSetup(args, onExfmtCallback)
    return setup.create()
}