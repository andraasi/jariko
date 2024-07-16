package com.dspf.multithread

import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class Client {
    private var server: Socket? = null

    private fun receive(): String {
        return this.server!!.getInputStream().bufferedReader().readLine()
    }

    private fun send(string: String) {
        this.server!!.getOutputStream().bufferedWriter().write(string)
    }

    @Throws(SocketException::class)
    constructor(programSource: String, ip: String, port: Int) {
        this.server = Socket(ip, port)
        this.server.use {
            this.send(programSource)
            val fields = this.receive()
            this.send("@")
        }
    }
}

fun main(args: Array<String>) {
    val programSource = args[0]
    val ip = args[1]
    val port = args[2].toInt()
    val program = Client(programSource, ip, port)
}