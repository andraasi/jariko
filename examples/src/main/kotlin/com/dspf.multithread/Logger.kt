package com.dspf.multithread

class Logger(private val prefix: String = "") {
    fun log(string: String) {
        println("${this.prefix} : $string")
    }
}