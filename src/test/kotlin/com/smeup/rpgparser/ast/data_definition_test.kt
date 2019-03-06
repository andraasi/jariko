package com.smeup.rpgparser.ast

import com.smeup.rpgparser.*
import org.junit.Test as test

class DataDefinitionTest {

    fun processDataDefinition(code: String) : CompilationUnit {
        val completeCode = """
|     H/COPY QILEGEN,£INIZH
|      *---------------------------------------------------------------
|     I/COPY QILEGEN,£TABB£1DS
|     I/COPY QILEGEN,£PDS
|     $code
        """.trimMargin("|")
        val rContext = assertCodeCanBeParsed(completeCode)
        return rContext.toAst(considerPosition = false)
    }

    @test fun singleDataParsing() {
        val cu = processDataDefinition("D U\$FUNZ          S             10")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", DataType.SINGLE, 10)
    }

    @test fun booleanDataParsing() {
        val cu = processDataDefinition("D OK              S              1N")
        cu.assertDataDefinitionIsPresent("OK", DataType.BOOLEAN, 1)
    }

    @test fun singleDataParsingOther() {
        val cu = processDataDefinition("D U\$FUNZ          S             99")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", DataType.SINGLE, 99)
    }

    @test fun singleDataParsingWithDecimals() {
        val cu = processDataDefinition("D \$X              S              3  2")
        cu.assertDataDefinitionIsPresent("\$X", DataType.SINGLE, 3, decimals = 2)
    }

    @test fun arrayParsing() {
        val cu = processDataDefinition("D U\$FUNZ          S             10    DIM(200)")
        cu.assertDataDefinitionIsPresent("U\$FUNZ", DataType.SINGLE, 10, arrayLength = IntLiteral(200))
    }

    @test fun structParsing() {
        val cu = processDataDefinition("D                 DS\n" +
                "     D \$\$SVAR                      1050    DIM(200)\n" +
                "     D  \$\$SVARCD                     50    OVERLAY(\$\$SVAR:1)                    Name\n" +
                "     D  \$\$SVARVA                   1000    OVERLAY(\$\$SVAR:*NEXT)                Value")
        cu.assertDataDefinitionIsPresent("\$\$SVAR", DataType.DATA_STRUCTURE, 1050,
                arrayLength = IntLiteral(200),
                fields = listOf(
                        FieldDefinition("\$\$SVARCD", 50),
                        FieldDefinition("\$\$SVARVA", 1000)
                ))
    }
}
