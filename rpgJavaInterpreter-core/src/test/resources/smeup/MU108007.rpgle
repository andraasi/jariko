      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU108007
      * Sorgente di origine : QTEMP/SRC(MU108007)
      * Esportato il        : 20240415 082654
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 22/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * CALL con parametri definiti in line e in specifiche D
     V* ==============================================================
     D A80_IA10        S             10
     D A80_IN10I       S             10I 0
     D OUTPUT_P        S             80    VARYING
      *
     D P_PROCEDTST     PR            80
     D  A80_A10                      10
     D   A80_TXTC      C                   '*BINARY'
     D  A80_N10I                     10I 0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU108007'
     C                   EVAL      £DBG_Sez = 'A80'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A80
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico LIKEDS
      *---------------------------------------------------------------------
     C     SEZ_A80       BEGSR
    OA* A£.TPDA(I)
     D* CALL con LIKEDS
     C                   EVAL      £DBG_Pas='P07'
     C                   EVAL      A80_IA10 = A80_TXTC
     C                   EVAL      A80_IN10I = 100
     C                   EVAL      OUTPUT_P=P_PROCEDTST(A80_IA10:A80_IN10I)
     C                   EVAL      £DBG_Str=%TRIM(OUTPUT_P)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
    RD* Procedura di test con costante I
      *---------------------------------------------------------------------
     PP_PROCEDTST      B
     D P_PROCEDTST     PI            80
     D  A80_A10                      10
     D   A80_TXTC      C                   '*BINARY'
     D  A80_N10I                     10I 0
      *
     D RETVAL          S             80
      *
     C                   EVAL      RETVAL='Res:'+%TRIM(A80_A10)+
     C                                    ':'+%CHAR(A80_N10I)
     C                   RETURN    RETVAL
      *
     PP_PROCEDTST      E
      *---------------------------------------------------------------------
