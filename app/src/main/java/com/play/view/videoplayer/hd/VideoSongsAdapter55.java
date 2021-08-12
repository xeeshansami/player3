package com.play.view.videoplayer.hd;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
//import android.widget.RelativeLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by azhar on 10/11/2017.
 */

public class VideoSongsAdapter55 extends RecyclerView.Adapter<VideoSongsAdapter55.myView> {
    private final boolean clear_cache;
    private LayoutInflater inflater;
    private Context context;
    VideoSongs songsClass;
    ArrayList<VideoSongs> list;
    static String nameofSOngs;
    static Bitmap sourceofImage;
    static int pos;
    File file;
    private int lastPosition = -1;
    boolean multiple = false;
    RelativeLayout advertise;
    ArrayList<Integer> strings;
    String adding = "false";
    String insert_query, select_query, delete_query;
    Cursor c;
    //DataBaseManager db;
    String root;
    SharedPreferences sharedPreferences;
    static int onevideo;
    private SharedPreferences mSharedPrefs = null;
    View v;
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    private AdView mAdView;
    private AdRequest adRequest, ar;
    RelativeLayout relaitive;


    //
    public VideoSongsAdapter55(Context context, ArrayList<VideoSongs> arraylist) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);



    }

    @Override
    public myView onCreateViewHolder(ViewGroup parent, int viewType) {



        v = null;
        RecyclerView.ViewHolder vh=null;




        v = inflater.inflate(R.layout.list_item_video_list, parent, false);

        MobileAds.initialize(v.getContext(), v.getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) v.findViewById(R.id.adView_banner);

        advertise = (RelativeLayout) v.findViewById(R.id.advertie_layout);
        relaitive = (RelativeLayout) v.findViewById(R.id.mainlay);

        adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
/*
        try {
            if (this.list.size() % 8 == 0) {

                v = inflater.inflate(R.layout.adview_item, parent, false);
                MobileAds.initialize(v.getContext(), v.getResources().getString(R.string.banner_ad_unit_id));
                mAdView = (AdView) v.findViewById(R.id.adView_banner);
                advertise = (RelativeLayout) v.findViewById(R.id.advertie_layout);
                adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                        .build();
                //vh=new AdviewHolder(v);


            } else {
                v = inflater.inflate(R.layout.list_item_video_list, parent, false);
                //vh = new ContentViewHolder(v);
            }


        }
        catch (Exception g)
        {
            v = inflater.inflate(R.layout.list_item_video_list, parent, false);
        }
        */

        strings = new ArrayList<>();

        /*
        db = new DataBaseManager(v.getContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        root = Environment.getExternalStorageDirectory().toString();
        return new myView(v);

    }


    @Override
    public void onBindViewHolder(final myView holder, final int position) {

       try {
           Animation animation = AnimationUtils.loadAnimation(context,
                   (position > lastPosition) ? R.anim.up_from_bottom
                           : R.anim.down_from_top);
           holder.itemView.startAnimation(animation);
           lastPosition = position;
           System.out.println("MainActivity Switch State "+  position % 8 );


            if (position % 8 == 0)
           {



               mAdView.loadAd(adRequest);





               mAdView.setAdListener(new AdListener() {

                   @Override
                   public void onAdLoaded() {

                       System.out.println("Add loaded");

                       new Handler().post(new Runnable() {
                           @Override
                           public void run() {

                           holder.itemView.getLayoutParams().height=180;
                           holder.itemView.getLayoutParams().width= SplashScreen.width;
                           holder.itemView.requestLayout();
                               advertise.setVisibility(View.VISIBLE);
                               relaitive.setVisibility(View.GONE);




                              // holder.lLayout.setLayoutParams(layout_description);


                               //    advertise.setVisibility(View.VISIBLE);
                           }
                       });


                       // advertise.getLayoutParams().width= RelativeLayout.LayoutParams.MATCH_PARENT;
                       //advertise.getLayoutParams().height= 60;


                   }
                   // Implement AdListener
               });


           } else {



               holder.bind(position);

               //


                advertise.setVisibility(View.GONE);
                relaitive.setVisibility(View.VISIBLE);


               songsClass = list.get(position);

               System.out.println("holder values "+songsClass.getName()+" "+songsClass.getData()+" "+songsClass.getSize()+" "+songsClass.getDuration());

               holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
               holder.sizeofVideo.setText(songsClass.getSize());
             //  if (clear_cache) {

           /*
           } else {
                   holder.proimage.setImageResource(R.drawable.head);
               }
               */
               //        holder.title.setText(songs.get(position).get("displayname"));
//        holder.artist.setText(songs.get(position).get("artist"));

               getFiles(songsClass.data,holder);




//        holder.title.setText(songs.get(position).get("title"));
//        String cove = songs.get(position).get("albumart");
//        Bitmap art = BitmapFactory.decodeFile(cove);
//        holder.proimage.setImageBitmap(art);
         Picasso.get().load(String.valueOf(songsClass.getImage()))
                .placeholder(R.drawable.video_icon).into(holder.proimage);
//        com.bumptech.glide.Glide.with(context).load(songsClass.getImage()).placeholder(android.R.drawable.list_selector_background).into(holder.proimage);
//        holder.proimage.setImageResource(songs.get(position).get(""));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                   Log.i("","onCLick" + position);
//                   Log.i("Name",list.get(position).getName());
//                    pos = position;
//                   nameofSOngs = list.get(position).getName();
//                   sourceofImage = list.get(position).getImage();
////                holder.title.setText(list.get(position).getName());
////                holder.proimage.setImageBitmap(list.get(position).getImage());
////                Context context = view.getContext();
////                Intent intent = new Intent(context, AudioDetail.class);
////
////                intent.putExtra("name",list.get(position).getName());
////                intent.putExtra("imageId",list.get(position).getImage());
////                context.startActivity(intent);
//            }
//        });
           }
       }
       catch (Exception s)
       {

       }

        holder.setItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(View view, int itemPos) {

                /*
                Log.i("", "onCLick" + itemPos);
                Log.i("NAME", list.get(itemPos).getName());
                Log.i("DATA", list.get(itemPos).getData());
                Log.i("DURATION", list.get(itemPos).getDuration());
                Log.i("DURATION", list.toString());
                */
                int mark = itemPos;

              // String i = list.get(itemPos).getData();
                //songsClass.setSelected(!songsClass.isSelected());


                //view.setBackgroundColor(songsClass.isSelected() ? Color.RED : Color.RED);
//                for (int c = 0; c < strings.size(); c++) {
//                    if (strings.get(c) == (itemPos)) {
//                        view.setBackgroundColor(songsClass.isSelected() ? Color.DKGRAY : Color.BLACK);
//                        strings.remove(strings.get(c));
//                        adding = "true";
//                        delete_query = "DELETE FROM videopath WHERE path='" + i + "'";
//                        Log.e("delete query", delete_query);
//                        db.delete(delete_query);
//                    } else {
//                        view.setBackgroundColor(songsClass.isSelected() ? Color.WHITE : Color.WHITE);
//                    }
//                }
//                if (adding.equalsIgnoreCase("false")) {
//                    strings.add(mark);
//                    insert_query = "Insert into videopath(path) values('" + i + "')";
//                    Log.e("Insert query", insert_query);
//                    db.insert_update(insert_query);
//                }
                mSharedPrefs = view.getContext().getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                onevideo = mSharedPrefs.getInt("oneattime", 0);

                if (multiple == false) {
                    if (onevideo == 0) {
                        int id = itemPos;
                        Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                        intent.putExtra("videofilename", list.get(itemPos).getData());
                        intent.putExtra("title", list.get(itemPos).getName());
                        intent.putExtra("id", id);
                        intent.putExtra("mylistVideo", list);
                        intent.putExtra("Showbuttons", false);
                        intent.putExtra("ShowNoti", true);
                        System.out.println("Song id " + id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();

    }
    @Override
    public int getItemViewType(int position) {


        if(list.get(position)==null)
            return AD_TYPE;
        return CONTENT_TYPE;
    }


    public class myView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, duration, artist, id, sizeofVideo;
        RelativeLayout lLayout;
        String insert_query, select_query, delete_query;
        Cursor c;
        //DataBaseManager db;


        //        TextView proname;
        ImageView proimage;
        onItemClickListener itemClickListener;

        public myView(final View itemView) {
            super(itemView);

            final ContentResolver contentResolver = itemView.getContext().getContentResolver();

            /*
            db = new DataBaseManager(itemView.getContext());

            try {
                db.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

            */

            title = (TextView) itemView.findViewById(R.id.last_text);
            duration = (TextView) itemView.findViewById(R.id.last_text_time);
            sizeofVideo = (TextView) itemView.findViewById(R.id.sizeOfSons);

            proimage = (ImageView) itemView.findViewById(R.id.past_icon);

           try {
               lLayout = (RelativeLayout) itemView.findViewById(R.id.advertie_layout);
           }
           catch (Exception d)
           {

           }


            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            title.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getLayoutPosition());
        }


        public void setItemClickListener(onItemClickListener ic) {
            this.itemClickListener = ic;
        }

        void sharevideo(String path) {
            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "view/mp4");
            content.put(MediaStore.Video.Media.DATA, path);

            ContentResolver resolver = itemView.getContext().getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("view/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey this is the view subject");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey this is the view text");
            sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);
            itemView.getContext().startActivity(Intent.createChooser(sharingIntent,"Share Video"));
        }

        public void shareVideoWhatsApp(String v) {
            File file = new File(v);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM,uri);
            itemView.getContext().startActivity(videoshare);
        }

    }

    @Override
    public void onViewDetachedFromWindow(myView holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class MediaFileFunctions {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public boolean deleteViaContentProvider(Context context, String fullname) {
            Uri uri = getFileUri(context, fullname);

            if (uri == null) {
                return false;
            }

            try {
                ContentResolver resolver = context.getContentResolver();

                // change type to image, otherwise nothing will be deleted
                ContentValues contentValues = new ContentValues();
                int media_type = 1;
                contentValues.put("media_type", media_type);
                resolver.update(uri, contentValues, null, null);

                return resolver.delete(uri, null, null) > 0;
            } catch (Throwable e) {
                return false;
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private Uri getFileUri(Context context, String fullname) {
            // Note: check outside this class whether the OS version is >= 11
            Uri uri = null;
            Cursor cursor = null;
            ContentResolver contentResolver = null;

            try {
                contentResolver = context.getContentResolver();
                if (contentResolver == null)
                    return null;

                uri = MediaStore.Files.getContentUri("external");
                String[] projection = new String[2];
                projection[0] = "_id";
                projection[1] = "_data";
                String selection = "_data = ? ";    // this avoids SQL injection
                String[] selectionParams = new String[1];
                selectionParams[0] = fullname;
                String sortOrder = "_id";
                cursor = contentResolver.query(uri, projection, selection, selectionParams, sortOrder);

                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0) // file present!
                        {
                            cursor.moveToFirst();
                            int dataColumn = cursor.getColumnIndex("_data");
                            String s = cursor.getString(dataColumn);
                            if (!s.equals(fullname))
                                return null;
                            int idColumn = cursor.getColumnIndex("_id");
                            long id = cursor.getLong(idColumn);
                            uri = MediaStore.Files.getContentUri("external", id);
                        } else // file isn't in the media database!
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("_data", fullname);
                            uri = MediaStore.Files.getContentUri("external");
                            uri = contentResolver.insert(uri, contentValues);
                        }
                    } catch (Throwable e) {
                        uri = null;
                    } finally {
                        cursor.close();
                    }
                }
            } catch (Throwable e) {
                uri = null;
            }
            return uri;
        }
    }



    private void getFiles(String songsName,myView holder) {
        String selection = MediaStore.Video.VideoColumns.DATA + " like?";
        String[] projection = {MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
           //     MediaStore.Video.VideoColumns.DISPLAY_NAME,
             //   MediaStore.Video.VideoColumns.ARTIST,
             //   MediaStore.Video.VideoColumns.RESOLUTION,
              //  MediaStore.Video.VideoColumns.ALBUM,
              //  MediaStore.Video.VideoColumns.DESCRIPTION,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE};

        String[] selectionArgs = {"%" + songsName + "%"};
//        String[] selectionArgs=new String[]{"%Swag-Se-Swagat-Song--Tiger-Zinda-Hai--Salman-Khan--Katrina-Kaif.mp4%"};

      //  Log.i("Files", "Video files" + Arrays.toString(selectionArgs));
       Cursor videoCursorActivity = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
//        System.out.println("MAPing "+dirPath + "=media="+ MediaStore.Video.Media.EXTERNAL_CONTENT_URI );

        int totalvideoscount = videoCursorActivity.getCount();
//        folders_list.clear();
        while (videoCursorActivity.moveToNext()) {
          String  filename = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
         String   title = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String dura = videoCursorActivity.getString(

                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
         /*
            String artist = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
           String album = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
            String desc = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
            String res = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                    */
            int size = videoCursorActivity.getInt(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            int videoId = videoCursorActivity.getInt(videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri albumArtUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
           /*
            System.out.println("albumUri "+ albumArtUri+" "+songsName +" "+milliSecondsToTimer(Long.parseLong(dura))+" "+getFileSize(size));
            System.out.println("Total SOngs" + totalvideoscount);
            System.out.println("Title" + title);
*/

            holder.duration.setText(milliSecondsToTimer(Long.parseLong(dura)));

            Picasso.get().load(albumArtUri.toString()).placeholder(R.drawable.logo).error(R.drawable.logo).fit().centerInside().into(holder.proimage);
//            Glide.with(context)
//                    .load(albumArtUri.toString()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
//                    .into(holder.proimage);
            /*
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            GlideApp.with(context)
                    .load(albumArtUri.toString()).placeholder(R.drawable.head).error(R.drawable.head)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.proimage);
*/

            /*
            insert_query = " Insert into diffvideos (filename,image,duration,title,album,artist,size,resol,position) values("+'"'+""+filename+""+'"'+","+'"'+"" +albumArtUri.toString()+ ""+'"'+","+'"'+"" +milliSecondsToTimer(Long.parseLong(dura))+ ""+'"'+","+'"'+"" +title+ ""+'"'+","+'"'+"" +abc+ ""+'"'+","+'"'+"" +artist+ ""+'"'+","+'"'+"" +getFileSize(size)+ ""+'"'+","+'"'+""+abc+""+'"'+","+'"'+""+pos+""+'"'+")";
            Log.e("Insert query", insert_query);
            db.insert_update(insert_query);
            */



        }
        videoCursorActivity.close();
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

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

// Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
// Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

// Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

// return timer string
        return finalTimerString;
    }


}
