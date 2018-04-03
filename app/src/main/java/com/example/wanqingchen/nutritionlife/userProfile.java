package com.example.wanqingchen.nutritionlife;

/**
 * Created by wanqingchen on 3/22/18.
 */



public class userProfile{

    private String gender,diabetesType;
    private String Calories,Fat,Protein,Carbs,Birthday,BloodGlucose;

    public userProfile() {
    }

    public userProfile(String gender,String diabetesType,String strBloodGlucose, String strCalories,String strFat,String strProtein, String strCarbs, String strBirthday) {
        this.gender = gender;
        this.diabetesType = diabetesType;
        this.Birthday = strBirthday;
        this.BloodGlucose = strBloodGlucose;
        this.Calories = strCalories;
        this.Fat = strFat;
        this.Protein = strProtein;
        this.Carbs = strCarbs;
    }

    public String getBloodGlucose() {
        return BloodGlucose;
    }
    public void setBloodGlucose(String strBloodGlucose)  {this.BloodGlucose = strBloodGlucose;}

    public String getGender() {
        return gender;
    }
    public void setGender(String gender)  {this.gender = gender;}

    public String getDiabetesType() {
        return diabetesType;
    }
    public void setDiabetesType(String diabetesType)  {this.diabetesType = diabetesType;}

    public String getBirthday() {
        return Birthday;
    }
    public void setBirthday(String strBirthday)  {this.Birthday = strBirthday;}

    public String getCalories() {
        return Calories;
    }
    public void setCalories(String strCalories)  {this.Calories = strCalories;}

    public String getFat() {
        return Fat;
    }
    public void setFat(String strFat)  {this.Fat = strFat;}

    public String getProtein() {
        return Protein;
    }
    public void setProtein(String strProtein)  {this.Protein = strProtein;}

    public String getCarbs() {
        return Carbs;
    }
    public void setCarbs(String strCarbs)  {this.Carbs = strCarbs;}

}
