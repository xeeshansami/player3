package com.play.view.player.videoplayer4k.CursorUtils;

import android.content.Context;

import com.play.view.player.videoplayer4k.Model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryUtility {
    private static final String HISTORY = "history";

    public static void addItem(Context context, Video video) {
        JSONArray data = new JSONArray();
        JSONObject item = video.toJSONObject();
        List<Video> history = getHistory(context);
        if (history == null || history.size() == 0) {
            data.put(item);
        } else if (!history.contains(video)) {
            if (history.size() < 10) {
                history.add(0, video);
            } else {
                history.remove(9);
                history.add(0, video);
            }
            for (Video v : history) {
                data.put(v.toJSONObject());
            }
        } else {
            return;
        }
        writeJSONArrayToFile(context, data);
    }

    public static void removeItemAt(Context context, int index) {
        JSONArray data = new JSONArray();
        List<Video> history = getHistory(context);
        if (history != null && history.size() >= index + 1) {
            history.remove(index);
            for (Video v : history) {
                data.put(v.toJSONObject());
            }
            writeJSONArrayToFile(context, data);
        }
    }

    private static void writeJSONArrayToFile(Context context, JSONArray data) {
        try {
            FileOutputStream outputStream = context.openFileOutput("history", 0);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Video> getHistory(Context context) {
        Exception e;
        List<Video> history = new ArrayList();
        try {
            FileInputStream inputStream = context.openFileInput("history");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            StringBuffer buffer = new StringBuffer();
            while (bufferedInputStream.available() != 0) {
                buffer.append((char) bufferedInputStream.read());
            }
            inputStream.close();
            bufferedInputStream.close();
            JSONArray data = new JSONArray(buffer.toString());
            if (data.length() <= 0) {
                return null;
            }
            for (int i = 0; i < data.length(); i++) {
                Video video = new Video();
                video.set_ID((long) data.getJSONObject(i).getInt("_id"));
                video.setName(data.getJSONObject(i).getString("_display_name"));
                video.setTitle(data.getJSONObject(i).getString("title"));
                video.setDateAdded(data.getJSONObject(i).getString("date_added"));
                video.setDuration(data.getJSONObject(i).getString("duration"));
                video.setResolution(data.getJSONObject(i).getString("resolution"));
                video.setSize(data.getJSONObject(i).getDouble("_size"));
                video.setSizeReadable(data.getJSONObject(i).getString(Video.SIZE_READABLE));
                video.setData(data.getJSONObject(i).getString("_data"));
                video.setMime(data.getJSONObject(i).getString("mime_type"));
                history.add(video);
            }
            return history;
        } catch (FileNotFoundException e2) {
            return null;
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return history;
        } catch (JSONException e4) {
            e = e4;
            e.printStackTrace();
            return history;
        }
    }
}
