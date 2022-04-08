package com.example.testproject;

public class recipe {

    // Variable to store data corresponding to level keyword in database
    private String recipeName;

    // Variable to store data corresponding to level keyword in database
    private String level;

    // Variable to store data corresponding to level keyword in database
    private String image;

    // Variable to store data corresponding to level keyword in database
    private String time;

    // Variable to store data corresponding to level keyword in database
    private String category;

    // Variable to store data corresponding to level keyword in database
    private String favourite;

    // Mandatory empty constructor for use of FirebaseUI
    public recipe() {
    }

    // Getter and setter method
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

}

