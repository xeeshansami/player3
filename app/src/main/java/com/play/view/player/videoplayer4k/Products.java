package com.play.view.player.videoplayer4k;

/**
 * Created by azhar on 11/6/2017.
 */

public class Products {

      String total_count;
      String folders_name;


    public void Products(String folders_name,String total_count ){
        this.total_count = total_count;
        this.folders_name = folders_name;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setFolders_name(String folders_name) {
        this.folders_name = folders_name;
    }

    public String getFolders_name() {
        return folders_name;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
