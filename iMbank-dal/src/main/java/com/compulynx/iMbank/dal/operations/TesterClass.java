package com.compulynx.iMbank.dal.operations;


public class TesterClass {


    public static void main(String[] args){

        String str = "EMMILY JELAGAT    ID 26451620               \n";
        String str1 = "PRIDEINN HOTELS & INVESTMENTS LTD           \n";

        str = str.replaceAll("\\D+","");
        str1 = str1.replaceAll("\\D+","");
        System.out.println("Result##" + str1);
    }
}
