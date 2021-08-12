package com.play.view.videoplayer.hd.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {
    public static final String SIZE_READABLE = "size_readable";
    private long _ID;
    public String data;
    private String dateAdded;
    private String duration;
    private String mime;
    private String name;
    private String resolution;
    private Double size;
    private String sizeReadable;
    private String title;

    public List<Integer> getTotalvideos() {
        return totalvideos;
    }

    public void setTotalvideos(List<Integer> totalvideos) {
        this.totalvideos = totalvideos;
    }

    private List<Integer> totalvideos;

    public Video(long _ID, String name, String title, String dateAdded, String duration, String resolution, Double size, String data, List<Integer> totalvideos) {
        this._ID = _ID;
        this.name = name;
        this.title = title;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.resolution = resolution;
        this.size = size;
        this.data = data;
        this.totalvideos = totalvideos;
    }
    public Video()
    {

    }

    public long get_ID() {
        return this._ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Double getSize() {
        return this.size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getSizeReadable() {
        return this.sizeReadable;
    }

    public void setSizeReadable(String sizeReadable) {
        this.sizeReadable = sizeReadable;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMime() {
        return this.mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public boolean equals(Object obj) {
        if (obj == null || !Video.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        Video other = (Video) obj;
        if (this.data != null) {
            return this.data.equals(other.data);
        }
        if (other.data == null) {
            return true;
        }
        return false;
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("_id", get_ID());
            object.put("_display_name", getName());
            object.put("title", getTitle());
            object.put("date_added", getDateAdded());
            object.put("duration", getDuration());
            object.put("resolution", getResolution());
            object.put("_size", getSize());
            object.put(SIZE_READABLE, getSizeReadable());
            object.put("_data", getData());
            object.put("mime_type", getMime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
