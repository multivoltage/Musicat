package com.multivoltage.musicat.entity;

public class Item {

    private String firstTitle ="";
    private String secondTitle = "";

    public Item(String firstTitle, String secondTitle){
        this.firstTitle = firstTitle;
        this.secondTitle = secondTitle;
    }
    public Item(){
    }

    protected void setFirstTitle(String firstTitle) {
        this.firstTitle = firstTitle;
    }

    protected void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }

    public String getFirstTitle() {
        return firstTitle;
    }

    public String getSecondTitle() {
        return secondTitle;
    }
}
