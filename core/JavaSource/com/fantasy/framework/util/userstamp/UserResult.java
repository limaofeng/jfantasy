package com.fantasy.framework.util.userstamp;

public class UserResult {
    private int userId;
    private String passwordHash;
    private int cssStyle;
    public static final int GUEST = 0;
    public static final int MEMBER = 1;
    public static final int COMPANY = 2;
    private int userType;
    private int randomType;

    public String getMemKey() {
        return new StringBuilder().append(isCompany() ? "com" : isGuest() ? "guest" : "").append(this.userId).toString();
    }

    public boolean isGuest() {
        return this.userType == 0;
    }

    public boolean isMember() {
        return this.userType == 1;
    }

    public boolean isCompany() {
        return this.userType == 2;
    }

    public boolean checkPassword(String password) {
        String pwStamp = String.valueOf(Encoder.hashPassword(password));
        return pwStamp.equals(this.passwordHash);
    }

    public int getUserId() {
        return this.userId;
    }

    protected void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getCssStyle() {
        return this.cssStyle;
    }

    protected void setCssStyle(int cssStyle) {
        this.cssStyle = cssStyle;
    }

    public int getRandomType() {
        return this.randomType;
    }

    public void setRandomType(int randomType) {
        this.randomType = randomType;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}