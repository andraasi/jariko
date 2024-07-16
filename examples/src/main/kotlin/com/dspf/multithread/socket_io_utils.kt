package com.dspf.multithread

import java.net.Socket

// using 'use' will also close the socket; use it wisely

fun receive(socket: Socket): String {
    val string = socket.getInputStream().bufferedReader().readLine()
    println("received: $string")
    return string
}

fun send(socket: Socket, string: String) {
    socket.getOutputStream().bufferedWriter().write("$string\n")
    println("sent: $string")
}
