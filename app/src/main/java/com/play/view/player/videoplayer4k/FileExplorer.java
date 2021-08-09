package com.play.view.player.videoplayer4k;

import android.content.Context;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by ishop on 06/10/2018.
 */

public class FileExplorer implements FileFilter {

    protected static final String TAG = "AudioFileFilter";

    int index = 0;


    /**
     * allows Directories
     */
    private final boolean allowDirectories;

    static int k = 0;


    public FileExplorer(boolean allowDirectories) {
        this.allowDirectories = allowDirectories;


    }

    public FileExplorer(Context contex, int k) {


        this(true);

        this.k = k;


    }

    /*
    @Override
    public boolean accept(File f) {
        if ( f.isHidden() || !f.canRead() ) {
            return false;
        }




        if ( f.isDirectory() ) {
            return checkDirectory( f );
        }
        else {
            return checkFileExtension(f);
        }
    }
    */

    private boolean checkFileExtension(File f) {
        String ext = getFileExtension(f);

        if (ext == null) return false;
        try {
            if (SupportedFileFormat.valueOf(ext.toUpperCase()) != null) {

                try {


                    String folderName = f.getPath().substring(0, f.getPath().lastIndexOf("/"));

                    //  System.out.println("Folder found " + f.getPath().substring(0, f.getPath().lastIndexOf("/")) + " " + f.getName());

                    String folderName1 = folderName.substring(folderName.lastIndexOf("/") + 1, folderName.length());


                    System.out.println("if condition true " + folderName.length() + " " + folderName1.length());


//                    if (folderName1.startsWith(".") || folderName.contains("/Android/") || folderName1.equalsIgnoreCase("0") ) {
//
//
//                        System.out.println("if condition true ");
//
//                    }
//                    else
//                    {


                    if (this.k == 0) {

                        AppClass.dirList.put(folderName, folderName1);
                    } else {


                        try {


                            if (AppClass.dirList.get(folderName1).equalsIgnoreCase(folderName1)) {
                                AppClass.dirList.put(folderName, folderName1 + k);
                            } else {
                                AppClass.dirList.put(folderName, folderName1);
                            }

                            //AppClass.dirList.put(folderName, folderName1);

                        } catch (Exception ggf) {
                            AppClass.dirList.put(folderName, folderName1);
                        }


                    }
                    //   index++;

                    try {


                        if (!AppClass.mVideoPathCache.get(f.getPath()).equalsIgnoreCase(f.getPath())) {
                            synchronized (AppClass.mVideoPathCache) {
                                AppClass.mVideoPathCache.put(f.getPath(), folderName);
                            }

                            AppClass.subDirs.add(f.getPath());
                        }


                    } catch (Exception wwf) {
                        synchronized (AppClass.mVideoPathCache) {
                            AppClass.mVideoPathCache.put(f.getPath(), folderName);
                        }
                        AppClass.subDirs.add(f.getPath());
                        //   System.out.println("Folder found  afterass/ error " + wwf.getMessage());
                    }
//                    }


                } catch (Exception g) {

                }

                return true;
            }

        } catch (IllegalArgumentException e) {

            //   checkDirectory(f);
            //Not known enum value
            //    return false;
        }

        return false;
    }

    private boolean checkDirectory(File dir) {

        System.out.println("Folder found  after " + k);
        // try {
        if (!allowDirectories) {
            return false;
        } else {

            int songNumb = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    if (file.isFile()) {

                        if (file.getName().equals(".nomedia"))
                            return false;

                        return checkFileExtension(file);
                    } else if (file.isDirectory()) {


                        checkDirectory(file);


                        return false;
                    } else
                        return false;
                }
            }).length;

            if (songNumb > 0) {
                // System.out.println( "checkDirectory: dir " + dir.toString() + " return true con songNumb -> " + songNumb );
                return true;
            }


            return false;
        }
    /*
    }
    catch (Exception d)
    {

        System.out.println("Folder found  after error " +k+d.getMessage());
        return false;
    }
    */

    }


    public String getFileExtension(File f) {
        return getFileExtension(f.getName());
    }

    public String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else
            return null;
    }

    @Override
    public boolean accept(File f) {

        if (f.isHidden() || !f.canRead()) {
            return true;
        }

        if (f.isDirectory()) {
            return checkDirectory(f);
        } else {
            return checkFileExtension(f);
        }

    }

    /**
     * Files formats currently supported by Library
     */
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


