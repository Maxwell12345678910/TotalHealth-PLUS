package com.gamecodeschool.totalhealthplus;

public class CalorieGoal {

    private String date;
    private String description;
    private String category;
    private boolean isGoalMet;

    public CalorieGoal(String newDate, String newDescription, String newCategory, boolean isMet){
        this.date = newDate;
        this.description = newDescription;
        this.category = newCategory;
        this.isGoalMet = isMet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isGoalMet() {
        return isGoalMet;
    }

    public void setGoalMet(boolean goalMet) {
        isGoalMet = goalMet;
    }

}
