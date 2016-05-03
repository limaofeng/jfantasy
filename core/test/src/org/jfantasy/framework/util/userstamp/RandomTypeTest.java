package org.jfantasy.framework.util.userstamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Random;


public class RandomTypeTest {

    private final static Log LOGGER = LogFactory.getLog(RandomType.class);

    @Test
    public void randomType() {
        int[][] test = new int[31][10];
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 10; j++) {
                test[i][j] = -1;
            }
        }
        Random r = new Random();

        for (int i = 0; i < 31; i++) {
            int t = 0;
            while (t < 12) {
                int n = r.nextInt(10);
                if (test[i][n] == -1) {
                    if ((t == 2) || (t == 6)){
                        t++;
                    }
                    test[i][n] = (t++);
                }
            }
        }
        for (int i = 0; i < 31; i++) {
            LOGGER.debug("{");
            for (int j = 0; j < 10; j++) {
                LOGGER.debug(test[i][j] + ", ");
            }
            LOGGER.debug("}, ");
        }
    }

}