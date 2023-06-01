package com.zerobase.stockdividendproject.model.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Month {

    JAN("Jan", 1)
    , FEB("Feb", 2)
    , MAR("Mar", 3)
    , APR("Apr", 4)
    , MAY("May", 5)
    , JUN("Jun", 6)
    , JUL("Jul", 7)
    , AUG("Aug", 8)
    , SEP("Sep", 9)
    , OCT("Oct", 10)
    , NOV("Nov", 11)
    , DEC("Dec", 12)

    ;

    private String string;
    private int number;

    public static int strToNumber(String string) {
        for(var month : Month.values()) {
            if (month.string.equals(string)) {
                return month.number;
            }
        }

        return -1;
    }
}
