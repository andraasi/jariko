     V*=====================================================================
     V* Description:
     V* This is a sample of an rpgle program with fibonacci series calculation
     V*=====================================================================
     FFIBV      CF   E             WORKSTN
     D NBR             S             21  0
     D RESULT          S             21  0 INZ(0)
     D COUNT           S             21  0
     D A               S             21  0 INZ(0)
     D B               S             21  0 INZ(1)
     C     PGMCTL        DOWEQ     0
     C                   EVAL      RESULT = 0
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      COUNT = 0
     C                   EVAL      NBR = 0
     C                   EXFMT     FMT01
      *
     C                   Eval      NBR    = %Dec(PPDAT : 8 : 0)
     C                   EXSR      FIB
     C                   EVAL      FINAL_VAL = RESULT
     C                   ENDDO
     C                   SETON                                          LR
      *--------------------------------------------------------------*
     C     FIB           BEGSR
     C                   SELECT
     C                   WHEN      NBR = 0
     C                   EVAL      RESULT = 0
     C                   WHEN      NBR = 1
     C                   EVAL      RESULT = 1
     C                   OTHER
     C                   FOR       COUNT = 2 TO NBR
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
     C                   ENDSL
     C                   ENDSR
      *--------------------------------------------------------------*


