package com.example.wanqingchen.nutritionlife;

/**
 * Created by wanqingchen on 3/26/18.
 */

public class FoodReport {

    private String Name;
    private String Energy,Protein,TotalFat,SaturatedFat,Carbohydrate,Cholesterol;

    public FoodReport() {
    }

    public FoodReport(String Name,String Energy,String Protein,String TotalFat,String SaturatedFat,String Carbohydrate,String Cholesterol) {
        this.Name = Name;
        this.Energy = Energy;
        this.Protein = Protein;
        this.TotalFat = TotalFat;
        this.SaturatedFat = SaturatedFat;
        this.Carbohydrate = Carbohydrate;
        this.Cholesterol = Cholesterol;
    }

    public String getname() {
        return Name;
    }
    public void setname(String Name)  {this.Name = Name;}

    public String getEnergy() {
        return Energy;
    }
    public void setEnergy(String Energy)  {this.Energy = Energy;}

    public String getProtein() {
        return Protein;
    }
    public void setProtein(String Protein)  {this.Protein = Protein;}

    public String getSaturatedFat() {
        return SaturatedFat;
    }
    public void setSaturatedFat(String SaturatedFat)  {this.SaturatedFat = SaturatedFat;}

    public String getCarbohydrate() {
        return Carbohydrate;
    }
    public void setCarbohydrate(String Carbohydrate)  {this.Carbohydrate = Carbohydrate;}

    public String getTotalFat() {
        return TotalFat;
    }
    public void setTotalFat(String TotalFat)  {this.TotalFat = TotalFat;}

    public String getCholesterol() {
        return Cholesterol;
    }
    public void setCholesterol(String Cholesterol)  {this.Cholesterol = Cholesterol;}

}

