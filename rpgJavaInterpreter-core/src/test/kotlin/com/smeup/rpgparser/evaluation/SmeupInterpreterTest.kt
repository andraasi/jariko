package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.Configuration
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

    @Test
    fun executeT04_A40() {
        val expected = listOf("A40_P1(122469.88)A40_P2(987.22)A40_P3(123456.10)A40_P4(121028170.03)")
        assertEquals(expected, "smeup/T04_A40".outputOf())
    }

    /**
     * This is a performance test function for the T01_A10_P03 program.
     * The function executes the T01_10_P02 program and measures the time taken for different operations and then,
     * it calculates the average time taken for each operation.
     * After that execute the Java implementation of the T01_A10_P03.
     * The results are then printed to the console.
     */
    @Test
    @Category(PerformanceTest::class)
    fun executeT01_A10_P03() {
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
        executePgm(programName = "smeup/T01_A10_P03", systemInterface = systemInterface)
        println("T01_A10_P03(RPGLE): original: ${original / iteration}ms")
        println("T01_A10_P03(RPGLE): varying: ${varying / iteration}ms")
        println("T01_A10_P03(RPGLE): exsrOverhead: ${exsrOverhead / iteration}ms")
        executeJavaT01_A10_P03()
    }

    /**
     * This function is a performance test for a Java implementation of T01_10_P03 program.
     * It measures the time taken to perform a specific operation a certain number of times.
     * The operation involves the evaluation of string variable (varying and not) and the assignment in a map similar to a symbol table.
     * The not varying variable is assigned to a string with a fixed length, while the varying variable is assigned to a string with a variable length.
     * The results of the test are printed to the console.
     */
    private fun executeJavaT01_A10_P03() {
        val original: Double
        val varying: Double
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
                symbolTable["£DBG_Str"] =
                    (symbolTable["£DBG_Str"] as StringBuilder).clear().append("Hello World!".padEnd(padding))
                symbolTable["NNN"] = (symbolTable["NNN"] as BigDecimal).subtract(BigDecimal.valueOf(1))
            } while ((symbolTable["NNN"] as BigDecimal).toLong() > 0)
        }
        // Calculate the time taken for the original operation
        original = (System.currentTimeMillis() - start).toDouble()

        // Start the timer for the varying operation
        start = System.currentTimeMillis()
        symbolTable["NNN"] = BigDecimal(100000)
        for (i in 1..iteration) {
            do {
                // Perform the varying operation
                symbolTable["£DBG_Str_var"] =
                    (symbolTable["£DBG_Str_var"] as StringBuilder).clear().append("Hello World!")
                symbolTable["NNN"] = (symbolTable["NNN"] as BigDecimal).subtract(BigDecimal.valueOf(1))
            } while ((symbolTable["NNN"] as BigDecimal).toLong() > 0)
        }
        // Calculate the time taken for the varying operation
        varying = (System.currentTimeMillis() - start).toDouble()

        // Print the results to the console
        println("T01_A10_P03(Java): original: ${original / iteration}ms")
        println("T01_A10_P03(Java): varying: ${varying / iteration}ms")
    }

    /**
     * This function is a performance test for the T01_A20_P02 program.
     * It measures the time taken to execute the program and prints the average duration to the console.
     * The function uses a JavaSystemInterface to capture the duration of each execution from the display messages.
     * The function also disables debugging information for the program execution.
     * After executing the RPGLE program, it also executes the Java implementation of the same program.
     */
    @Test
    @Category(PerformanceTest::class)
    fun executeT01_A20_P02() {
        // Initialize the duration variable to store the total duration of all executions
        var duration = 0.0
        // Define the number of iterations for the test
        val iteration = 100
        // Create a JavaSystemInterface to interact with the program
        val systemInterface = JavaSystemInterface().apply {
            // Define the onDisplay function to capture the duration from the display messages
            onDisplay = { message, _ ->
                // If the message starts with "Duration:", extract the duration and add it to the total duration
                if (message.startsWith("Duration:")) {
                    duration += message.substringAfter("Duration:").trim().toLong()
                } else {
                    // If the message does not start with "Duration:", throw an error
                    error("message not expected: $message")
                }
            }
        }
        // Create a Configuration object for the program execution
        val configuration = Configuration().apply {
            // Disable debugging information for the program execution
            options.debuggingInformation = false
        }
        // Execute the program with the defined system interface and configuration
        executePgm(
            programName = "smeup/T01_A20_P02",
            systemInterface = systemInterface,
            configuration = configuration
        )
        // Print the average duration of the program executions to the console
        println("T01_A20_P02(RPGLE): Duration: ${duration / iteration}ms")
        // Execute the Java implementation of the program
        executeJavaT01_A20_P02(iteration = iteration)
    }

    /**
     * This function is a performance test for the Java implementation of the T01_A20_P02 program.
     * It measures the time taken to perform a specific operation a certain number of times.
     * The operation involves assigning a string to a variable in a symbol table and decrementing a counter until it reaches zero.
     * The function then calculates the average time taken for these operations and prints the result to the console.
     */
    private fun executeJavaT01_A20_P02(iteration: Int) {
        // Initialize the duration variable to store the total duration of all executions
        var duration = 0.0
        // Create a symbol table with initial values
        val symbolTable = mutableMapOf(
            "NNN" to BigDecimal(100000),
            "A20_A15" to "",
            "£DBG_Str" to ""
        )
        // Initialize additional variables in the symbol table
        for (i in 1..100) {
            symbolTable["VAR$i"] = ""
        }

        // Start the timer for the operation
        val start: Long = System.currentTimeMillis()
        // Reset the counter in the symbol table
        symbolTable["NNN"] = BigDecimal(100000)
        // Perform the operation for the defined number of iterations
        for (i in 1..iteration) {
            do {
                // Assign a string to a variable in the symbol table
                symbolTable["A20_A15"] = "Lorem quam"
                // Decrement the counter in the symbol table
                symbolTable["NNN"] = (symbolTable["NNN"] as BigDecimal).subtract(BigDecimal.valueOf(1))
            } while ((symbolTable["NNN"] as BigDecimal).toLong() > 0)
        }
        // Calculate the time taken for the operation
        duration = (System.currentTimeMillis() - start).toDouble()

        // Print the average time taken for the operation to the console
        println("T01_A20_P02(Java): Duration: ${duration / iteration}ms")
    }
}