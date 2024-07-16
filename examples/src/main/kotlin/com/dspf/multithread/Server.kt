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

class JarikoSocketProgram {
    private val server: ServerSocket
    private var client: Socket? = null
    private val thread: Thread

    @Throws(SocketException::class)
    constructor(port: Int) {
        this.server = ServerSocket(port)
        this.client = this.server.accept()

        this.thread = Thread(this::handleConnection)
        this.thread.start()
    }

    private fun receive(): String {
        return this.client!!.getInputStream().bufferedReader().readLine()
    }

    private fun send(string: String) {
        this.client!!.getOutputStream().bufferedWriter().write(string)
    }

    private fun handleConnection() {
        val programSource = this.receive()

        val (program, configuration) = setup(arrayOf(programSource), ::onExfmt)
        program.singleCall(emptyList(), configuration)

        this.client!!.close()
        this.server.close()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        this.send(fields.toString())
        val values = this.receive()
        println(values)
        return OnExfmtResponse(snapshot, emptyMap())
    }
}

fun main(args: Array<String>) {
    
}