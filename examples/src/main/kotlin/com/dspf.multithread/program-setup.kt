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

private const val isRunAsJar: Boolean = true

private class CLIProgramSetup(
    private val programSource: String,
    private var onExfmt: (
        fields: List<DSPFField>,
        snapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse? = { _, _ -> null }
) {
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
        val programSource = this.programSource
        val programFinders = this.createProgramFinders()
        val program = getProgram(programSource, programFinders = programFinders)
        val configuration = this.createConfig()

        return Pair(program, configuration)
    }
}

class NotEnoughPortsException : Exception("Supply at 3 ports to listen to")

fun setup(
    programSource: String,
    onExfmtCallback: (
        fields: List<DSPFField>,
        snapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse?
): Pair<CommandLineProgram, Configuration> {
    val setup = CLIProgramSetup(programSource, onExfmtCallback)
    return setup.create()
}

fun getListenPorts(args: Array<String>): List<Int> {
    val ports = mutableListOf<Int>()
    if (isRunAsJar && args.size < 3) throw NotEnoughPortsException()
    val port1 = if (isRunAsJar) args[0].toInt() else 5170
    val port2 = if (isRunAsJar) args[1].toInt() else 5171
    val port3 = if (isRunAsJar) args[2].toInt() else 5172
    ports.add(port1)
    ports.add(port2)
    ports.add(port3)
    return ports
}

fun getClientArgs(args: Array<String>): Pair<Pair<String, Int>, String> {
    val ip = if (isRunAsJar) args[0] else "localhost"
    val port = if (isRunAsJar) args[1].toInt() else 5170
    val programSource = if (isRunAsJar) args[2] else "add01.rpgle"
    return Pair(Pair(ip, port), programSource)
}