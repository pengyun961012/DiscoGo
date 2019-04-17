package com.disco.skeletalproduct;

public class PendingFriend {
    private int userId;
    private int imageId;
    private int tokenNum;
    private String userName;

    public PendingFriend(int userId, int imageId, int tokenNum, String userName){
        this.imageId = imageId;
        this.userId = userId;
        this.tokenNum = tokenNum;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTokenNum() {
        return tokenNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTokenNum(int tokenNum) {
        this.tokenNum = tokenNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
