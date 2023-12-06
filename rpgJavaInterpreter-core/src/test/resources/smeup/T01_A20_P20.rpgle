     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D DURATION        S             20  0
     D EXT_LOOP        S              3  0  INZ(1)
     D MSG             S            100
     D £DBG_Str        S           2560    VARYING
     D A20_A30000V     S          30000    VARYING
     D NNN             S              6  0 INZ(100000)

      * Main
     C                   DO        EXT_LOOP
     C                   TIME                    £DBG_TIMINI
     C                   DO        NNN
     C                   EXSR      SUB_A20_L
     C                   ENDDO
     C                   EVAL      £DBG_Str=A20_A30000V
     C                   TIME                    £DBG_TIMEND
     C                   EXSR      PRINT
     C                   ENDDO
      *---------------------------------------------------------------------

    RD* Subsezione di SEZ_A20 P19 e P20
      *---------------------------------------------------------------------
     C     SUB_A20_L     BEGSR
      *
     C                   EVAL      A20_A30000V='Lorem ipsum dolor sit amet, '
     C                             +'consectetur adipiscing elit. Vestibulum '
     C                             +'posuere nisl at neque auctor bibendum. '
     C                             +'Pellentesque eget risus eu mi accumsan '
     C                             +'commodo ut a eros. Aliquam a augue eros. '
     C                             +'Integer vitae cursus arcu. In pulvinar '
     C                             +'erat massa, at pulvinar enim euismod in. '
     C                             +'Vestibulum a posuere risus. Donec bibendum'
     C                             +' facilisis enim ac bibendum. Mauris in '
     C                             +'fringilla nunc.Aliquam odio purus, '
     C                             +'eleifend id posuere id, tristique in '
     C                             +'justo. Morbi in faucibus urna, et iaculis'
     C                             +' lacus. Proin aliquam porttitor ullamcor'
     C                             +'per. Donec malesuada nisi sodales neque su'
     C                             +'scipit, condimentum aliquam diam volutpat.'
     C                             +' Maecenas lacinia, metus nec porta tempor,'
     C                             +' ex quam pharetra risus, at euismod metus '
     C                             +'magna et neque. Etiam neque magna, tristi'
     C                             +'que eget semper eu, consequat eu nisl. Sed'
     C                             +' interdum, eros a maximus ultricies, '
     C                             +'tortor elit hendrerit risus, sit amet elei'
     C                             +'fend justo lectus quis purus. Duis biben'
     C                             +'dum metus et ante hendrerit scelerisque. '
     C                             +'Duis hendrerit metus ut felis suscipit '
     C                             +'dapibus. Donec ac mi eu erat lobortis '
     C                             +'dapibus. Aliquam rutrum risus sed massa '
     C                             +'accumsan dignissim. Vestibulum at libero '
     C                             +'tristique, consequat tortor in, blandit '
     C                             +'orci. Etiam eleifend gravida dui. Nam '
     C                             +'posuere, nibh non facilisis condimentum, '
     C                             +'quam libero ullamcorper quam, eu fringilla'
     C                             +' est risus nec quam. Integer laoreet elit '
     C                             +'metus, sed ullamcorper augue congue vel. '
     C                             +'Mauris eget aliquam ante. Cras sit amet '
     C                             +'nulla et mi posuere porttitor quis '
     C                             +'elementum lacus. Donec eget placerat '
     C                             +'ligula, finibus bibendum leo.'
     C                             +'Lorem ipsum dolor sit amet, '
     C                             +'consectetur adipiscing elit. Vestibulum '
     C                             +'posuere nisl at neque auctor bibendum. '
     C                             +'Pellentesque eget risus eu mi accumsan '
     C                             +'commodo ut a eros. Aliquam a augue eros. '
     C                             +'Integer vitae cursus arcu. In pulvinar '
     C                             +'erat massa, at pulvinar enim euismod in. '
     C                             +'Vestibulum a posuere risus. Donec bibendum'
     C                             +' facilisis enim ac bibendum. Mauris in '
     C                             +'fringilla nunc.Aliquam odio purus, '
     C                             +'eleifend id posuere id, tristique in '
     C                             +'justo. Morbi in faucibus urna, et iaculis'
     C                             +' lacus. Proin aliquam porttitor ullamcor'
     C                             +'per. Donec malesuada nisi sodales neque su'
     C                             +'scipit, condimentum aliquam diam volutpat.'
     C                             +' Maecenas lacinia, metus nec porta tempor,'
     C                             +' ex quam pharetra risus, at euismod metus '
     C                             +'magna et neque. Etiam neque magna, tristi'
     C                             +'que eget semper eu, consequat eu nisl. Sed'
     C                             +' interdum, eros a maximus ultricies, '
     C                             +'tortor elit hendrerit risus, sit amet elei'
     C                             +'fend justo lectus quis purus. Duis biben'
     C                             +'dum metus et ante hendrerit scelerisque. '
     C                             +'Duis hendrerit metus ut felis suscipit '
     C                             +'dapibus. Donec ac mi eu erat lobortis '
     C                             +'dapibus. Aliquam rutrum risus sed massa '
     C                             +'accumsan dignissim. Vestibulum at libero '
     C                             +'tristique, consequat tortor in, blandit '
     C                             +'orci. Etiam eleifend gravida dui. Nam '
     C                             +'posuere, nibh non facilisis condimentum, '
     C                             +'quam libero ullamcorper quam, eu fringilla'
     C                             +' est risus nec quam. Integer laoreet elit '
     C                             +'metus, sed ullamcorper augue congue vel. '
     C                             +'Mauris eget aliquam ante. Cras sit amet '
     C                             +'nulla et mi posuere porttitor quis '
     C                             +'elementum lacus. Donec eget placerat '
     C                             +'ligula, finibus bibendum leo.'
      *
     C                   ENDSR
      *---------------------------------------------------------------------

      * Utilities
     C     PRINT         BEGSR
     C     £DBG_TIMEND   SUBDUR    £DBG_TIMINI   DURATION:*MS
     C                   EVAL      MSG='Duration:' + %CHAR(DURATION/1000)
     C     MSG           DSPLY
     C                   ENDSR