package org.jfantasy.framework.util.userstamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Random;

public class RandomType {

	private final static Log LOGGER = LogFactory.getLog(RandomType.class);
	private RandomType(){}
	public static final int[][] sequence = { { 10, 8, 7, 0, 1, 4, 11, 3, 9, 5 }, { 4, 0, 8, 5, 9, 1, 10, 11, 7, 3 }, { 0, 8, 10, 7, 5, 4, 1, 3, 11, 9 }, { 3, 1, 0, 4, 7, 5, 10, 9, 8, 11 }, { 10, 4, 5, 7, 11, 1, 9, 8, 3, 0 }, { 10, 1, 4, 7, 0, 8, 11, 9, 5, 3 }, { 10, 3, 5, 1, 0, 4, 11, 7, 9, 8 }, { 3, 1, 10, 8, 7, 9, 5, 0, 4, 11 }, { 7, 10, 3, 9, 5, 11, 4, 8, 1, 0 }, { 4, 8, 11, 9, 3, 5, 0, 7, 10, 1 }, { 7, 1, 0, 10, 9, 5, 3, 4, 11, 8 }, { 11, 4, 0, 10, 9, 1, 8, 5, 7, 3 }, { 4, 1, 5, 11, 9, 10, 7, 0, 8, 3 }, { 3, 7, 1, 9, 11, 8, 5, 4, 0, 10 }, { 8, 5, 1, 9, 0, 4, 11, 3, 10, 7 }, { 5, 3, 8, 1, 4, 0, 11, 9, 7, 10 }, { 0, 9, 4, 10, 5, 8, 1, 3, 7, 11 }, { 7, 3, 4, 11, 1, 8, 9, 10, 0, 5 }, { 7, 3, 4, 1, 5, 8, 11, 9, 0, 10 }, { 9, 7, 5, 10, 1, 11, 8, 0, 4, 3 },
			{ 9, 4, 1, 0, 10, 8, 3, 11, 5, 7 }, { 3, 10, 11, 8, 4, 1, 9, 5, 7, 0 }, { 8, 11, 9, 3, 4, 1, 7, 10, 5, 0 }, { 1, 10, 8, 5, 9, 3, 11, 7, 4, 0 }, { 9, 0, 10, 4, 11, 7, 1, 8, 5, 3 }, { 1, 9, 5, 4, 8, 3, 7, 0, 10, 11 }, { 4, 5, 3, 11, 0, 1, 8, 7, 10, 9 }, { 10, 1, 4, 7, 0, 11, 9, 8, 3, 5 }, { 11, 0, 4, 7, 3, 1, 10, 9, 5, 8 }, { 11, 3, 5, 0, 7, 1, 9, 10, 8, 4 }, { 5, 3, 1, 10, 9, 4, 8, 7, 0, 11 } };

	public static void main(String[] args) {
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
