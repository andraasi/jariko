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

    @Throws(SocketException::class)
    constructor(port: Int) {
        this.server = ServerSocket(port)
        this.thread = Thread(this::handleConnection, "${this.server.localSocketAddress}:$port")
    }

    fun listen() {
        try {
            this.client = this.server.accept()
            println("client connected")
            this.thread.start()
        } catch (e: Exception) {
            println("Exception occured: ${e.message}")
            this.close()
            this.thread.interrupt()
        }
    }

    private fun handleConnection() {
        println("sending ready signal")
        send(this.client!!, "READY")

        println("waiting for program source")
        val programSource = receive(this.client!!)
        val (program, configuration) = setup(programSource, this::onExfmt)

        println("starting program $programSource")
        program.singleCall(emptyList(), configuration)
        println("program $programSource ended")

        this.close()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        println("executing EXFMT")
        send(this.client!!, json.encodeToString<List<DSPFField>>(fields))
        val values = json.decodeFromString<Map<String, Value>>(receive(this.client!!))
        return OnExfmtResponse(snapshot, values)
    }

    private fun close() {
        this.client!!.close()
        this.server.close()
        println("connection closed")
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