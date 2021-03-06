package com.disco.skeletalproduct;

import java.util.Date;

public class Profile {
    public String songName;
    public int songScore;
    public int songDurationMinute;
    public int songDurationSecond;
    public String songTime;
    public boolean isPlayed;

    public Profile(String songName, int songScore, int songDurationMinute, int songDurationSecond, String songTime){
        this.songName = songName;
        this.songScore = songScore;
        this.songDurationMinute = songDurationMinute;
        this.songDurationSecond = songDurationSecond;
        this.songTime = songTime;
        this.isPlayed = false;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSongScore() {
        return songScore;
    }

    public int getSongDurationMinute() {
        return songDurationMinute;
    }

    public int getSongDurationSecond() {
        return songDurationSecond;
    }

    public String getSongTime() {
        return songTime;
    }

    public void setSongDurationMinute(int songDurationMinute) {
        this.songDurationMinute = songDurationMinute;
    }

    public void setSongDurationSecond(int songDurationSecond) {
        this.songDurationSecond = songDurationSecond;
    }

    public void setSongScore(int songScore) {
        this.songScore = songScore;
    }

    public void setSongTime(String songTime) {
        this.songTime = songTime;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

}
