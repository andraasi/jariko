package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.Value
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.ServerSocket

class ClientHandler(val id: String, var reader: BufferedReader, var writer: BufferedWriter) : Runnable {

    // TODO implement a thread pool by using Executors.
    private var jarikoThread = Thread(this)
    private val monitor = Object()

    init {
        println("ClientHandler created")
        jarikoThread.start()
    }

    override fun run() {
        val configuration = Configuration()
        configuration.jarikoCallback.onExfmt = { fields, snapshot ->
            send(fields)
            // TODO check what happens on resumed thread
            val values = receive()
            OnExfmtResponse(snapshot, values)
        }
        startJariko(configuration)
    }

    private fun startJariko(configuration: Configuration) {
        TODO("Not yet implemented")
    }

    private fun send(fields: List<DSPFField>) {
        TODO("Not yet implemented")
    }

    private fun receive(): Map<String, Value> {
        try {
            val json = reader.readLine()
            TODO("json to map")
        } catch (e: Exception) {
            println("Exception occured: ${e.message}")
            synchronized(monitor) {
                monitor.wait()
            }
            val json = reader.readLine()
            TODO("json to map")
        }
    }

    fun resume() {
        synchronized(monitor) {
            monitor.notify()
        }
    }
}

class Server(val port: Int) {

    private val serverSocket = ServerSocket(port)
    private val clients = mutableListOf<ClientHandler>()

    private var alive = true

    fun start() {
        while (alive) {
            println("Server listening on port $port...")
            val client = serverSocket.accept()
            val reader = client.getInputStream().bufferedReader(charset = Charsets.UTF_8)
            val writer = client.getOutputStream().bufferedWriter(charset = Charsets.UTF_8)
            val id = reader.readLine()
            println("Client sent id: $id")
            val clientHandler = getClientHandlerById(id)?.apply {
                this.reader = this.reader
                this.writer = this.writer
                println("Resuming client: ${this.id}")
                this.resume()
            } ?: ClientHandler(id = id, reader = reader, writer = writer).apply {
                println("Creating client: ${this.id}")
                clients.add(this)
            }
            println("Client connected: ${clientHandler.id}")
        }
    }

    fun stop() {
        alive = false
        serverSocket.close()
    }

    private fun getClientHandlerById(id: String): ClientHandler? {
        return clients.find { it.id == id }
    }
}

fun main() {
    val server = Server(5170)
    server.start()
}