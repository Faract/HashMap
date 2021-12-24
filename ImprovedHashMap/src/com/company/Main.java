package com.company;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        SpecialHashMap map = new SpecialHashMap();
        map.put("value1", 1);
        map.put("value2", 2);
        map.put("value3", 3);
        map.put("1", 10);
        map.put("2", 20);
        map.put("3", 30);
        map.put("(1, 5)", 100);
        map.put("(5, 5)", 200);
        map.put("(10, 5)", 300);
        map.put("(1, 5, 3)", 400);
        map.put("(5, 5, 4)", 500);
        map.put("(10, 5, 5)", 600);

        System.out.println("Testing iloc");
        for (int i : new int[]{0, 2, 5, 8}) {
            System.out.println("map.iloc(" + i + ") => " + map.iloc(i));
        }

        System.out.println("Testing ploc");
        for (String predicate : new String[]{">=1", "<3", ">0, >0", ">=10, >0", "<5, >=5, >=3"}) {
            System.out.println("map.ploc(\"" + predicate + "\") => " + map.ploc(predicate).toString());
        }
    }
}
