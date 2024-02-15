      *
      * ADD operation with array
      *

     D RES             S             500          VARYING
     D ARR_INT         S              2  0 DIM(3)
     D ARR_INT_S2      S              2  0 DIM(2)
     D ARR_INT_RES     S              2  0 DIM(3)
     D ARR_INT_RES_S2  S              2  0 DIM(2)
     D ARR_DEC         S              2  1 DIM(3)
     D ARR_DEC_RES     S              2  1 DIM(3)

     D* With integer values
     C                   EVAL      ARR_INT(1)=1
     C                   EVAL      ARR_INT(2)=2
     C                   EVAL      ARR_INT(3)=3
     C                   EVAL      ARR_INT_RES(1)=3
     C                   EVAL      ARR_INT_RES(2)=2
     C                   EVAL      ARR_INT_RES(3)=1
     C                   ADD       ARR_INT       ARR_INT_RES
     C                   EVAL      RES=%CHAR(ARR_INT_RES(1))+', '
     C                                  +%CHAR(ARR_INT_RES(2))+', '
     C                                  +%CHAR(ARR_INT_RES(3))
     C     RES           DSPLY

     D* With decimal values
     C                   EVAL      ARR_DEC(1)=1.5
     C                   EVAL      ARR_DEC(2)=2.5
     C                   EVAL      ARR_DEC(3)=3.5
     C                   EVAL      ARR_DEC_RES(1)=2.5
     C                   EVAL      ARR_DEC_RES(2)=1.5
     C                   EVAL      ARR_DEC_RES(3)=0.5
     C                   ADD       ARR_DEC       ARR_DEC_RES
     C                   EVAL      RES=%CHAR(ARR_DEC_RES(1))+', '
     C                                  +%CHAR(ARR_DEC_RES(2))+', '
     C                                  +%CHAR(ARR_DEC_RES(3))
     C     RES           DSPLY

     D* With Factor 2 as decimal and Result as integer
     C                   EVAL      ARR_DEC(1)=1.5
     C                   EVAL      ARR_DEC(2)=2.5
     C                   EVAL      ARR_DEC(3)=3.5
     C                   EVAL      ARR_INT_RES(1)=2
     C                   EVAL      ARR_INT_RES(2)=1
     C                   EVAL      ARR_INT_RES(3)=0
     C                   ADD       ARR_DEC       ARR_INT_RES
     C                   EVAL      RES=%CHAR(ARR_INT_RES(1))+', '
     C                                  +%CHAR(ARR_INT_RES(2))+', '
     C                                  +%CHAR(ARR_INT_RES(3))
     C     RES           DSPLY

     D* With Factor 2 as integer and Result as decimal
     C                   EVAL      ARR_INT(1)=1
     C                   EVAL      ARR_INT(2)=2
     C                   EVAL      ARR_INT(3)=3
     C                   EVAL      ARR_DEC_RES(1)=2.5
     C                   EVAL      ARR_DEC_RES(2)=1.5
     C                   EVAL      ARR_DEC_RES(3)=0.5
     C                   ADD       ARR_INT       ARR_DEC_RES
     C                   EVAL      RES=%CHAR(ARR_DEC_RES(1))+', '
     C                                  +%CHAR(ARR_DEC_RES(2))+', '
     C                                  +%CHAR(ARR_DEC_RES(3))
     C     RES           DSPLY

     D* With integer values and when Factor 2 size is greater than Result
     C                   EVAL      ARR_INT(1)=1
     C                   EVAL      ARR_INT(2)=2
     C                   EVAL      ARR_INT(3)=3
     C                   EVAL      ARR_INT_RES_S2(1)=3
     C                   EVAL      ARR_INT_RES_S2(2)=2
     C                   ADD       ARR_INT       ARR_INT_RES_S2
     C                   EVAL      RES=%CHAR(ARR_INT_RES_S2(1))+', '
     C                                  +%CHAR(ARR_INT_RES_S2(2))
     C     RES           DSPLY

     D* With integer values and when Factor 2 size is smaller than Result
     C                   EVAL      ARR_INT_S2(1)=1
     C                   EVAL      ARR_INT_S2(2)=2
     C                   EVAL      ARR_INT_RES(1)=3
     C                   EVAL      ARR_INT_RES(2)=2
     C                   EVAL      ARR_INT_RES(3)=1
     C                   ADD       ARR_INT_S2    ARR_INT_RES
     C                   EVAL      RES=%CHAR(ARR_INT_RES(1))+', '
     C                                  +%CHAR(ARR_INT_RES(2))+', '
     C                                  +%CHAR(ARR_INT_RES(3))
     C     RES           DSPLY

     D* With integer values and when Factor 2 isn't as array as Result, but Integer
     C                   EVAL      ARR_INT_RES(1)=1
     C                   EVAL      ARR_INT_RES(2)=2
     C                   EVAL      ARR_INT_RES(3)=3
     C                   ADD       1             ARR_INT_RES
     C                   EVAL      RES=%CHAR(ARR_INT_RES(1))+', '
     C                                  +%CHAR(ARR_INT_RES(2))+', '
     C                                  +%CHAR(ARR_INT_RES(3))
     C     RES           DSPLY