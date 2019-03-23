package com.disco.skeletalproduct;

public class Friend {
    private String userName;
    private String songName;
    private String albumName;
    private int userImageUrl;

    public Friend(String userName, String songName, String albumName, int imageUrl) {
        this.userName = userName;
        this.songName = songName;
        this.albumName = albumName;
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

    public String getAlbumName() {
        return albumName;
    }

    public String getUserName() {
        return userName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setUserImageUrl(int userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
