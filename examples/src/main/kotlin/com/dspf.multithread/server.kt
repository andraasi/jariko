package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class SocketProgram {
    private val server: ServerSocket
    private var client: Socket? = null
    private val thread: Thread
    private val logger: Logger

    @Throws(SocketException::class)
    constructor(port: Int) {
        this.server = ServerSocket(port)
        this.thread = Thread(this::handleConnection, "${this.server}")
        this.logger = Logger("${this.server}")
    }

    fun listen() {
        try {
            this.thread.start()
        } catch (e: Exception) {
            this.logger.log("Exception occured: ${e.message}")
            this.close()
            this.thread.interrupt()
        }
    }

    private fun handleConnection() {
        this.logger.log("listening...")
        this.client = this.server.accept()
        this.logger.log("client connected")

        this.logger.log("sending ready signal")
        send(this.client!!, "READY")

        this.logger.log("waiting for program source")
        val programSource = receive(this.client!!)
        val (program, configuration) = setup(programSource, this::onExfmt)

        this.logger.log("starting program $programSource")
        program.singleCall(emptyList(), configuration)
        this.logger.log("program $programSource ended")

        this.close()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        this.logger.log("executing EXFMT")
        send(this.client!!, json.encodeToString<List<DSPFField>>(fields))
        val values = json.decodeFromString<Map<String, Value>>(receive(this.client!!))
        return OnExfmtResponse(snapshot, values)
    }

    private fun close() {
        this.client!!.close()
        this.server.close()
        this.logger.log("connection closed")
    }
}

fun main(args: Array<String>) {
    val ports = getListenPorts(args)
    val program1 = SocketProgram(ports[0])
    val program2 = SocketProgram(ports[1])
    val program3 = SocketProgram(ports[2])
    program1.listen()
    program2.listen()
    program3.listen()
}