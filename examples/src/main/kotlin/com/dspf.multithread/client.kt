package com.dspf.multithread

import com.jariko.dspf.sdk.startVideoSession
import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class RemoteProgram(
    private val ip: String,
    private val port: Int,
    private val programSource: String
) {
    private var server: Socket? = null

    @Throws(SocketException::class)
    fun call() {
        try {
            this.server = Socket(ip, port)
            println("connected")

            this.server.use {
                println("waiting for ready signal")
                receive(this.server!!)
                send(it!!, this.programSource)

                while(true) {
                    val fields = json.decodeFromString<List<DSPFField>>(receive(it))
                    val values = startVideoSession(fields)
                    send(it, json.encodeToString<Map<String, Value>>(values))
                }
            }

        } catch (e: Exception) {
            println("Exception occured: ${e.message}")
            this.close()
        }
    }

    private fun close() {
        this.server?.close()
        println("disconnected")
    }
}

fun main(args: Array<String>) {
    val (socket, programSource) = getClientArgs(args)
    val (ip, port) = socket
    val program = RemoteProgram(ip, port, programSource)
    program.call()
}