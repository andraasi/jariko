      *
      * Z-SUB operation with array
      *

     D ARR             S              2  0 DIM(3)
     D ARR_RES         S              2  0 DIM(3)
     D RES             S             500          VARYING

     C                   EVAL      ARR(1)=1
     C                   EVAL      ARR(2)=-2
     C                   EVAL      ARR(3)=3
     C                   Z-SUB     ARR           ARR_RES
     C                   EVAL      RES=%CHAR(ARR_RES(1))+', '
     C                                  +%CHAR(ARR_RES(2))+', '
     C                                  +%CHAR(ARR_RES(3))
     C     RES           DSPLY