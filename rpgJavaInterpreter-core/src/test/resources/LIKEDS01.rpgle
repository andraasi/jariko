     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 19/04/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è l'utilizzo della LIKE di un field
    O *  di un'altra DS dichiarata successivamente.
     V* ==============================================================
     D* Sezione delle variabili.
     
     D FIRST           DS            10
     D  FIRST_F1                                 LIKE(SECOND_F1)

     D SECOND          DS            10
     D  SECOND_F1                    10

     




     
     