<variable declaration> ::= <DATA_TYPE> <IDENTIFIER> ;
<variable assignment> ::= <DATA_TYPE> <IDENTIFIER> = <LITERAL>|<IDENTIFIER> ;

variable-declaration = declaration
declaration = DATA_TYPE IDENTIFIER O
O = TERMINATOR | ASSIGNMENT G
G = LITERAL | IDENTIFIER
declaration = DATA_TYPE IDENTIFIER ASSIGNMENT LITERAL
declaration = DATA_TYPE IDENTIFIER ASSIGNMENT IDENTIFIER

