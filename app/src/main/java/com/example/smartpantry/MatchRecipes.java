package com.example.smartpantry;

public class MatchRecipes {

    //compare names
    public static String matchName(String name){
        String n = name.trim().toLowerCase().replaceAll("\\s+", " ");
        if(n.endsWith("oes")){
            n =n.substring(0, n.length() - 2);
        } else if (n.endsWith("ies")) {
            n = n.substring(0, n.length() - 3) + "y";
        } else if (n.endsWith("s") && !n.endsWith("ss")) {
            n = n.substring(0, n.length() - 1);
        }
        return n;

        }

        //convert ub=nit measurements
    public static String baseUnit(String unit){
        if(unit.equals("kg") || unit.equals("g")) return "g";
        if(unit.equals("l") || unit.equals("ml")) return "ml";
        return  unit;
    }

    //convert amount to base unit
    public static double baseAmount(double quantity, String unit){
        if(unit.equals("kg") || unit.equals("l")) return quantity * 1000;
        return quantity;
    }

    }

