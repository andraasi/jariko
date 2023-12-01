
      * Evaluation of overhead of not varying vs varying D spec declaration
      *
     D NNN             S              6  0 INZ(100000)
     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D £DBG_Str        S           2560
     D £DBG_Str_var    S           2560     VARYING
     D A90_N1          S             20  0
     D MSG             S            100
     D SUB_DESC        S             50     VARYING

     D EXT_LOOP        S              2  0  INZ(5)


     C                   DO        EXT_LOOP
      * --------------------------------------------------------------
      * Here we use exec SUB_A10_A1 that use the not varying D spec
      * Original
     C                   EVAL      NNN = 100000
     C                   EVAL      SUB_DESC = 'Original'
     C                   TIME                    £DBG_TIMINI
     C                   DO        NNN
     C                   EXSR      SUB_A10_A1
     C                   ENDDO
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
      * --------------------------------------------------------------
      * Here we use exec SUB_A10_A2 that use the varying D spec
      * Varying
     C                   EVAL      NNN = 100000
     C                   EVAL      SUB_DESC = 'Varying'
     C                   TIME                    £DBG_TIMINI
     C                   DO        NNN
     C                   EXSR      SUB_A10_A2
     C                   ENDDO
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
      * --------------------------------------------------------------
      * Here we use exec SUB_A10_A3 that does nothing
      * ExsrOverhead
     C                   EVAL      NNN = 100000
     C                   EVAL      SUB_DESC = 'ExsrOverhead'
     C                   TIME                    £DBG_TIMINI
     C                   DO        NNN
     C                   EXSR      SUB_A10_A3
     C                   ENDDO
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
      * --------------------------------------------------------------

     C                   ENDDO


     C                   SETON                                        LR

      * Performances test routine
      *
     C     SUB_A10_A1    BEGSR
     C                   EVAL      £DBG_Str='Hello world!'
     C                   ENDSR
      *---------------------------------------------------------------------
      *
     C     SUB_A10_A2    BEGSR
     C                   EVAL      £DBG_Str_var='Hello world!'
     C                   ENDSR
      *---------------------------------------------------------------------
      *
     C     SUB_A10_A3    BEGSR

     C                   ENDSR
      *---------------------------------------------------------------------

      * Utilities
     C     PRINT         BEGSR
     C     £DBG_TIMEND   SUBDUR    £DBG_TIMINI   A90_N1:*MS
     C                   EVAL      MSG=%CHAR(A90_N1/1000)
     C                   EVAL      MSG=SUB_DESC + ':' + %CHAR(A90_N1/1000)
     C     MSG           DSPLY
     C                   ENDSR