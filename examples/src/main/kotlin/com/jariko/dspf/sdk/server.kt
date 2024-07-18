package com.jariko.dspf.sdk

import java.net.ServerSocket

class Server(private val port: Int) {

    private val serverSocket = ServerSocket(port)
    private val clients = mutableListOf<ClientHandler>()
    private var alive = true

    private fun getClientHandlerById(id: String): ClientHandler? {
        return clients.find { it.id == id }
    }

    fun start() {
        while (alive) {
            println("Server listening on port $port...")
            val client = serverSocket.accept()
            val reader = client.getInputStream().bufferedReader(charset = Charsets.UTF_8)
            val writer = client.getOutputStream().bufferedWriter(charset = Charsets.UTF_8)
            val id = reader.readLine()

            println("Client sent id: $id")
            val clientHandler = getClientHandlerById(id)?.apply {
                this.reader = reader
                this.writer = writer
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
}

fun main() {
    val server = Server(5170)
    server.start()
}