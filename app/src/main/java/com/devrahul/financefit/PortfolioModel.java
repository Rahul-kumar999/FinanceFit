package com.devrahul.financefit;

public class PortfolioModel {
    private String amount, Name, StartDate, EndDate,type,Id,TypeA;

    public PortfolioModel(String amount, String name, String startDate, String endDate, String type, String id, String typea) {
        this.amount = amount;
        Name = name;
        StartDate = startDate;
        EndDate = endDate;
        this.type = type;
        Id = id;
        TypeA = typea;
    }

    public String getTypeA() {
        return TypeA;
    }

    public void setTypeA(String typeA) {
        TypeA = typeA;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
