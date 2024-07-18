package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.Throws

class RemoteProgram(
    private val ip: String,
    private val port: Int,
    private val programSource: String
) {
    private var server: Socket? = null
    private val reader: BufferedReader?
        get() = server?.getInputStream()?.bufferedReader()
    private val writer: BufferedWriter?
        get() = server?.getOutputStream()?.bufferedWriter()

    private fun tellProgramSource() {
        write(writer!!, programSource)
    }

    private fun send(values: Map<String, Value>) {
       write(writer!!, json.encodeToString<Map<String, Value>>(values))
    }

    private fun receive(): List<DSPFField>{
        return json.decodeFromString<List<DSPFField>>(read(reader!!))
    }

    @Throws(SocketException::class)
    fun call() {
        try {
            server = Socket(ip, port)
            println("connected")

            println("waiting for ready signal")
            receive()
            tellProgramSource()

            while(shouldContinue()) {
                val fields = receive()
                val values = startVideoSession(fields)
                send(values)
            }
        } catch (e: Exception) {
            println("Exception occurred: ${e.message}")
        } finally {
            close()
        }
    }

    private fun shouldContinue(): Boolean {
        return readln() != "q"
    }


    private fun close() {
        server?.close()
        println("disconnected")
    }
}

fun main(args: Array<String>) {
    val ip = "localhost"
    val port = 5170
    val programSource = try { args[0] } catch (e: IndexOutOfBoundsException) { "add01.rpgle" }
    val program = RemoteProgram(ip, port, programSource)
    program.call()
}