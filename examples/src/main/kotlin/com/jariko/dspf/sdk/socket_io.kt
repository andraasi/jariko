package com.jariko.dspf.sdk

import java.io.BufferedReader
import java.io.BufferedWriter

// End Of Buffer
private const val EOB = "EOB"

fun read(bufferedReader: BufferedReader): String {
    val string = StringBuilder()
    var line: String?

    // allows JSON to be collected and parsed correctly
    while (bufferedReader.readLine().also { line = it } != null) {
        if (line == EOB) {
            break
        }
        string.append(line)
    }

    return string.toString()
}

fun write(bufferedWriter: BufferedWriter, string: String) {
    bufferedWriter.write(string)
    bufferedWriter.newLine()
    bufferedWriter.write(EOB)
    bufferedWriter.newLine()
    bufferedWriter.flush()
}