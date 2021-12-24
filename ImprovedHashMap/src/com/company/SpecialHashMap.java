package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialHashMap extends HashMap<String, Integer> {
    public Integer iloc(Integer pos) {
        List<String> keys = new ArrayList<>(keySet());
        Collections.sort(keys);

        if (pos < 0 || pos >= keys.size()) throw new IllegalArgumentException("Position is outside of bounds");

        return get(keys.get(pos));
    }

    public HashMap<String, Integer> ploc(String predicate) {
        String predicateCopy = predicate.replace("(", "").replace(")", "");

        List<String> keys = new ArrayList<>(keySet());

        String[] predicates = predicateCopy.split(",");
        for (int i = 0; i < predicates.length; i++) {
            predicates[i] = predicates[i].trim();
        }

        keys.removeIf(s -> s.split(",").length != predicates.length);
        String[][] preparedKeys = new String[keys.size()][];

        for (int i = 0; i < keys.size(); i++) {
            preparedKeys[i] = keys.get(i).split(",");
            for (int j = 0; j < preparedKeys[i].length; j++) {
                preparedKeys[i][j] = preparedKeys[i][j].replace("(", "").replace(")", "").trim();
            }
        }

        for (int i = 0; i < predicates.length; i++) {
            String preparedPredicate = predicates[i];

            Pattern p = Pattern.compile("^[^0-9]*(\\d*([\\.,]\\d*)?)$");
            Matcher a = p.matcher(preparedPredicate);

            if (a.matches()) {
                double predicateValue;
                try {
                    predicateValue = Double.parseDouble(a.group(1));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Can't parse predicate: " + preparedPredicate);
                }

                int finalI = i;
                keys.removeIf(s -> {
                    double keyValue;
                    try {
                        keyValue = Double.parseDouble(s.split(",")[finalI].replace("(", "").replace(")", "").trim());
                    } catch (NumberFormatException ex) {
                        return true;
                    }

                    if (preparedPredicate.startsWith("<")) {
                        if (preparedPredicate.startsWith("<>")) {
                            return keyValue == predicateValue;
                        } else if (preparedPredicate.startsWith("<=")) {
                            return keyValue > predicateValue;
                        } else {
                            return keyValue >= predicateValue;
                        }
                    } else if (preparedPredicate.startsWith(">")) {
                        if (preparedPredicate.startsWith(">=")) {
                            return keyValue < predicateValue;
                        } else {
                            return keyValue <= predicateValue;
                        }
                    } else if (preparedPredicate.startsWith("=")) {
                        return keyValue != predicateValue;
                    } else throw new IllegalArgumentException("Can't parse predicate: " + preparedPredicate);
                });
            }
        }

        HashMap<String, Integer> result = new HashMap<>();

        for (String key : keys) {
            result.put(key, get(key));
        }

        return result;
    }
}
