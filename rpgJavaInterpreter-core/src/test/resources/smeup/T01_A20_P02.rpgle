     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D DURATION        S             20  0
     D EXT_LOOP        S              2  0  INZ(5)
     D MSG             S            100
     D £DBG_Str        S           2560    VARYING
     D A20_A15         S             15
     D NNN             S              6  0 INZ(100000)

     D VAR1            S             10
     D VAR2            S             10
     D VAR3            S             10
     D VAR4            S             10
     D VAR5            S             10
     D VAR6            S             10
     D VAR7            S             10
     D VAR8            S             10
     D VAR9            S             10
     D VAR10           S             10
     D VAR11           S             10
     D VAR12           S             10
     D VAR13           S             10
     D VAR14           S             10
     D VAR15           S             10
     D VAR16           S             10
     D VAR17           S             10
     D VAR18           S             10
     D VAR19           S             10
     D VAR20           S             10
     D VAR21           S             10
     D VAR22           S             10
     D VAR23           S             10
     D VAR24           S             10
     D VAR25           S             10
     D VAR26           S             10
     D VAR27           S             10
     D VAR28           S             10
     D VAR29           S             10
     D VAR30           S             10
     D VAR31           S             10
     D VAR32           S             10
     D VAR33           S             10
     D VAR34           S             10
     D VAR35           S             10
     D VAR36           S             10
     D VAR37           S             10
     D VAR38           S             10
     D VAR39           S             10
     D VAR40           S             10
     D VAR41           S             10
     D VAR42           S             10
     D VAR43           S             10
     D VAR44           S             10
     D VAR45           S             10
     D VAR46           S             10
     D VAR47           S             10
     D VAR48           S             10
     D VAR49           S             10
     D VAR50           S             10
     D VAR51           S             10
     D VAR52           S             10
     D VAR53           S             10
     D VAR54           S             10
     D VAR55           S             10
     D VAR56           S             10
     D VAR57           S             10
     D VAR58           S             10
     D VAR59           S             10
     D VAR60           S             10
     D VAR61           S             10
     D VAR62           S             10
     D VAR63           S             10
     D VAR64           S             10
     D VAR65           S             10
     D VAR66           S             10
     D VAR67           S             10
     D VAR68           S             10
     D VAR69           S             10
     D VAR70           S             10
     D VAR71           S             10
     D VAR72           S             10
     D VAR73           S             10
     D VAR74           S             10
     D VAR75           S             10
     D VAR76           S             10
     D VAR77           S             10
     D VAR78           S             10
     D VAR79           S             10
     D VAR80           S             10
     D VAR81           S             10
     D VAR82           S             10
     D VAR83           S             10
     D VAR84           S             10
     D VAR85           S             10
     D VAR86           S             10
     D VAR87           S             10
     D VAR88           S             10
     D VAR89           S             10
     D VAR90           S             10
     D VAR91           S             10
     D VAR92           S             10
     D VAR93           S             10
     D VAR94           S             10
     D VAR95           S             10
     D VAR96           S             10
     D VAR97           S             10
     D VAR98           S             10
     D VAR99           S             10
     D VAR100          S             10
      *---------------------------------------------------------------------

     C                   DO        EXT_LOOP
     C                   TIME                    £DBG_TIMINI
     C                   DO        NNN
     C                   EXSR      SUB_A20_A
     C                   ENDDO
     C                   EVAL      £DBG_Str=A20_A15
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
     C                   ENDDO
      *---------------------------------------------------------------------

     C     SUB_A20_A     BEGSR
      *
     C                   EVAL      A20_A15='Lorem quam'
      *
     C                   ENDSR
      *---------------------------------------------------------------------

      * Utilities
     C     PRINT         BEGSR
     C     £DBG_TIMEND   SUBDUR    £DBG_TIMINI   DURATION:*MS
     C                   EVAL      MSG='Duration:' + %CHAR(DURATION/1000)
     C     MSG           DSPLY
     C                   ENDSR