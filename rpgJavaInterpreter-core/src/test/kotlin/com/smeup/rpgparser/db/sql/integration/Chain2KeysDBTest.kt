package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.Test
import kotlin.test.assertEquals

class Chain2KeysDBTest : AbstractTest() {

    @Test
    fun findsExistingRecord() {
        assertEquals(
                listOf("Found: ABC12"),
                outputOfDBPgm(
                        "db/CHAIN2KEYS",
                        listOf(sqlCreateTestTable(), insertRecords()),
                        mapOf("toFind1" to StringValue("ABC"), "toFind2" to StringValue("12"))
                ))
    }

    @Test
    fun doesntFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
                outputOfDBPgm(
                        "db/CHAIN2KEYS",
                        listOf(sqlCreateTestTable(), insertRecords()),
                        mapOf("toFind1" to StringValue("ZZZ"), "toFind2" to StringValue("99"))))
    }

    private fun sqlCreateTestTable() =
            """
        CREATE TABLE MYFILE2 (
            KY1TST CHAR(5) DEFAULT '' NOT NULL,
            KY2TST DECIMAL(2, 0) DEFAULT 0 NOT NULL,
            DESTST CHAR(40) DEFAULT '' NOT NULL,
            PRIMARY KEY(KY1TST, KY2TST) )
        """.trimIndent()

    private fun insertRecords() = "INSERT INTO MYFILE2 (KY1TST, KY2TST, DESTST) VALUES('ABA', 1, 'ABA1'), ('ABC', 1, 'ABC1'), ('ABC', 12, 'ABC12'), ('XYZ', 1, 'XYZ1')"
}
