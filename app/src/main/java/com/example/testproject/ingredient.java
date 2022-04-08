package com.example.testproject;

public class ingredient {

    // Variable to store data corresponding to level keyword in database
    private String recipeName;

    // Variable to store data corresponding to level keyword in database
    private String item;

    // Mandatory empty constructor for use of FirebaseUI
    public ingredient() {
    }

    // Getter and setter method
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
