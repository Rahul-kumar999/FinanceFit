package com.devrahul.financefit;

public class TransactionModel {
    private String amount, desc, id, type, DateTime;

    public TransactionModel() {
    }

    public TransactionModel(String amount, String desc, String id, String type, String DateTime) {
        this.amount = amount;
        this.desc = desc;
        this.id = id;
        this.type = type;
        this.DateTime = DateTime;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
