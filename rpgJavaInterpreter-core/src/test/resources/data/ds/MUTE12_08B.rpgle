     V*=====================================================================
     V*
     V*  Play with some declaration variables ways
     V*
     V*=====================================================================
      * Declaration of 'N01B' derived from 'N01' (on D specs.)
     D N01             S              1  0
     D N01B            S                   LIKE(N01)
      *
      * Declaration of 'N02B' derived from 'N02' (on C specs.)
     D N02B            S                   LIKE(N02)
      *
      * Declaration of 'N03' derived from 'N01B' implicit derived from
      * 'N01' (on D spec.)
     D N03             S                   LIKE(N01B)
      *
      * Declaration of DS with an array, used to define 'FLD_DER' derived
      * field.
     D MYDS            DS
     D FLD                            7    DIM(10)
     D  SUBFLD01                      5    OVERLAY(FLD:1)
     D  SUBFLD02                      2    OVERLAY(FLD:*NEXT)
      *
      * Standalone 'FLD_DER' derived from field 'FLD' of 'MYDS'
     D FLD_DER         S                   LIKE(FLD) DIM(%ELEM(FLD))
      *
     C                   Z-ADD     0             N02               1 0
      *
    MU* VAL1(N01B) VAL2(9) COMP(EQ)
     C                   EVAL      N01B=9
      *
    MU* VAL1(N02B) VAL2(8) COMP(EQ)
     C                   EVAL      N02B=8
      *
    MU* VAL1(N03) VAL2(7) COMP(EQ)
     C                   EVAL      N03=7
      *
      * Declaration of 'N05' derived from 'N01' (on D specs.)
     C     *LIKE         DEFINE    N01           N05
    MU* VAL1(N05) VAL2(6) COMP(EQ)
     C                   EVAL      N05=6
      *
      * Declaration of 'N06' derived from 'N01B' implicit derived from
      * 'N01' (on D spec.)
     C     *LIKE         DEFINE    N01B          N06
    MU* VAL1(N06) VAL2(5) COMP(EQ)
     C                   EVAL      N06=5
      *
    MU* VAL1(FLD(1)) VAL2('First') COMP(EQ)
     C                   EVAL      FLD(1)='First'
      *
    MU* VAL1(FLD_DER(1)) VAL2('Tsrif') COMP(EQ)
     C                   EVAL      FLD_DER(1)='Tsrif'
      *
     C                   SETON                                        LR
