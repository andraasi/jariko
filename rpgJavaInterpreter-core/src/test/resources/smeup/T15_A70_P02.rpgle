     D £DBG_Str        S           2560    VARYING
     D £DBG_Pas        S             10
     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D NNN             S              6  0 INZ(100000)
     D DURATION        S             20  0
     D MSG             S            100
     D SUB_DESC        S             50     VARYING

     D A70_N020        S              2  0
     D A70_N040        S              4  0
     D A70_DGMY        S              6  0
     D A70_O060        S              6  0
     D A70_D080        S              8  0
     D A70_N070        S              7  0
     D A70_N062        S              6  2
     D A70_N064        S              6  4
     D A70_N112        S             11  2
     D A70_N102        S             10  2
     D A70_N051        S              5  1
     D A70_N132        S             13  2
     D A70_N150        S             15  0
     D A70_N142        S             14  2

     D* Numeri / Date / Ore (100.000 volte)
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      A70_N020=2
     C                   EVAL      A70_N040=217
     C                   EVAL      A70_DGMY=011299
     C                   EVAL      A70_D080=20181231
     C                   EVAL      A70_O060=082345
     C                   EVAL      A70_N070=2345
     C                   EVAL      A70_N062=-23,45
     C                   EVAL      A70_N064=-23,451
     C                   EVAL      A70_N112=-1234567,89
     C                   EVAL      A70_N102=-1234567,89
     C                   EVAL      A70_N051=-21,4
     C                   EVAL      A70_N132=-1234567,89
     C                   EVAL      A70_N150=-123456789
     C                   EVAL      A70_N142=-123456789,12
     C                   TIME                    £DBG_TIMINI
     C                   EVAL      SUB_DESC='original'
     C                   DO        NNN
     C                   EXSR      SUB_A70_A
     C                   ENDDO
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
      *
      *---------------------------------------------------------------------
    RD* Subsezione di SEZ_A70 P01 e P02
      *---------------------------------------------------------------------
     C     SUB_A70_A     BEGSR
      *
     C                   EVAL      £DBG_Str=
     C                             %EDITW(A70_N020:'  ')
     C                             +%EDITW(A70_N040:'0   ')
     C                             +%EDITW(A70_DGMY:'  /  /  ')
     C                             +%EDITW(A70_D080:'    /  /  ')
     C                             +%EDITW(A70_D080:'    -  -  ')
     C                             +%EDITW(A70_O060:'  :  :  ')
     C                             +%EDITW(A70_O060:'0  :  :  ')
     C                             +%EDITW(A70_O060:'  .  .  ')
     C                             +%EDITW(A70_N070:'    .   -')
     C                             +%EDITW(A70_N062:'    ,  -')
     C                             +%EDITW(A70_N064:'  ,    -')
     C                             +%EDITW(A70_N112:'        . 0 ,  -')
     C                             +%EDITW(A70_N102:'    .   . 0 ,  -')
     C                             +%EDITW(A70_N051:'   0, -%')
     C                             +%EDITW(A70_N132:'        , 0 .  ')
     C                             +%EDITW(A70_N150:'         .   .   -')
     C                             +%EDITW(A70_N142:'         . 0 ,  -')
      *
     C                   ENDSR
      *---------------------------------------------------------------------

     C     £DBG_Str      DSPLY


      * Utilities
     C     PRINT         BEGSR
     C     £DBG_TIMEND   SUBDUR    £DBG_TIMINI   DURATION:*MS
     C                   EVAL      MSG=%CHAR(DURATION/1000)
     C                   EVAL      MSG=SUB_DESC + ':' + %CHAR(DURATION/1000)
     C     MSG           DSPLY
     C                   ENDSR
