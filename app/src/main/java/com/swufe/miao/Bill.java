package com.swufe.miao;

public class Bill {
    private int id;
    private String user_id;
    private int category_id; //类别编号
    private int flag; //支出-0 收入-1
    private float cost; //金额
    private int year;
    private int month;
    private int day;

    public Bill() {
        super();
        user_id = "";
        category_id = 1;
        flag=0;
        cost=0;
        year=0;
        month=0;
        day=0;
    }
    public Bill(String user_id, int category_id,int flag,
                float cost,int year,int month,int day) {
        super();
        this.user_id = user_id;
        this.category_id =category_id ;
        this.flag =flag ;
        this.cost =cost ;
        this.year =year ;
        this.month =month ;
        this.day =day ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
