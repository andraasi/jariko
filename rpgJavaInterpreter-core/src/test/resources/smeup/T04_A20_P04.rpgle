     D NNN             S              6  0 INZ(100000)
     D A20_C1          C                   'MULANGTA10'
     D A20_P1          S             10    INZ('MULANGT04')
     D A20_P2          S              2  0
     D A20_P3          S             50
     D £DBG_Str        S             100         VARYING

     C                   CLEAR                   A20_P3
     C                   DO        NNN
     C                   EXSR      SUB_A20_A
     C                   ENDDO
     C                   EVAL      £DBG_Str='CALL('+A20_P1+', '
     C                                     +%CHAR(A20_P2)
     C                                     +', '+A20_P3+') '
     C     £DBG_Str      DSPLY

     C     SUB_A20_A     BEGSR
     C                   CALL      A20_C1                               35
      *
     C                   CALL      'MULANGTB10'
     C                   PARM                    A20_P1
     C                   PARM      1             A20_P2
     C                   PARM                    A20_P3
      *
     C                   ENDSR