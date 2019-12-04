   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulle stringhe
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!LOOKUP    !  !%LOOKUP  !  !
      *+----------+--+---------+--+
     D $S              S             10  0
     D $C              S             10  0
     D $V              S             10  0
     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D V1              S          30000
     D S1              S            100
     D V2              S          30000    Varying
     D I1              S              5  0
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     C                   EXSR      F_CERCA
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(9000)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CERCA
      *---------------------------------------------------------------------
     C     F_CERCA       BEGSR
      *
      * carico stringa 30Kb
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 1'      DSPLY
     C                   DOW       %LEN(%TRIM(V1)) < 30000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      V1=%TRIM(V1)+%TRIM(SI($S))
     C                               +'.'+%TRIM(%EDITC($C:'Z'))+'_'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(5000) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * carico stringa 30Kb varying
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 2'      DSPLY
     C                   DOW       %LEN(V2) < 30000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      V2=V2+%TRIM(SI($S))
     C                               +'.'+%TRIM(%EDITC($C:'Z'))+'_'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(100) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ricerco su V1
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 3'      DSPLY
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      $V=%SCAN(%TRIM(S1):V1)
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(1000) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ricerco su V2
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 4'      DSPLY
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      $V=%SCAN(%TRIM(S1):V2)
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(700) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ricerco su V1 con SUBSTR
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 5'      DSPLY
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      I1=$C * 10
     C                   IF        I1 > 29900
     C                   EVAL      I1=29900
     C                   ENDIF
     C                   EVAL      $V=%SCAN(%TRIM(S1):V1:I1)
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(1500) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ricerco su V2 con SUBSTR
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C     'Loop 6'      DSPLY
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      I1=$C * 10
     C                   IF        I1 > 29900
     C                   EVAL      I1=29900
     C                   ENDIF
     C                   EVAL      $V=%SCAN(%TRIM(S1):V2:I1)
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(1000) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   ENDSR
      *---------------------------------------------------------------------
** SI
001
d01
A01
c01
B01
b01
C01
901
101
D01
H01
E01
201
e01
a01
x01
X01
