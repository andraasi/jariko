package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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

            println("waiting for ready signal")
            receive(this.server!!)
            println("ready")

            this.server.use {
                send(it!!, this.programSource)

                while(true) {
                    val fields = json.decodeFromString<List<DSPFField>>(receive(it))
                    val values = startVideoSession(fields)
                    send(it, json.encodeToString<Map<String, Value>>(values))
                }
            }

        } catch (e: Exception) {
            println(e)
            this.close()
            println("disconnected")
        }
    }

    private fun close() {
        this.server?.close()
    }
}

fun main(args: Array<String>) {
    val programSource = if (isRunAsJar) args[0] else "add01.rpgle"
    val ip = if (isRunAsJar) args[1] else "localhost"
    val port = if (isRunAsJar) args[2].toInt() else 5170
    val program = RemoteProgram(programSource, ip, port)
    program.call()
}