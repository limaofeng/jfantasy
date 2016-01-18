package org.jfantasy.sms.service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class RandomWordGenerator {
    private char[] possiblesChars;
    private Random myRandom = new SecureRandom();

    public RandomWordGenerator(String acceptedChars) {
        this.possiblesChars = acceptedChars.toCharArray();
    }

    public String getWord(Integer length) {
        StringBuilder word = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            word.append(this.possiblesChars[this.myRandom.nextInt(this.possiblesChars.length)]);
        }

        return word.toString();
    }

    public String getWord(Integer length, Locale locale) {
        return this.getWord(length);
    }
}
