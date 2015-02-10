package com.fantasy.framework.util.common;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class AlgorithmTest {

    private static final Logger LOG = Logger.getLogger(AlgorithmTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBinarySearch() throws Exception {

        String[] array = new String[]{"7","2", "1", "3", "6", "4", "5"};
        Comparator stringComparator = new Comparator<String>() {
            @Override
            public int compare(String o, String t1) {
                return t1.compareTo(o);
            }
        };
        Algorithm.bubbleSort(array,stringComparator);
        LOG.debug(Arrays.toString(array));

        array = new String[]{"7","2", "1", "3", "6", "4", "5"};
        Algorithm.insertSort(array,stringComparator);

        LOG.debug(Arrays.toString(array));

        String[] items = {"1", "2", "3"};

        LOG.debug(Algorithm.binarySearch(items, "1"));
        LOG.debug("===========================");
        LOG.debug(Algorithm.binarySearch(items, "2"));
        LOG.debug("===========================");
        LOG.debug(Algorithm.binarySearch(items, "3"));
        LOG.debug("===========================");
        LOG.debug(Algorithm.binarySearch(items, "4"));

        LOG.debug(Algorithm.binarySearch(items, "5"));

        LOG.debug(Algorithm.binarySearch(items, "6"));

    }


    @Test
    public void testBubbleSort() throws Exception {

    }

    @Test
    public void testInsertSort() throws Exception {

    }


    @Test
    public void testSwap() throws Exception {

    }

}