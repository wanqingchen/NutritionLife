package com.example.wanqingchen.nutritionlife;

/**
 * Created by wanqingchen on 3/28/18.
 */

public class NutritionReport {
    private String EnergySum,ProteinSum,TotalFatSum,CarbohydrateSum;



    public NutritionReport() {
    }

    public NutritionReport(String EnergySum,String ProteinSum,String TotalFatSum,String CarbohydrateSum) {

        this.EnergySum = EnergySum;
        this.ProteinSum = ProteinSum;
        this.TotalFatSum = TotalFatSum;
        this.CarbohydrateSum = CarbohydrateSum;

    }

    public String getEnergySum() {
        return EnergySum;
    }
    public void setEnergySum(String EnergySum)  {this.EnergySum = EnergySum;}

    public String getProteinSum() {
        return ProteinSum;
    }
    public void setProteinSum(String ProteinSum)  {this.ProteinSum = ProteinSum;}

    public String getCarbohydrateSum() {
        return CarbohydrateSum;
    }
    public void setCarbohydrateSum(String CarbohydrateSum)  {this.CarbohydrateSum = CarbohydrateSum;}

    public String getTotalFatSum() {
        return TotalFatSum;
    }
    public void setTotalFatSum(String TotalFatSum)  {this.TotalFatSum = TotalFatSum; }

}
