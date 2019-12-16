package com.compulynx.iMbank.dal.operations;

import org.joda.time.DateTime;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DateHelperUtil {


    public static void main(String[] args){
        timeNow();
        dateTime();
        dateTimeInt();

        shortenString("Unga Ltd");
    }

    public static Timestamp convertStrDtEx(String Dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        java.util.Date date = null;
        try {
            date = sdf.parse(Dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Timestamp timest = new Timestamp(date.getTime());
        //java.sql.Timestamp convDt = new java.sql.Timestamp(dateFormat.);
        return timest;

    }

    public static int dateInt(){
        DateTime dateTime = new DateTime();
        return Integer.parseInt(dateTime.toString("yyyyMMdd"));
    }

    public static String transDate(){
        DateTime dateTime = new DateTime();
        return dateTime.toString("MMddyy");
    }

    public static String  headerDate(){
        DateTime dateTime = new DateTime();
        return dateTime.toString("MMddyy");
    }

    public static String todayDate() {
        DateTime dateTime = new DateTime();
        System.out.println(dateTime.toString("MMdd"));
        return dateTime.toString("MMdd");
    }

    public static String timeNow() {
        DateTime dateTime = new DateTime();
        System.out.println(dateTime.toString("HHMMss"));
        return dateTime.toString("HHmmss");
    }

    public static String dateTime() {
        DateTime dateTime = new DateTime();
        System.out.println(dateTime.toString("yyyy-MM-dd HH:MM:ss"));
        //return dateTime.toString("yyyyMMddHHMMss");
        return dateTime.toString("yyyy-MM-dd HH:MM:ss");
    }

    public static String dateTimeInt(){
        DateTime dateTime = new DateTime();
        //System.out.println(dateTime.toString("yyyy-MM-dd HH:MM:ss"));
        return dateTime.toString("yyyyMMddHHMMss");
    }

    public static String dispYearShortDate(String indate) {

        String outformat = "yyyy/MM/dd";
        try {

            String retDate1 = indate;
            String retDay1 = retDate1.substring(0, 2);
            String retMonth1 = retDate1.substring(3, 5);
            String retYear1 = retDate1.substring(6, 10);
            retDate1 = retYear1 + "/" + retMonth1 + "/" + retDay1;

            return retDate1;
        } catch (Exception e) {
            // System.out.println("Unparsable dates");
            // System.out.println("input date :"+indate);
            // System.out.println("required format :"+outformat);
        }
        return "";
    }

    public static String formatBidDate(String indate) {
        String informat = "yyyy-MM-dd HH:mm:ss";
        String outformat = "yyyyMMdd";
        try {
            // System.out.println("indate" + indate);
            SimpleDateFormat insdf = new SimpleDateFormat(informat);
            SimpleDateFormat outsdf = new SimpleDateFormat(outformat);
            java.util.Date indt = insdf.parse(indate);
            return outsdf.format(indt);
        } catch (Exception e) {
            // System.out.println("Unparsable dates");
            // System.out.println("input date :"+indate);
            // System.out.println("required format :"+informat);
        }
        return "";
    }

    public static String paddedString(String input,int maxlength){

        StringBuilder sb=new StringBuilder();
        for(int i=maxlength-input.length();i > 0;i--){
            sb.append("0");
        }
        sb.append(input);

        return sb.toString();
    }


    public static String appendString(String source, String pad, int length)
    {
        if (source == null)
        {
            return null;
        }

        if (pad == null)
        {
            return source;
        }

        StringBuffer result = new StringBuffer();

        result.append(source);

        while (result.length() < length)
        {
            result.insert(result.length(), pad);
        }

        return result.toString();
    }


    public static boolean validateFields(String input){
        boolean valid=true;
        if(input==null||input.trim().equals("")){
            System.out.println("Invalid Input>>"+input);
            valid=false;
        }

        return valid;
    }


    public static String shortenString(String companyName){
        String shortName="";
        System.out.println("Company Name>>"+companyName.trim());
        shortName=companyName.trim().substring(0,3).toUpperCase();
        System.out.println("ShortName>>"+shortName);

        return shortName;
    }

}
