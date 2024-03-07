     D £DBG_Str        S           2500          VARYING

     C                   CLEAR                   NUM_ESE           7 0
     C                   EVAL      NUM_ESE=100
     C                   DO        NUM_ESE
     C                   CALL      'MULANGTA20'
     C                   ENDDO
     C                   EVAL      £DBG_Str=%CHAR(NUM_ESE)
     C     £DBG_Str      DSPLY