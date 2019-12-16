package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardPrint {
    public int id;
    public String cardNumber;
    public String accountNumber;
    public String accountName;
    public int printedBy;
    public String printerSerialNo;
    public boolean printStatus;
    public int branchPrinted;
    public String datePrinted;
    public int respCode;
    public String respMessage;
    public int accountBranch;
    public String cardFormatId;
    public String printerId;
    //public int usrId;

    public CardPrint(int respCode,String respMessage,String cardNumber,String accountNumber,String accountName,int accountBranch,String cardFormatId,String printerId)
    {
        super();
        this.respCode=respCode;
        this.respMessage=respMessage;
        this.cardNumber=cardNumber;
        this.accountName=accountName;
        this.accountNumber=accountNumber;
        this.accountBranch=accountBranch;
        this.cardFormatId = cardFormatId;
        this.printerId = printerId;
        //this.usrId = usrId;
     }
    public CardPrint(int id,
           String cardNumber,
           String accountName,
           String accountNumber,
           int printedBy,
           String printerSerialNo,
           boolean printStatus,
           int branchPrinted,
           String datePrinted,
           int respCode,
           String respMessage,String cardFormatId )
    {
        this.cardNumber=cardNumber;
        this.accountNumber=accountNumber;
        this.accountName=accountName;
        this.printedBy=printedBy;
        this.printerSerialNo=printerSerialNo;
        this.printStatus=printStatus;
        this.branchPrinted=branchPrinted;
        this.datePrinted=datePrinted;
        this.respCode=respCode;
        this.respMessage=respMessage;
        this.cardFormatId = cardFormatId;
        //this.usrId = usrId;
     }
    public CardPrint()
    {
        super();

    }


}
