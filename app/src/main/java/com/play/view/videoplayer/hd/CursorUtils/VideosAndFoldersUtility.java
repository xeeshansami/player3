package com.play.view.videoplayer.hd.CursorUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video.Media;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import android.util.Log;


import com.play.view.videoplayer.hd.Model.Folder;
import com.play.view.videoplayer.hd.Model.TheUtility;
import com.play.view.videoplayer.hd.Model.Video;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideosAndFoldersUtility {
    static final /* synthetic */ boolean $assertionsDisabled = (!VideosAndFoldersUtility.class.desiredAssertionStatus());
    private String[] VIDEO_COLUMNS = new String[]{"_id", "_display_name", "title", "date_added", "duration", "resolution", "_size", "_data", "mime_type"};
    private Context context;
    private List<Video> videos = new ArrayList();
    Cursor videoCursorActivity;
    private String filename;
    public static List<String> list2 = null;
    private String title;
    ArrayList<Video> videoActivitySongsList;

    public VideosAndFoldersUtility(Context context) {
        this.context = context;
    }

    public List<Video> fetchAllVideos() {
        Cursor videoCursor = this.context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, this.VIDEO_COLUMNS, null, null, "date_added DESC");
        if (videoCursor != null) {
            this.videos = getVideosFromCursor(videoCursor);
//            Log.i("cursorVideosSize", videoCursor.getString(videoCursor.getColumnIndex("size")));
            Log.i("cursorVideos", this.videos + "");
            videoCursor.close();
        }
        return this.videos;
    }

    @SuppressLint({"Recycle"})
    public List<Video> fetchVideosByFolder(String folder) {
        list2 = getSdCardPaths(context, true);
        Log.i("allvideos", list2 + "");
        Log.i("foldername", folder + "");
        videoActivitySongsList = new ArrayList<Video>();
        List<Video> folderVideos = new ArrayList();
        Video video;
        String[] selectionArgs = new String[]{folder + "%"};

        Cursor videoCursor = this.context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, this.VIDEO_COLUMNS, " mime_type = \"video/*\" OR _data Like ?", selectionArgs, "date_added DESC");
        if (videoCursor != null) {
            while (videoCursor.moveToNext()) {
                if (new File(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_data"))).getParent().equalsIgnoreCase(folder)) {
                    video = new Video();
                    video.set_ID(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_id"))));
                    video.setName(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_display_name")));
                    video.setTitle(videoCursor.getString(videoCursor.getColumnIndexOrThrow("title")));
                    video.setDateAdded(TheUtility.humanReadableDate(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("date_added"))) * 1000));
                    video.setDuration(TheUtility.parseTimeFromMilliseconds(videoCursor.getString(videoCursor.getColumnIndexOrThrow("duration"))));
                    video.setResolution(videoCursor.getString(videoCursor.getColumnIndexOrThrow("resolution")));
                   /* try {
                        video.setSize(Double.valueOf(videoCursor.getString(videoCursor.getColumnIndex(6))));
                    }catch (IllegalArgumentException e){
                        video.setSize(Double.valueOf(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_size"))));
                    }*/
//                    video.setSizeReadable(TheUtility.humanReadableByteCount(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_size"))), false));
                    video.setData(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_data")));
                    video.setMime(videoCursor.getString(videoCursor.getColumnIndexOrThrow("mime_type")));
                    folderVideos.add(video);
                }
            }

            return folderVideos;
        }

        throw new AssertionError();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static List<String> getSdCardPaths(final Context context, final boolean includePrimaryExternalStorage) {
        String storageState = null;
        final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs == null || externalCacheDirs.length == 0)
            return null;
        if (externalCacheDirs.length == 1) {
            if (externalCacheDirs[0] == null)
                return null;
            storageState = EnvironmentCompat.getStorageState(externalCacheDirs[0]);
            if (!Environment.MEDIA_MOUNTED.equals(storageState))
                return null;
            if (!includePrimaryExternalStorage && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Environment.isExternalStorageEmulated())
                return null;
        }
        final List<String> result = new ArrayList<>();
        if (includePrimaryExternalStorage || externalCacheDirs.length == 1)
            result.add(getRootOfInnerSdCardFolder(externalCacheDirs[0]));


        try {
            for (int i = 1; i < externalCacheDirs.length; ++i) {
                final File file = externalCacheDirs[i];
                if (file == null)
                    continue;
                storageState = EnvironmentCompat.getStorageState(file);
                if (Environment.MEDIA_MOUNTED.equals(storageState))
                    result.add(getRootOfInnerSdCardFolder(externalCacheDirs[i]));
            }
        } catch (Exception gg) {
            result.add("/storage/");
        }

        //
        //  System.out.println("sdcard "+Environment.getExternalStoragePublicDirectory());


        try {
            Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            if (isSDPresent) {
                if (externalCacheDirs.length == 1) {
                    result.add("/storage/");
                }
            }
        } catch (Exception ss) {

        }


        if (result.isEmpty())
            return null;
        return result;
    }

    /**
     * Given any file/folder inside an sd card, this will return the path of the sd card
     */
    private static String getRootOfInnerSdCardFolder(File file) {
        if (file == null)
            return null;
        final long totalSpace = file.getTotalSpace();
        while (true) {
            final File parentFile = file.getParentFile();
            if (parentFile == null || parentFile.getTotalSpace() != totalSpace)
                return file.getAbsolutePath();
            file = parentFile;
        }
    }

    private void getFiles(String songsName) {
        String selection = MediaStore.Video.VideoColumns.DATA + " like?";
        String[] projection = {MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.ARTIST,
                MediaStore.Video.VideoColumns.RESOLUTION,
                MediaStore.Video.VideoColumns.ALBUM,
                MediaStore.Video.VideoColumns.DESCRIPTION,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE};

        String[] selectionArgs = {"%" + songsName + "%"};
        //        String[] selectionArgs=new String[]{"%Swag-Se-Swagat-Song--Tiger-Zinda-Hai--Salman-Khan--Katrina-Kaif.mp4%"};

        Log.i("Files", "Video files" + Arrays.toString(selectionArgs));
        videoCursorActivity = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
//        System.out.println("MAPing "+dirPath + "=media="+ MediaStore.Video.Media.EXTERNAL_CONTENT_URI );

        int totalvideoscount = videoCursorActivity.getCount();
        Log.i("VideoCount123", "Video files" + totalvideoscount);
//        folders_list.clear();
        while (videoCursorActivity.moveToNext()) {
            filename = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            title = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String dura = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String artist = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String desc = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
            String res = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
            int size = videoCursorActivity.getInt(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            int videoId = videoCursorActivity.getInt(videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri albumArtUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videoId, MediaStore.Video.Thumbnails.MICRO_KIND, options);
//            Uri myuri  =      getImageUri(getApplicationContext(),curThumb);


//            System.out.println("albumUri " + albumArtUri + " " + songsName + " " + milliSecondsToTimer(Long.parseLong(dura)) + " " + getFileSize(size));
//            System.out.println("Total SOngs" + totalvideoscount);
//            System.out.println("Title" + title);


            Video songs = new Video();
            songs.setData(filename);
//            songs.setImage(myuri.toString());
//            songs.setDuration(milliSecondsToTimer(Long.parseLong(dura)));
            songs.setName(title);
//            songs.setArtist(artist);
//            songs.setSize(getFileSize(size));


            videoActivitySongsList.add(songs);

        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

   /* public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }*/

    public List<Video> getVideosFromCursor(Cursor videoCursor) {
        List<Video> videos = new ArrayList();
        while (videoCursor.moveToNext()) {
            Video video = new Video();
            video.set_ID(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_id"))));
            video.setName(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_display_name")));
            video.setTitle(videoCursor.getString(videoCursor.getColumnIndexOrThrow("title")));
            video.setDateAdded(TheUtility.humanReadableDate(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("date_added"))) * 1000));
            try {
                video.setDuration(TheUtility.parseTimeFromMilliseconds(videoCursor.getString(videoCursor.getColumnIndexOrThrow("duration"))));
            } catch (Exception ex) {
//                Mint.logException("VideosAndFoldersUtility Duration" + videoCursor.getColumnIndexOrThrow("duration"), "", ex);
            }
            video.setResolution(videoCursor.getString(videoCursor.getColumnIndexOrThrow("resolution")));
//            video.setSize(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_size"))));
//            video.setSizeReadable(TheUtility.humanReadableByteCount(Long.parseLong(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_size"))), false));
            video.setData(videoCursor.getString(videoCursor.getColumnIndexOrThrow("_data")));
            video.setMime(videoCursor.getString(videoCursor.getColumnIndexOrThrow("mime_type")));
            videos.add(video);
        }
        Log.i("getVideos", videos + "");
        return videos;
    }

    public void deleteVideos(List<Long> videos) {
        for (Long video : videos) {
            this.context.getContentResolver().delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, video.longValue()), null, null);
        }
    }

    public boolean deleteVideosByFolder(String folder) {
        File file = new File(folder);
        if (file.exists() && file.isDirectory()) {
            String[] selectionArgs = new String[]{folder + "%"};
            Cursor cursor = this.context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "_data Like ?", selectionArgs, "date_added DESC");
            if ($assertionsDisabled || cursor != null) {
                while (cursor.moveToNext()) {
                    if (new File(cursor.getString(cursor.getColumnIndexOrThrow("_data"))).getParent().equalsIgnoreCase(folder)) {
                        this.context.getContentResolver().delete(ContentUris.withAppendedId(Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("_id")))), null, null);
                    }
                }
            } else {
                throw new AssertionError();
            }
        }
        return file.delete();
    }

    public static ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4") ||
                        file.getName().endsWith(".gif")) {
                    if (!inFiles.contains(file)) inFiles.add(file);

                    if (!inFiles.contains(file)) inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public List<Folder> fetchAllFolders() {
        List<Folder> folderList = new ArrayList();
        for (int i = 0; i < this.videos.size(); i++) {
            try {
                String path = new File(videos.get(i).getData()).getParent();
                String videoFolderName = new File(path).getName();
                Folder mfolder = new Folder();
                mfolder.setName(videoFolderName);
                mfolder.setPath(path);
                mfolder.videosPP();
//            mfolder.sizePP(view.getSize());
                if (path.contains(mfolder.getName())) {
                    Log.i("Same", "  = " + path);
                }
                if (!folderList.contains(mfolder))
                    folderList.add(mfolder);
            } catch (NullPointerException e) {
                Log.i("NullPointerException", "  = " + e.getMessage());
            }
         /*   if (folderList.get(mfolder.).contains(mfolder)) {
                Log.i("checkSame", folderList + " = " + mfolder);
                ((Folder) folderList.get(folderList.indexOf(mfolder))).videosPP();
                ((Folder) folderList.get(folderList.indexOf(mfolder))).sizePP(view.getSize());
            } else {*/
//            }
        }
        Log.i("folderSizes", " = " + folderList.size());
        return folderList;
    }

    public void deleteFolders(List<String> folders) {
        for (String folder : folders) {
            deleteVideosByFolder(folder);
        }
    }

    public enum SupportedFileFormat {
        _3GP("3gp"),
        MP4("mp4"),
        MPG("mpg"),
        FLV("flv"),
        WEB("web"),
        MP41("MP4"),
        MKV("mkv"),
        M4A("m4a"),
        AVI("avi"),
        SVI("svi"),
        M4V("m4v"),
        MOV("mov"),
        ASF("asf"),
        SWF("swf"),
        RM("rm"),
        SND("snd"),
        MPEG("mpeg"),
        GPP("3gpp"),
        QT("qt"),
        M4P("m4p"),
        MXF("mxf"),
        M2V("m2v"),
        MGA("mga"),
        MP2("mp2"),
        MNG("mng"),
        M3U("m3u"),
        M4G("m4a"),
        G2("3g2"),
        WEBM("webm");


//        AAC("aac"),
//        TS("ts"),
//        FLAC("flac"),
//        MP3("mp3"),
//        MID("mid"),
//        XMF("xmf"),
//        MXMF("mxmf"),
//        RTTTL("rtttl"),
//        RTX("rtx"),
//        OTA("ota"),
//        IMY("imy"),
//        OGG("ogg"),
//        WAV("wav");


        private String filesuffix;

        SupportedFileFormat(String filesuffix) {
            this.filesuffix = filesuffix;
        }

        public String getFilesuffix() {
            return filesuffix;
        }
    }

}
