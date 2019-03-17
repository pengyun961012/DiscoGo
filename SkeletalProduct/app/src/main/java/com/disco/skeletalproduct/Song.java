package com.disco.skeletalproduct;

public class Song {
    public int songImage;
    public String songName;

    public Song(String songName, int songImage) {
        this.songImage = songImage;
        this.songName = songName;
    }

    public int getSongImage() {
        return songImage;
    }

    public void setSongImage(int productImage) {
        this.songImage = productImage;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
