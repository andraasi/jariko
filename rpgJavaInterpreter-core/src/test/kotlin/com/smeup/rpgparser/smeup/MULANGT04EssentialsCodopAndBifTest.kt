package com.smeup.rpgparser.smeup

import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

open class MULANGT04EssentialsCodopAndBifTest : MULANGTTest() {
    /**
     * TIME with inline number declarations
     */
    @Test
    fun executeT04_A80_P05() {
        val isEarly = LocalDateTime.now().hour < 10
        val suffixLength = if (isEarly) 1 else 2
        val expected = listOf(
            "A80_D1(hhmm${"s".repeat(suffixLength)}) A80_D2(hhmmssDDMM${"Y".repeat(suffixLength)}) A80_D3(hhmmssDDMMYY${"Y".repeat(suffixLength)})"
        )
        assertEquals(expected, "smeup/T04_A80_P05".outputOf())
    }

    /**
     * SUBDUR with time difference (100.000 iterations)
     */
    @Test
    fun executeT04_A90_P03() {
        val expected = listOf(
            "Microsecondi(21763813000) Millisecondi(21763813) Secondi(21763) Minuti(362) Ore(6)"
        )
        assertEquals(expected, "smeup/T04_A90_P03".outputOf())
    }
}