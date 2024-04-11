package com.gamecodeschool.totalhealthplus;

public class CalorieGoal {

    private String date;
    private int calories;
    private boolean isGoalMet;

    public CalorieGoal(String newDate, int newCals, boolean isMet){
        this.date = newDate;
        this.calories = newCals;
        this.isGoalMet = isMet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int newCalories) {
        this.calories = newCalories;
    }

    public boolean isGoalMet() {
        return isGoalMet;
    }

    public void setGoalMet(boolean goalMet) {
        isGoalMet = goalMet;
    }

}
