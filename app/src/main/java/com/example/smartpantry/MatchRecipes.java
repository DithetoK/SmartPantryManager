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

    }

