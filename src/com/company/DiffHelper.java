package com.company;

import java.util.HashMap;
import java.util.Map;

/*
 * Class that contains the methods which will handle comparing and indexing the MRZ-code from a box-file.
 *
 * @author Lukas Hjernquist & Olle Gardell
 */
public class DiffHelper {

    public Integer getIndexOfStart(String correctMrzString, String refinedHashmapString) {
        int indexOfStartCounter = 0;
        int redfinedCounter = 0;
        String faultyChar = diff(correctMrzString, refinedHashmapString).second;
        for (int i = 0; i < refinedHashmapString.length(); ++i) {
            if (refinedHashmapString.charAt(redfinedCounter) != faultyChar.charAt(i)) {
                if (refinedHashmapString.charAt(redfinedCounter + 1) != faultyChar.charAt(i) &&
                        refinedHashmapString.charAt(redfinedCounter + 2) != faultyChar.charAt(i + 1)) {
                    indexOfStartCounter--;
                    break;
                }
                redfinedCounter++;
            }
            redfinedCounter++;
            indexOfStartCounter++;
        }
        return indexOfStartCounter;
    }

    public Integer getIndexOfEnd(int indexOfStartCounter, String correctMrzString) {
        return indexOfStartCounter + correctMrzString.length();
    }

    private DiffHelper.Pair<String> diff(String correctMrzString, String refinedHashmapString) {
        return mrzHelper(correctMrzString, refinedHashmapString, new HashMap<>());
    }

    private DiffHelper.Pair<String> mrzHelper(String correctMrzString, String refinedHashmapString, Map<Long, DiffHelper.Pair<String>> lookup) {
        long key = ((long) correctMrzString.length()) << 32 | refinedHashmapString.length();
        if (!lookup.containsKey(key)) {
            DiffHelper.Pair<String> value;
            if (correctMrzString.isEmpty() || refinedHashmapString.isEmpty()) {
                value = new DiffHelper.Pair<>(correctMrzString, refinedHashmapString);
            } else if (correctMrzString.charAt(0) == refinedHashmapString.charAt(0)) {
                value = mrzHelper(correctMrzString.substring(1), refinedHashmapString.substring(1), lookup);
            } else {
                DiffHelper.Pair<String> correctMrzSubString = mrzHelper(correctMrzString.substring(1), refinedHashmapString, lookup);
                DiffHelper.Pair<String> refinedHashmapSubString = mrzHelper(correctMrzString, refinedHashmapString.substring(1), lookup);
                if (correctMrzSubString.first.length() + correctMrzSubString.second.length() < refinedHashmapSubString.first.length() +
                        refinedHashmapSubString.second.length()) {
                    value = new DiffHelper.Pair<>(correctMrzString.charAt(0) + correctMrzSubString.first, correctMrzSubString.second);
                } else {
                    value = new DiffHelper.Pair<>(refinedHashmapSubString.first, refinedHashmapString.charAt(0) + refinedHashmapSubString.second);
                }
            }
            lookup.put(key, value);
        }
        return lookup.get(key);
    }

    public static class Pair<T> {
        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        public final T first, second;

        public String toString() {
            return second + "  :   " + first;
        }
    }
}
