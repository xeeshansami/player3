package com.play.view.videoplayer.hd;

import java.io.Serializable;

/**
 * Created by DuskySolution on 11/16/2017.
 */

public class VideoSongs implements Serializable {


    int id;
    String image;
    String data;
    String name;
    String artist;
    String duration;
    String album;
    String size;
    String resol;
    private boolean isSelected = false;


    public VideoSongs()
    {

    }

    public   VideoSongs(String data ,String image, String name, String duration, String artist,String album,String size,String resol) {
        this.data = data;
        this.image = image;
        this.name = name;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.size = size;
        this.resol = resol;

    }
//    public void VideoSongs(Bitmap image, String name, String duration, String artist) {
//        this.image = image;
//        this.name = name;
//        this.duration = duration;
//        this.artist = artist;
//
//    }
//
//    public void VideoSongs(Bitmap image, String name, String duration) {
//        this.image = image;
//        this.name = name;
//        this.duration = duration;
//
//    }

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

    public String getImage() {
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

    public void setSize(String size) {
        this.size = size;
    }

    public void setResol(String resol) {
        this.resol = resol;
    }

    public String getSize() {
        return size;
    }

    public String getResol() {
        return resol;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
