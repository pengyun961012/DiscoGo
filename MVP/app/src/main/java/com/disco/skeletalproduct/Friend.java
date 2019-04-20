package com.disco.skeletalproduct;

public class Friend {
    private int userId;
    private String userName;
    private String songName;
    private int userImageUrl;
    private String songLink;

    public Friend(int userId, String userName, String songName, String songLink, int imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.songName = songName;
        this.songLink = songLink;
        this.userImageUrl = imageUrl;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }

    public int getUserImageUrl() {
        return userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserImageUrl(int userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
