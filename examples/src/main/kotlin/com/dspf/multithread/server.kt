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
        this.thread = Thread(this::handleConnection)
    }

    private fun close() {
        println("closing connection...")
        this.client!!.close()
        this.server.close()
        println("connection closed")
    }

    private fun handleConnection() {
        val programSource = receive(this.client!!)

        val (program, configuration) = setup(programSource, this::onExfmt)
        program.singleCall(emptyList(), configuration)
        this.close()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        println("executing EXFMT...")
        send(this.client!!, json.encodeToString<List<DSPFField>>(fields))
//        val values = emptyMap<String, Value>()
        val values = json.decodeFromString<Map<String, Value>>(receive(this.client!!))
        return OnExfmtResponse(snapshot, values)
    }

    fun listen() {
        try {
            this.client = this.server.accept()
            println("client connected")
            this.thread.start()
        } catch (e: Exception) {
            this.close()
            this.thread.interrupt()
        }
    }
}

fun main(args: Array<String>) {
    val port = if (isRunAsJar) args[0].toInt() else 5170
    val program = SocketProgram(port)
    program.listen()
}