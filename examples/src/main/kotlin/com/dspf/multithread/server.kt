package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

private fun onExfmt(fields: List<DSPFField>, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
    while (true) {
        try {
            return OnExfmtResponse(runtimeInterpreterSnapshot, emptyMap())
        } catch (e: Exception) {
            continue
        }
    }
}

class SocketProgram {
    private val server: ServerSocket
    private var client: Socket? = null
    private val thread: Thread

    @Throws(SocketException::class)
    constructor(port: Int) {
        this.server = ServerSocket(port)
        this.thread = Thread(this::handleConnection)
    }

    private fun receive(): String {
        this.client!!.getInputStream().bufferedReader().use {
            val string = it.readLine()
            println("received: $string")
            return string
        }
    }

    private fun send(string: String) {
        this.client!!.getOutputStream().bufferedWriter().use {
            it.write(string)
            println("sent: $string")
        }
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        println("executing EXFMT...")
        this.send(fields.toString())
        val values = this.receive()
        return OnExfmtResponse(snapshot, emptyMap())
    }

    fun listen() {
        this.client = this.server.accept()
        println("client connected")
        this.thread.start()
    }

    private fun handleConnection() {
        val programSource = this.receive()

        val (program, configuration) = setup(programSource, ::onExfmt)
        program.singleCall(emptyList(), configuration)

        this.client!!.close()
        this.server.close()
        println("connection closed")
    }
}

fun main(args: Array<String>) {
    val port = if (isRunAsJar) args[0].toInt() else 5170
    val program = SocketProgram(port)
    program.listen()
}