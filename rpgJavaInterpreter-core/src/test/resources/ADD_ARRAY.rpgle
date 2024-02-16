      *
      * ADD operation with array
      *

     D RES             S             500          VARYING
     D ARRINTF1        S              2  0 DIM(3)
     D ARRINTF2        S              2  0 DIM(3)
     D ARRINTF1_S2     S              2  0 DIM(2)
     D ARRINTF2_S2     S              2  0 DIM(2)
     D ARRINTRES       S              2  0 DIM(3)
     D ARRINTRES_S2    S              2  0 DIM(2)
     D ARRDECF1        S              2  1 DIM(3)
     D ARRDECF2        S              2  1 DIM(3)
     D ARRDECRES       S              2  1 DIM(3)



     D* Test without Factor 1

     D* With integer values
     C                   EVAL      ARRINTF2(1)=1
     C                   EVAL      ARRINTF2(2)=2
     C                   EVAL      ARRINTF2(3)=3
     C                   EVAL      ARRINTRES(1)=3
     C                   EVAL      ARRINTRES(2)=2
     C                   EVAL      ARRINTRES(3)=1
     C                   ADD       ARRINTF2      ARRINTRES
     C                   EVAL      RES=%CHAR(ARRINTRES(1))+', '
     C                                  +%CHAR(ARRINTRES(2))+', '
     C                                  +%CHAR(ARRINTRES(3))
     C     RES           DSPLY

     D* With decimal values
     C                   EVAL      ARRDECF2(1)=1.5
     C                   EVAL      ARRDECF2(2)=2.5
     C                   EVAL      ARRDECF2(3)=3.5
     C                   EVAL      ARRDECRES(1)=2.5
     C                   EVAL      ARRDECRES(2)=1.5
     C                   EVAL      ARRDECRES(3)=0.5
     C                   ADD       ARRDECF2      ARRDECRES
     C                   EVAL      RES=%CHAR(ARRDECRES(1))+', '
     C                                  +%CHAR(ARRDECRES(2))+', '
     C                                  +%CHAR(ARRDECRES(3))
     C     RES           DSPLY

     D* With Factor 2 as decimal and Result as integer
     C                   EVAL      ARRDECF2(1)=1.5
     C                   EVAL      ARRDECF2(2)=2.5
     C                   EVAL      ARRDECF2(3)=3.5
     C                   EVAL      ARRINTRES(1)=2
     C                   EVAL      ARRINTRES(2)=1
     C                   EVAL      ARRINTRES(3)=0
     C                   ADD       ARRDECF2      ARRINTRES
     C                   EVAL      RES=%CHAR(ARRINTRES(1))+', '
     C                                  +%CHAR(ARRINTRES(2))+', '
     C                                  +%CHAR(ARRINTRES(3))
     C     RES           DSPLY

     D* With Factor 2 as integer and Result as decimal
     C                   EVAL      ARRINTF2(1)=1
     C                   EVAL      ARRINTF2(2)=2
     C                   EVAL      ARRINTF2(3)=3
     C                   EVAL      ARRDECRES(1)=2.5
     C                   EVAL      ARRDECRES(2)=1.5
     C                   EVAL      ARRDECRES(3)=0.5
     C                   ADD       ARRINTF2      ARRDECRES
     C                   EVAL      RES=%CHAR(ARRDECRES(1))+', '
     C                                  +%CHAR(ARRDECRES(2))+', '
     C                                  +%CHAR(ARRDECRES(3))
     C     RES           DSPLY

     D* With integer values and when Factor 2 size is greater than Result
     C                   EVAL      ARRINTF2(1)=1
     C                   EVAL      ARRINTF2(2)=2
     C                   EVAL      ARRINTF2(3)=3
     C                   EVAL      ARRINTRES_S2(1)=3
     C                   EVAL      ARRINTRES_S2(2)=2
     C                   ADD       ARRINTF2      ARRINTRES_S2
     C                   EVAL      RES=%CHAR(ARRINTRES_S2(1))+', '
     C                                  +%CHAR(ARRINTRES_S2(2))
     C     RES           DSPLY

     D* With integer values and when Factor 2 size is smaller than Result
     C                   EVAL      ARRINTF2_S2(1)=1
     C                   EVAL      ARRINTF2_S2(2)=2
     C                   EVAL      ARRINTRES(1)=3
     C                   EVAL      ARRINTRES(2)=2
     C                   EVAL      ARRINTRES(3)=1
     C                   ADD       ARRINTF2_S2   ARRINTRES
     C                   EVAL      RES=%CHAR(ARRINTRES(1))+', '
     C                                  +%CHAR(ARRINTRES(2))+', '
     C                                  +%CHAR(ARRINTRES(3))
     C     RES           DSPLY

     D* With integer values and when Factor 2 isn't as array as Result, but Integer
     C                   EVAL      ARRINTRES(1)=1
     C                   EVAL      ARRINTRES(2)=2
     C                   EVAL      ARRINTRES(3)=3
     C                   ADD       1             ARRINTRES
     C                   EVAL      RES=%CHAR(ARRINTRES(1))+', '
     C                                  +%CHAR(ARRINTRES(2))+', '
     C                                  +%CHAR(ARRINTRES(3))
     C     RES           DSPLY



     D* Test with Factor 1

     D* With integer values
     C                   EVAL      ARRINTF1(1)=1
     C                   EVAL      ARRINTF1(2)=2
     C                   EVAL      ARRINTF1(3)=3
     C                   EVAL      ARRINTF2(1)=3
     C                   EVAL      ARRINTF2(2)=2
     C                   EVAL      ARRINTF2(3)=1
     C     ARRINTF1      ADD       ARRINTF2      ARRINTRES
     C                   EVAL      RES=%CHAR(ARRINTRES(1))+', '
     C                                  +%CHAR(ARRINTRES(2))+', '
     C                                  +%CHAR(ARRINTRES(3))
     C     RES           DSPLY

     D* With integer values
     C                   EVAL      ARRDECF1(1)=1.5
     C                   EVAL      ARRDECF1(2)=2.5
     C                   EVAL      ARRDECF1(3)=3.5
     C                   EVAL      ARRDECF2(1)=2.5
     C                   EVAL      ARRDECF2(2)=1.5
     C                   EVAL      ARRDECF2(3)=0.5
     C     ARRDECF1      ADD       ARRDECF2      ARRDECRES
     C                   EVAL      RES=%CHAR(ARRDECRES(1))+', '
     C                                  +%CHAR(ARRDECRES(2))+', '
     C                                  +%CHAR(ARRDECRES(3))
     C     RES           DSPLY

     D* With Factor 1 as decimal and Factor 2 as integer
     C                   EVAL      ARRDECF1(1)=1.5
     C                   EVAL      ARRDECF1(2)=2.5
     C                   EVAL      ARRDECF1(3)=3.5
     C                   EVAL      ARRINTF2(1)=2
     C                   EVAL      ARRINTF2(2)=1
     C                   EVAL      ARRINTF2(3)=0
     C     ARRDECF1      ADD       ARRINTF2      ARRDECRES
     C                   EVAL      RES=%CHAR(ARRDECRES(1))+', '
     C                                  +%CHAR(ARRDECRES(2))+', '
     C                                  +%CHAR(ARRDECRES(3))
     C     RES           DSPLY

     D* With Factor 1 as integer and Factor 2 as decimal
     C                   EVAL      ARRINTF1(1)=2
     C                   EVAL      ARRINTF1(2)=1
     C                   EVAL      ARRINTF1(3)=0
     C                   EVAL      ARRDECF2(1)=1.5
     C                   EVAL      ARRDECF2(2)=2.5
     C                   EVAL      ARRDECF2(3)=3.5
     C     ARRDECF1      ADD       ARRINTF2      ARRDECRES
     C                   EVAL      RES=%CHAR(ARRDECRES(1))+', '
     C                                  +%CHAR(ARRDECRES(2))+', '
     C                                  +%CHAR(ARRDECRES(3))
     C     RES           DSPLY