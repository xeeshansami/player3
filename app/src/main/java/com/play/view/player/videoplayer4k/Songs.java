package com.play.view.player.videoplayer4k;

import java.io.Serializable;

/**
 * Created by azhar on 1/26/2018.
 */

public class Songs implements Serializable {

    private String data;
    private String album;
    int id;
    String image;


    String name;
    String artist;
    String duration;


    public  Songs(String image ,String name, String artist, String data, String album,String duration) {
        this.artist = artist;
        this.data = data;
        this.image = image;
        this.album = album;
        this.duration = duration;
        this.name = name;
    }



    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String  getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}