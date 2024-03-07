      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : MULANGTA20
      * Sorgente di origine : QTEMP/SRC(MULANGTA20)
      * Esportato il        : 20240306 170241
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulla CALL
     D*---------------------------------------------------------------------
     C                   EXSR      F_CALL
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test SORTA
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
     C                   DO        100
     C                   CALL      'MULANGTB20'
     C                   ENDDO
     C                   ENDSR
