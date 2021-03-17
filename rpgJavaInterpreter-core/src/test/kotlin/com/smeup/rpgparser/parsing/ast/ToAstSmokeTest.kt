package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class ToAstSmokeTest : AbstractTest() {

    @Test
    fun buildAstForJD_001() {
        val cu = assertASTCanBeProduced("JD_001")
        assertEquals(10, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(7, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        assertEquals(18, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(10, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        assertEquals(16, cu.dataDefinitions.size)
        assertEquals(4, cu.main.stmts.size)
        assertEquals(6, cu.subroutines.size)
    }

    @Test
    fun buildAstForJCODFISS() {
        val cu = assertASTCanBeProduced("JCODFISS")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(2, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForJD_001_dataDefinitions() {
        val root = assertASTCanBeProduced("JD_001")
        assertEquals(10, root.dataDefinitions.size)
        assertEquals("@UNNAMED_DS_16", root.dataDefinitions[0].name)
        assertEquals("U\$FUNZ", root.dataDefinitions[1].name)
        assertEquals("U\$METO", root.dataDefinitions[2].name)
        assertEquals("U\$SVARSK", root.dataDefinitions[3].name)
        assertEquals("U\$IN35", root.dataDefinitions[4].name)
        assertEquals("\$\$URL", root.dataDefinitions[5].name)
        assertEquals("\$X", root.dataDefinitions[6].name)
        assertEquals("U\$SVARSK_INI", root.dataDefinitions[7].name)
        assertEquals("§§FUNZ", root.dataDefinitions[8].name)
        assertEquals("§§METO", root.dataDefinitions[9].name)
    }

    @Test
    fun buildAstForJD_001_subroutines() {
        val root = assertASTCanBeProduced("JD_001")
        assertEquals(7, root.subroutines.size)
        assertEquals("£INIZI", root.subroutines[0].name)
        assertEquals(1, root.subroutines[0].stmts.size)
        assertEquals("IMP0", root.subroutines[1].name)
        assertEquals(1, root.subroutines[1].stmts.size)
        assertEquals("FIN0", root.subroutines[2].name)
        assertEquals(0, root.subroutines[2].stmts.size)
        assertEquals("FINZ", root.subroutines[3].name)
        assertEquals(2, root.subroutines[3].stmts.size)
        assertEquals("FESE", root.subroutines[4].name)
        assertEquals(3, root.subroutines[4].stmts.size)
        assertEquals("REPVAR", root.subroutines[5].name)
        assertEquals(1, root.subroutines[5].stmts.size)
        assertEquals("FCLO", root.subroutines[6].name)
        assertEquals(0, root.subroutines[6].stmts.size)
    }

    @Test
    fun buildAstForHELLO() {
        val cu = assertASTCanBeProduced("HELLO")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCAL() {
        val cu = assertASTCanBeProduced("CALCFIBCAL")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCA2() {
        val cu = assertASTCanBeProduced("CALCFIBCA2")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForCALCFIBCA3() {
        val cu = assertASTCanBeProduced("CALCFIBCA3")
        assertEquals(0, cu.dataDefinitions.size)
        assertEquals(3, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForHELLOTYPE() {
        val cu = assertASTCanBeProduced("HELLOTYPE")
        assertEquals(2, cu.dataDefinitions.size)
        assertEquals(6, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForHELLOERROR() {
        val cu = assertASTCanBeProduced("HELLOERROR")
        assertEquals(1, cu.dataDefinitions.size)
        assertEquals(5, cu.main.stmts.size)
        assertEquals(0, cu.subroutines.size)
    }

    @Test
    fun buildAstForTIMESTDIFF() {
        val cu = assertASTCanBeProduced("TIMESTDIFF")
        assertEquals(5, cu.dataDefinitions.size)
        assertEquals(6, cu.main.stmts.size)
        assertEquals(1, cu.subroutines.size)
    }

    @Test
    fun buildAstForMUTE10_01() {
        assertASTCanBeProduced("performance/MUTE10_01")
        assertASTCanBeProduced("performance/MUTE10_01A")
        assertASTCanBeProduced("performance/MUTE10_01B")
        assertASTCanBeProduced("performance/MUTE10_01C")
    }

    @Test
    fun buildAstForREADP() {
        assertASTCanBeProduced("db/READP")
    }

    @Test
    fun buildAstForACTGRP_FIX() {
        val cu = assertASTCanBeProduced("ACTGRP_FIX")
        assertEquals(firstActivationGroupDirective(cu).type, NamedActivationGroup("MYACT"))
    }

    @Test
    fun buildAstForACTGRP_NEW() {
        val cu = assertASTCanBeProduced("ACTGRP_NEW")
        assertEquals(firstActivationGroupDirective(cu).type, NewActivationGroup)
    }

    @Test
    fun buildAstForACTGRP_CALLER() {
        val cu = assertASTCanBeProduced("ACTGRP_CLR")
        assertEquals(firstActivationGroupDirective(cu).type, CallerActivationGroup)
    }

    private fun firstActivationGroupDirective(cu: CompilationUnit): ActivationGroupDirective {
        assertTrue(cu.directives.size >= 1)
        val directive = cu.directives[0]
        assertTrue(directive is ActivationGroupDirective)
        return directive
    }

    /**
     * We need to be able to create AST for sources containing just D specifications
     * */
    @Test
    fun buildAstForMU1DSPEC() {
        assert(assertASTCanBeProduced("£MU1DSPEC").dataDefinitions.size == 2)
    }

    /**
     * We need to be able to create AST for sources containing just C specifications
     * */
    @Test
    fun buildAstForMU1CSPEC() {
        assert(assertASTCanBeProduced("£MU1CSPEC").subroutines.size == 1)
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun buildAstForMU1API() {
        val cu = assertASTCanBeProduced("£MU1API")
        assert(cu.dataDefinitions.size == 2)
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun buildAstForMU1PGM() {
        val cu = assertASTCanBeProduced("£MU1PGM", considerPosition = true)
        assert(cu.subroutines.size == 4)
        assert(cu.dataDefinitions.size == 2)
    }

    @Test
    fun buildAstForLOSER_PR() {
        assertASTCanBeProduced("LOSER_PR", considerPosition = true)
    }

    @Test
    fun buildAstForLOSER_PR_FULL() {
        assertASTCanBeProduced("LOSER_PR_FULL", considerPosition = true)
    }

    @Test
    fun buildAstForAPIPGM1() {
        assertASTCanBeProduced("APIPGM1", considerPosition = true).apply {
            assertEquals(4, this.dataDefinitions.size)
            assertEquals(1, this.subroutines.size)
        }
    }

    @Test
    fun testLoadAPIOverhead() {
        val millis = measureTimeMillis {
            repeat(3) {
                val totalApis = 1000
                // Every API contains a subroutine named nnn
                val apiTemplate = """
|    C     nnn           BEGSR
|    C                   ENDSR
        """.trimIndent()
                val templateUsingApiDirective = """
|     /API APInnn
|    C                   EXSR      nnn     
        """.trimIndent()

                val templateUsingApiInline = """
|    C                   EXSR      nnn     
        """.trimIndent()

                val programUsingApiDirective = StringBuffer()
                val programUsingApiInline = StringBuffer()
                // this maps contains moduleName to moduleSource
                val apis = mutableMapOf<String, String>().apply {
                    repeat(totalApis) {
                        // create APInnn
                        val subroutineName = it.toString().padStart(3, '0')
                        val apiSource = apiTemplate.replace("nnn", subroutineName)
                        val apiName = "API$subroutineName"
                        this[apiName] = apiSource

                        // Include API directive to programUsingApiDirective
                        programUsingApiDirective.append(templateUsingApiDirective.replace("nnn", subroutineName)).append("\n")

                        // Include API inline in programUsingApiInline
                        programUsingApiInline.append(templateUsingApiInline.replace("nnn", subroutineName)).append("\n")
                        programUsingApiInline.append(apiSource).append("\n")
                    }
                }

                val memoryProgramFinder = object : RpgProgramFinder {
                    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
                        TODO("Not yet implemented")
                    }

                    override fun findCopy(copyId: CopyId): Copy? {
                        TODO("Not yet implemented")
                    }

                    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
                        return ApiDescriptor()
                    }

                    override fun findApi(apiId: ApiId): Api {
                        return Api.loadApi(apis[apiId.member]!!.byteInputStream(), sourceProgram = SourceProgram.RPGLE)
                    }
                }
                val si = JavaSystemInterface().apply {
                    rpgSystem.addProgramFinder(memoryProgramFinder)
                    // loggingConfiguration = consoleLoggingConfiguration(PARSING_LOGGER)
                }
                MainExecutionContext.execute(systemInterface = si) {
//                    it.executionProgramName = "APIINCL"
//                    RpgParserFacade().parseAndProduceAst(programUsingApiDirective.toString().byteInputStream()).apply {
//                        assertEquals(totalApis, this.subroutines.size)
//                    }
                    it.executionProgramName = "APIINLN"
                    RpgParserFacade().parseAndProduceAst(programUsingApiInline.toString().byteInputStream()).apply {
                        assertEquals(totalApis, this.subroutines.size)
                    }
                }
            }
        }
        println("Millis: $millis")
    }
}