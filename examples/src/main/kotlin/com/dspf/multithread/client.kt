package com.dspf.multithread

import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class RemoteProgram(
    private val programSource: String,
    private val ip: String,
    private val port: Int
) {
    private var server: Socket? = null

    @Throws(SocketException::class)
    fun call() {
        try {
            this.server = Socket(ip, port)
            println("connected")
            this.server.use {
                send(it!!, this.programSource)
                val fields = receive(it)
                send(it, fields)
            }
            println("disconnected")
        } catch (e: Exception) {
            this.server?.close()
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