package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import org.junit.experimental.categories.Category
import java.math.BigDecimal
import kotlin.test.assertEquals

open class SmeupInterpreterTest : AbstractTest() {

    @Test
    fun executeT15_A80() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A80: " + "smeup/T15_A80".outputOf())
    }

    @Test
    fun executeT15_A90() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A90: " + "smeup/T15_A90".outputOf())
    }

    @Test
    fun executeT02_A30() {
        val len = 100
        val expected = listOf(
            buildString {
                append("AAAAA".padEnd(len, ' '))
                append("BBBBB".padEnd(len, ' '))
                append("CCCCC".padEnd(len, ' '))
                append("DDDDD".padEnd(len, ' '))
                append("EEEEE".padEnd(len, ' '))
                append("FFFFF".padEnd(len, ' '))
                append("GGGGG".padEnd(len, ' '))
                append("HHHHH".padEnd(len, ' '))
                // Here I don't padEnd because the display messages are trimmed
                append("IIIII")
            }
        )
        assertEquals(expected, "smeup/T02_A30".outputOf())
    }

    /**
     * This is a performance test function for the T01_10_P02 program.
     * The function executes the T01_10_P02 program and measures the time taken for different operations and then,
     * it calculates the average time taken for each operation.
     * After that execute the Java implementation of the T01_10_P02.
     * The results are then printed to the console.
     */
    @Test()
    @Category(PerformanceTest::class)
    fun executeT01_10_P02() {
        var original = 0.0
        var varying = 0.0
        var exsrOverhead = 0.0
        // are inside the program
        val iteration = 5
        val systemInterface = JavaSystemInterface().apply {
            onDisplay = { message, _ ->
                // println("executeT01_10_P02: $message")
                if (message.startsWith("Original")) {
                    original += message.substringAfter("Original:").trim().toLong()
                } else if (message.startsWith("Varying")) {
                    varying += message.substringAfter("Varying:").trim().toLong()
                } else if (message.startsWith("ExsrOverhead:")) {
                    exsrOverhead += message.substringAfter("ExsrOverhead:").trim().toLong()
                } else {
                    error("message not expected: $message")
                }
            }
        }
        executePgm(programName = "smeup/T01_10_P02", systemInterface = systemInterface)
        println("T01_10_P02(RPGLE): original: ${original / iteration}ms")
        println("T01_10_P02(RPGLE): varying: ${varying / iteration}ms")
        println("T01_10_P02(RPGLE): exsrOverhead: ${exsrOverhead / iteration}ms")
        executeJavaT01_10_P02()
    }

    /**
     * This function is a performance test for a Java implementation of T01_10_P02 program.
     * It measures the time taken to perform a specific operation a certain number of times.
     * The operation involves the evaluation of string variable (varying and not) and the assignment in a map similar to a symbol table.
     * The not varying variable is assigned to a string with a fixed length, while the varying variable is assigned to a string with a variable length.
     * The results of the test are printed to the console.
     */
    private fun executeJavaT01_10_P02() {
        var original = 0L
        var varying = 0L
        val padding = 2560
        val iteration = 5
        val symbolTable = mutableMapOf(
            "NNN" to BigDecimal(100000),
            "£DBG_Str" to StringBuilder().append("".padEnd(padding)),
            "£DBG_Str_var" to StringBuilder()
        )
        // Start the timer for the original operation
        var start: Long = System.currentTimeMillis()
        symbolTable["NNN"] = BigDecimal(100000)
        for (i in 1..iteration) {
            do {
                // Perform the original operation
                symbolTable["£DBG_Str"] = (symbolTable["£DBG_Str"] as StringBuilder).clear().append("Hello World!".padEnd(padding))
                symbolTable["NNN"] = (symbolTable["NNN"] as BigDecimal).subtract(BigDecimal.valueOf(1))
            } while ((symbolTable["NNN"] as BigDecimal)!!.toLong() > 0)
        }
        // Calculate the time taken for the original operation
        original += (System.currentTimeMillis() - start)

        // Start the timer for the varying operation
        start = System.currentTimeMillis()
        symbolTable["NNN"] = BigDecimal(100000)
        for (i in 1..iteration) {
            do {
                // Perform the varying operation
                symbolTable["£DBG_Str_var"] = (symbolTable["£DBG_Str_var"] as StringBuilder).clear().append("Hello World!")
                symbolTable["NNN"] = (symbolTable["NNN"] as BigDecimal).subtract(BigDecimal.valueOf(1))
            } while ((symbolTable["NNN"] as BigDecimal)!!.toLong() > 0)
        }
        // Calculate the time taken for the varying operation
        varying = System.currentTimeMillis() - start

        // Print the results to the console
        println("T01_10_P02(Java): original: ${original}ms")
        println("T01_10_P02(Java): varying: ${varying}ms")
    }
}