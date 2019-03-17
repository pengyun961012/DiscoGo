package com.disco.skeletalproduct;

import java.util.Date;

public class Profile {
    public String songName;
    public int songScore;
    public int songDurationMinute;
    public int songDurationSecond;
    public Date songTime;

    public Profile(String songName, int songScore, int songDurationMinute, int songDurationSecond, Date songTime){
        this.songName = songName;
        this.songScore = songScore;
        this.songDurationMinute = songDurationMinute;
        this.songDurationSecond = songDurationSecond;
        this.songTime = songTime;
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

    public Date getSongTime() {
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

    public void setSongTime(Date songTime) {
        this.songTime = songTime;
    }
}
