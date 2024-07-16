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
        println("connected")
    }

    private fun receive(): String {
        this.server!!.getInputStream().bufferedReader().use {
            val string = it.readLine()
            println("received: $string")
            return string
        }
    }

    private fun send(string: String) {
        this.server!!.getOutputStream().bufferedWriter().use {
            it.write(string)
            println("sent: $string")
        }
    }

    fun call() {
        this.server.use {
            this.send(this.programSource!!)
            val fields = this.receive()
            this.send(fields)
        }
    }
}

fun main(args: Array<String>) {
    val programSource = if (isRunAsJar) args[0] else "ADD01.rpgle"
    val ip = if (isRunAsJar) args[1] else "localhost"
    val port = if (isRunAsJar) args[2].toInt() else 5170
    val program = RemoteProgram(programSource, ip, port)
    program.call()
}