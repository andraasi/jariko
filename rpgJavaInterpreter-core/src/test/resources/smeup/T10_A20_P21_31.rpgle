     D A20_AR2         S              2  0 DIM(6)
     D A20_AR3         S              2  0 DIM(6)
     D A20_ARES        S              2  0 DIM(6)
     D A20_AR4         S              7  5 DIM(6)
     D A20_AR5         S              7  5 DIM(6)
     D A20_ARES2       S              7  5 DIM(6)
     D £DBG_Str        S             100          VARYING

     D* Z-SUB con due array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=5
     C                   EVAL      A20_AR2(5)=7
     C                   EVAL      A20_AR2(6)=11
     C                   Z-SUB     A20_AR2       A20_AR3
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* ADD con due array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C                   ADD       A20_AR2       A20_AR3
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* ADD con tre array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C     A20_AR2       ADD       A20_AR3       A20_ARES
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_ARES(1))+', '
     C                                    +%CHAR(A20_ARES(2))+', '
     C                                    +%CHAR(A20_ARES(3))+', '
     C                                    +%CHAR(A20_ARES(4))+', '
     C                                    +%CHAR(A20_ARES(5))+', '
     C                                    +%CHAR(A20_ARES(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact1('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* SUB con due array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C                   SUB       A20_AR2       A20_AR3
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* SUB con tre array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C     A20_AR2       SUB       A20_AR3       A20_ARES
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_ARES(1))+', '
     C                                    +%CHAR(A20_ARES(2))+', '
     C                                    +%CHAR(A20_ARES(3))+', '
     C                                    +%CHAR(A20_ARES(4))+', '
     C                                    +%CHAR(A20_ARES(5))+', '
     C                                    +%CHAR(A20_ARES(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact1('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* MULT con due array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=5
     C                   EVAL      A20_AR2(5)=7
     C                   EVAL      A20_AR2(6)=11
     C                   MULT      A20_AR2       A20_AR3
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* MULT con tre array
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C     A20_AR2       MULT      A20_AR3       A20_ARES
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_ARES(1))+', '
     C                                    +%CHAR(A20_ARES(2))+', '
     C                                    +%CHAR(A20_ARES(3))+', '
     C                                    +%CHAR(A20_ARES(4))+', '
     C                                    +%CHAR(A20_ARES(5))+', '
     C                                    +%CHAR(A20_ARES(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact1('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* DIV con due array interi
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=5
     C                   EVAL      A20_AR2(5)=7
     C                   EVAL      A20_AR2(6)=11
     C                   DIV       A20_AR2       A20_AR3
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* DIV con tre array interi
     C                   EVAL      A20_AR2(1)=1
     C                   EVAL      A20_AR2(2)=2
     C                   EVAL      A20_AR2(3)=3
     C                   EVAL      A20_AR2(4)=4
     C                   EVAL      A20_AR2(5)=5
     C                   EVAL      A20_AR2(6)=6
     C                   EVAL      A20_AR3(1)=6
     C                   EVAL      A20_AR3(2)=5
     C                   EVAL      A20_AR3(3)=4
     C                   EVAL      A20_AR3(4)=3
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=1
     C     A20_AR2       DIV       A20_AR3       A20_ARES
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_ARES(1))+', '
     C                                    +%CHAR(A20_ARES(2))+', '
     C                                    +%CHAR(A20_ARES(3))+', '
     C                                    +%CHAR(A20_ARES(4))+', '
     C                                    +%CHAR(A20_ARES(5))+', '
     C                                    +%CHAR(A20_ARES(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))
     C                                    +') Fact1('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* DIV con due array
     C                   EVAL      A20_AR4(1)=1
     C                   EVAL      A20_AR4(2)=2
     C                   EVAL      A20_AR4(3)=3
     C                   EVAL      A20_AR4(4)=5
     C                   EVAL      A20_AR4(5)=7
     C                   EVAL      A20_AR4(6)=11
     C                   DIV       A20_AR4       A20_AR5
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR5(1))+', '
     C                                    +%CHAR(A20_AR5(2))+', '
     C                                    +%CHAR(A20_AR5(3))+', '
     C                                    +%CHAR(A20_AR5(4))+', '
     C                                    +%CHAR(A20_AR5(5))+', '
     C                                    +%CHAR(A20_AR5(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR4(1))+', '
     C                                    +%CHAR(A20_AR4(2))+', '
     C                                    +%CHAR(A20_AR4(3))+', '
     C                                    +%CHAR(A20_AR4(4))+', '
     C                                    +%CHAR(A20_AR4(5))+', '
     C                                    +%CHAR(A20_AR4(6))
     C                                    +')'
     C     £DBG_Str      DSPLY

     D* DIV con tre array
     C                   EVAL      A20_AR4(1)=1
     C                   EVAL      A20_AR4(2)=2
     C                   EVAL      A20_AR4(3)=3
     C                   EVAL      A20_AR4(4)=4
     C                   EVAL      A20_AR4(5)=5
     C                   EVAL      A20_AR4(6)=6
     C                   EVAL      A20_AR5(1)=6
     C                   EVAL      A20_AR5(2)=5
     C                   EVAL      A20_AR5(3)=4
     C                   EVAL      A20_AR5(4)=3
     C                   EVAL      A20_AR5(5)=2
     C                   EVAL      A20_AR5(6)=1
     C     A20_AR4       DIV       A20_AR5       A20_ARES2
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_ARES2(1))+', '
     C                                    +%CHAR(A20_ARES2(2))+', '
     C                                    +%CHAR(A20_ARES2(3))+', '
     C                                    +%CHAR(A20_ARES2(4))+', '
     C                                    +%CHAR(A20_ARES2(5))+', '
     C                                    +%CHAR(A20_ARES2(6))
     C                                    +') Fact2('
     C                                    +%CHAR(A20_AR5(1))+', '
     C                                    +%CHAR(A20_AR5(2))+', '
     C                                    +%CHAR(A20_AR5(3))+', '
     C                                    +%CHAR(A20_AR5(4))+', '
     C                                    +%CHAR(A20_AR5(5))+', '
     C                                    +%CHAR(A20_AR5(6))
     C                                    +') Fact1('
     C                                    +%CHAR(A20_AR4(1))+', '
     C                                    +%CHAR(A20_AR4(2))+', '
     C                                    +%CHAR(A20_AR4(3))+', '
     C                                    +%CHAR(A20_AR4(4))+', '
     C                                    +%CHAR(A20_AR4(5))+', '
     C                                    +%CHAR(A20_AR4(6))
     C                                    +')'
     C     £DBG_Str      DSPLY
