lexer grammar MuteLexer;

OPEN_PAREN : '(';
CLOSE_PAREN : ')';
EQUAL : '=';


VAL1 : 'VAL1';
VAL2 : 'VAL2';
NOXMI: '"NOXMI"';

EQ : '(EQ)' ;
NE : '(NE)' ;
GT : '(GT)' ;
GE : '(GE)' ;
LT : '(LT)' ;
LE : '(LE)' ;

COMP : 'COMP' ;
TYPE : 'Type' ;

TIMEOUT : [Tt][Ii][Mm][Ee][Oo][Uu][Tt] ;

NUMBER : [0-9]+;

FAIL : [Ff][Aa][Ii][Ll] ;

WS : [ \r\t\n]+ -> skip ;
EXP: '['.*?']';
EXP2: OPEN_PAREN~[0-9].*?CLOSE_PAREN;

