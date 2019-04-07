package com.disco.skeletalproduct;

public class Leaderboard {
    private int userProfileImageUrl;
    private String userName;
    private int rank;
    private int score;

    public Leaderboard(int image, String userName, int rank, int score){
        this.userProfileImageUrl = image;
        this.userName = userName;
        this.rank = rank;
        this.score = score;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public int getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUserProfileImageUrl(int userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }
}
