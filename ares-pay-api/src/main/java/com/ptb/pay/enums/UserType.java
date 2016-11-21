package com.ptb.pay.enums;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public enum UserType {
    USER_IS_BUYER(1),
    USER_IS_SELLER(2);

    int userType;

    private UserType(int type){this.userType = type;}

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
