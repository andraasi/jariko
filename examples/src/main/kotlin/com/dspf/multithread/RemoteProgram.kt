package com.dspf.multithread

import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class RemoteProgram {
    private var programSource: String? = null
    private var server: Socket? = null

    @Throws(SocketException::class)
    constructor(programSource: String, ip: String, port: Int) {
        this.programSource = programSource
        this.server = Socket(ip, port)
    }

    fun call() {
        this.server.use {
            this.send(this.programSource!!)
            val fields = this.receive()
            this.send("@")
        }
    }

    private fun receive(): String {
        return this.server!!.getInputStream().bufferedReader().readLine()
    }

    private fun send(string: String) {
        this.server!!.getOutputStream().bufferedWriter().write(string)
    }
}

fun main(args: Array<String>) {
    val programSource = args[0]
    val ip = args[1]
    val port = args[2].toInt()
    val program = RemoteProgram(programSource, ip, port)
    program.call()
}