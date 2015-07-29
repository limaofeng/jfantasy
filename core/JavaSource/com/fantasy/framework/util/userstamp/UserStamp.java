package com.fantasy.framework.util.userstamp;

public class UserStamp {
    private int randomType;
    private String passwordHash;
    private String str;

    public String toString() {
        return this.str;
    }

    public int getRandomType() {
        return this.randomType;
    }

    protected void setRandomType(int randomType) {
        this.randomType = randomType;
    }

    protected void setStr(String str) {
        this.str = str;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}