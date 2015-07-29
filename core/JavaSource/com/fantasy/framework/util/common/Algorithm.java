package com.fantasy.framework.util.common;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Deprecated
@SuppressWarnings("unchecked")
public class Algorithm {

    public static int binarySearch(List<?> items, Object val, Comparator comparator) {
        int startIndex = 0;
        int stopIndex = items.size() - 1;
        int middle = (int) Math.floor((double) (stopIndex + startIndex) / 2.0D);
        int s;
        while ((s = middle < 0 ? (middle = 0) : comparator.compare(items.get(middle), val)) != 0 && startIndex < stopIndex) {
            if (s > 0) {
                stopIndex = middle - 1;
            } else if (s < 0) {
                startIndex = middle + 1;
            }
            middle = (int) Math.floor((stopIndex + startIndex) / 2);
        }
        return s < 0 ? -1 : (s != 0 ? middle + 1 : middle);
    }

    public static <T> int binarySearch(T[] items, T val, Comparator comparator) {
        int startIndex = 0;
        int stopIndex = items.length - 1;
        int middle = (int) Math.floor((double) (stopIndex + startIndex) / 2.0D);
        int s;
        while (((s = comparator.compare(items[middle], val)) != 0) && (startIndex < stopIndex)) {
            if (s > 0) {
                stopIndex = middle - 1;
            } else if (s < 0) {
                startIndex = middle + 1;
            }
            middle = (int) Math.floor((stopIndex + startIndex) / 2);
        }
        return s < 0 ? -1 : (s != 0 ? middle + 1 : middle);
    }

    public static int binarySearch(String[] items, String val) {
        int startIndex = 0;
        int stopIndex = items.length - 1;
        int middle = (int) Math.floor((double) (stopIndex + startIndex) / 2.0D);
        int s;
        while (((s = items[middle].compareTo(val)) != 0) && (startIndex < stopIndex)) {
            if (s > 0) {
                stopIndex = middle - 1;
            } else if (s < 0) {
                startIndex = middle + 1;
            }
            middle = (int) Math.floor((stopIndex + startIndex) / 2);
        }
        return s < 0 ? -1 : (s != 0 ? middle + 1 : middle);
    }

    public static <T> void bubbleSort(T[] src, Comparator comparator) {
        int len = src.length;
        int n = 0;
        for (int i = 0; i < len; n = 0) {
            for (int j = len - 1; j > i; j--) {
                if (comparator.compare(src[j], src[j - 1]) > 0) {
                    swap(src, j, j - 1);
                    n++;
                }
            }
            if (n == 0) {
                break;
            }
            i++;
        }
    }

    public static <T> void insertSort(T[] src, Comparator comparator) {
        int len = src.length;
        for (int i = 1; i < len; i++) {
            for (int j = 0; j < i; j++) {
                if (comparator.compare(src[i], src[j]) > 0) {
                    swap(src, j, i);
                }
            }
        }
    }

    public static <T> void swap(T[] data, int i, int j) {
        T temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private static void printResult(int i, Object[] src) {
        System.out.println(i + "|" + Arrays.toString(src));
    }

}