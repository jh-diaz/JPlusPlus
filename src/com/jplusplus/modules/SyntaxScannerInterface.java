package com.jplusplus.modules;

import com.jplusplus.datatypes.DataType;

import java.util.HashMap;

public interface SyntaxScannerInterface {
    //bale lahat ng variable declarations, papasok dito
    //tapos may property yung TOKEN nila na line number
    //so if sa current token (kunware nag access si output operation ng variable
    //tapos si output operation token nasa line number 57
    //icompare yung line number attribute ng token ni variable
    //if less than 57, oks siya
    //if greater than, then hindi. kasi di pa siya existing that time
    public HashMap<Token, DataType> getVariableSymbolicTable();
}
