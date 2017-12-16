


statement -> if|elseif|else|while|i/o operation|variable-declaration|assignment

block-statement -> { statement }

variable-declaration -> data-type assignment|I line-terminator
I -> identifier
L -> literal
operand -> L|I
assignment -> I equals expression
expression -> arithmetic-expression|relation-comparison
arithmetic-expression -> operand { arithmetic-operator  operand }
arithmetic-operator -> + | - | * | / | %
relational-comparison -> arithmetic-expression relational-operator arithmetic-expression

i/o operation -> INP|OUT line-terminator
INP -> input.integer|input.fraction|input.nibble|input.word|input.bool identifier
OUT -> output identifier|literal


if-statement -> if expression {..} endif //syntax wise, 1+1 is correct. needs semantic checking
//since if elseif clause requires a boolean attribute
elseif-statement -> if-statement elseif expression {..} endelseif
else-statement -> elseif-statement|ifstatement else expression {..} endelse

while-statement -> while expression {..} endwhile
line-terminator -> ;


for-statement -> for variable-declaration line-terminator relational-comparison line-terminator expression line-terminator

do-while-statement -> do {..}
cond-expression -> cond relational-comparison line-terminator



