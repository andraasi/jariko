package com.dspf.multithread

import java.net.Socket

// using 'use' will also close the socket; use it wisely

// saving readers and buffered is mandatory
// because function creates a new one each time

private const val EOB = "EOB"

fun receive(socket: Socket): String {
    println("receiving...")
    val bufferedReader = socket.getInputStream().bufferedReader()
    val string = StringBuilder()
    var line: String?

    // allows JSON to be collected and parsed correctly
    while (bufferedReader.readLine().also { line = it } != null) {
        if (line == EOB) {
            break
        }
        string.append(line)
    }

    println("received ${string.length} B")
    return string.toString()
}

fun send(socket: Socket, string: String) {
    println("sending...")
    val bufferedWriter = socket.getOutputStream().bufferedWriter()
    bufferedWriter.write(string)
    bufferedWriter.newLine()
    bufferedWriter.write(EOB)
    bufferedWriter.newLine()
    bufferedWriter.flush()
    println("sent ${string.length} B")
}
