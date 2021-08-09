package com.play.view.player.videoplayer4k;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.play.view.player.videoplayer4k.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class CustomListAdapter extends BaseAdapter implements Filterable {
    public static ArrayList<VideoSongs> listData;
    private LayoutInflater layoutInflater;
    SharedPreferences sharedPreferences;
    MediaPlayer mp;
    private SharedPreferences mSharedPrefs = null;
    String path = "";
    boolean multiple = false;
    public static ArrayList<VideoSongs> mFilteredList = new ArrayList<VideoSongs>();
    MediaMetadataRetriever retriever;

    int firsttime = 0;

    int firsttime1 = 0;

    int counterone = 0;

    static int onevideo;
    Map<Integer, Integer> adsShows = new HashMap<Integer, Integer>();
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

    private AdRequest adRequest;
    Context context;

    public CustomListAdapter(Context context, ArrayList<VideoSongs> listData, String from) {


        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        firsttime = 0;
        counterone = 0;
        firsttime1 = 0;

        //  AppClass.firsttime=0;

        //  MobileAds.initialize(context, context.getResources().getString(R.string.banner_ad_unit_id));
        adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();

        adsShows.clear();


        if (!from.equalsIgnoreCase("search")) {

            this.listData = listData;
            this.mFilteredList = listData;

            for (int k = 0; k < listData.size(); k++) {
                if (k % 9 == 0) {

                    if (k != 0) {
                        adsShows.put(k, 0);
                    }
                }
            }
        }

        if (isOnline()) {
            VideoFolder.mAdView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public VideoSongs getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        // String name=getItem(position).getName();


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_video_list, null);
            holder = new ViewHolder();

            /*

            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            */

            holder.title = (TextView) convertView.findViewById(R.id.last_text);
            holder.duration = (TextView) convertView.findViewById(R.id.last_text_time);
            holder.sizeofVideo = (TextView) convertView.findViewById(R.id.sizeOfSons);

            holder.proimage = (ImageView) convertView.findViewById(R.id.past_icon);

            holder.mAdView = (AdView) convertView.findViewById(R.id.adView_banner);

            //   holder.mAdView1 = (AdView) convertView.findViewById(R.id.adView_banner1);

            holder.lLayout = (RelativeLayout) convertView.findViewById(R.id.advertie_layout);

            // holder.lLayout1 = (RelativeLayout) convertView.findViewById(R.id.advertie_layout1);

            holder.cardView = (RelativeLayout) convertView.findViewById(R.id.mainlay);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }







        /*


        try {



           // if(firsttime==0)
           // {

                 if(position==0 )
                {


                    System.out.println("position of view if " + position + " " + firsttime);


                    if(firsttime==0)
                    {




                        AppClass.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.mAdView.loadAd(adRequest);

                                holder.mAdView.setAdListener(new AdListener() {

                                    @Override
                                    public void onAdLoaded() {


                                        System.out.println("Add loaded " + position + " " + firsttime);

                                        if (firsttime == 0) {

                                            holder.lLayout.setVisibility(View.VISIBLE);
                                        }


                                        firsttime = 1;

                                    }
                                    // Implement AdListener
                                });

                            }
                        });



                        try {
                            loadItem(holder, position, convertView);
                        } catch (Exception d) {

                        }
                    }
                }









               // holder.cardView.setVisibility(View.GONE);
              //  holder.lLayout.setVisibility(View.VISIBLE);
          //  }


/*

            else if (adsShows.get(position) == 0  ) {





                holder.mAdView.loadAd(adRequest);


                holder.mAdView.setAdListener(new AdListener() {

                    @Override
                    public void onAdLoaded() {

                        System.out.println("Add loaded");


                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {



                              //  convertView.setVisibility(View.VISIBLE);





                                holder.lLayout.setVisibility(View.VISIBLE);


                                // holder.lLayout.setLayoutParams(layout_description);


                                //	holder.lLayout.setVisibility(View.VISIBLE);
                            }
                        });


                        // advertise.getLayoutParams().width= RelativeLayout.LayoutParams.MATCH_PARENT;
                        //advertise.getLayoutParams().height= 60;


                    }
                    // Implement AdListener
                });



                try {
                    loadItem(holder, position, convertView);
                }
                catch (Exception d)
                {

                }
                // holder.cardView.setVisibility(View.GONE);
            }
            */

/*

            else
            {





                System.out.println("position of view else " + position + " " + firsttime);

                if(position!=0 )
                {




                    try {
                        loadItem(holder, position, convertView);
                    } catch (Exception d) {

                    }




                }


            }

        }
        catch (Exception f)
        {

        }

        */


        try {

            if (adsShows.get(position) == 0) {



                   /*
                   holder.mAdView.setAdListener(new AdListener() {

                       @Override
                       public void onAdLoaded() {



                           System.out.println("adsShows call  onAdLoaded  "+position+" "+firsttime1);



                           if (firsttime1 == 0)
                           {

                               holder.lLayout.setVisibility(View.VISIBLE);
                               firsttime1 = 1;
                           }






                       }

                       @Override
                       public void onAdFailedToLoad(int errorCode) {


                       }
                       // Implement AdListener
                   });

                   */

                if (firsttime1 == 0) {

                    holder.mAdView.loadAd(adRequest);

                    firsttime1 = 1;

                    holder.mAdView.setAdListener(new AdListener() {

                        @Override
                        public void onAdLoaded() {


                            System.out.println("adsShows call  onAdLoaded  " + position + " " + firsttime1);


                            holder.lLayout.setVisibility(View.VISIBLE);


                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {


                            System.out.println("adsShows call  onAdLoaded error  " + position + " " + firsttime1 + " " + errorCode);
                        }
                        // Implement AdListener
                    });


                }

                System.out.println("adsShows call   " + position + " " + adsShows.get(position) + " " + firsttime1);
            }

        } catch (Exception f) {

            if (position == 7 && firsttime1 == 1) {
                holder.mAdView.setVisibility(View.GONE);
            }
            System.out.println("adsShows call  error  " + position + " " + firsttime1);

        }


        if (position == 0) {
            System.out.println("adsShows call firsttime1 && counterone > 8 " + counterone + " " + firsttime1);


            if (counterone > 7) {
                holder.mAdView.setVisibility(View.GONE);

                //    holder.mAdView.setVisibility(View.VISIBLE);


                if (isOnline()) {
                    VideoFolder.mAdView.setVisibility(View.VISIBLE);

                    try {

                        VideoSearchActivity.mAdView.setVisibility(View.VISIBLE);
                    } catch (Exception f) {

                    }
                }
            }

        } else {

            if (position < 8) {
                holder.mAdView.setVisibility(View.GONE);
            } else {
                holder.mAdView.setVisibility(View.VISIBLE);
            }

            if (position > 8) {

                System.out.println("adsShows call else position>8");


                if (isOnline()) {
                    VideoFolder.mAdView.setVisibility(View.GONE);

                    try {

                        VideoSearchActivity.mAdView.setVisibility(View.GONE);
                    } catch (Exception f) {

                    }
                }
                //


            }

        }


        try {




            /*

                 if(position==0 )
                {





                    System.out.println("postion call if 0 "+position+" "+counterone+" "+firsttime);


                    if(firsttime==0)
                    {





                                holder.mAdView.loadAd(adRequest);

                                holder.mAdView.setAdListener(new AdListener() {

                                    @Override
                                    public void onAdLoaded() {




                                        if (firsttime == 0) {

                                            holder.lLayout.setVisibility(View.VISIBLE);
                                        }




                                        firsttime = 1;

                                    }

                                    @Override
                                    public void onAdFailedToLoad(int errorCode) {


                                    }
                                    // Implement AdListener
                                });




                        try {
                            loadItem(holder, position, convertView);
                        } catch (Exception d) {

                        }
                    }

                    //if(firsttime==1 && counterone > 8)
                    //{
                    //    holder.mAdView.setVisibility(View.VISIBLE);
                    //    VideoFolder.mAdView.setVisibility(View.VISIBLE);
                   // }

                }

*/


            // holder.cardView.setVisibility(View.GONE);
            //  holder.lLayout.setVisibility(View.VISIBLE);
            //  }


/*

            else if (adsShows.get(position) == 0  ) {





                holder.mAdView.loadAd(adRequest);


                holder.mAdView.setAdListener(new AdListener() {

                    @Override
                    public void onAdLoaded() {

                        System.out.println("Add loaded");


                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {



                              //  convertView.setVisibility(View.VISIBLE);





                                holder.lLayout.setVisibility(View.VISIBLE);


                                // holder.lLayout.setLayoutParams(layout_description);


                                //	holder.lLayout.setVisibility(View.VISIBLE);
                            }
                        });


                        // advertise.getLayoutParams().width= RelativeLayout.LayoutParams.MATCH_PARENT;
                        //advertise.getLayoutParams().height= 60;


                    }
                    // Implement AdListener
                });



                try {
                    loadItem(holder, position, convertView);
                }
                catch (Exception d)
                {

                }
                // holder.cardView.setVisibility(View.GONE);
            }
            */


/*

            else
            {







                if(position!=0 )
                {



                    System.out.println("postion call else  "+position+" "+counterone+" "+firsttime);



                   // if(counterone==9)
                   // {
                     //   holder.mAdView.setVisibility(View.GONE);
                   // }


                   // if(position>8)
                   // {


                     //   VideoFolder.mAdView.setVisibility(View.GONE);
                      //


                   // }

                    try {
                        loadItem(holder, position, convertView);
                    } catch (Exception d) {

                    }

                }




            }

            */


            try {
                loadItem(holder, position, convertView);
            } catch (Exception d) {

            }


        } catch (Exception f) {


        }


        //getFiles(newsItem.getData(),holder);

       /* holder.headlineView.setText(newsItem.getHeadline());
        holder.reporterNameView.setText("By, " + newsItem.getReporterName());
        holder.reportedDateView.setText(newsItem.getDate());

        if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
        }

*/
        return convertView;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected() && wifiNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected() && mobileNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) {
            return true;
        }

        return false;
    }


    public void loadItem(final ViewHolder holder, final int position, View convertView) {

        final VideoSongs newsItem = mFilteredList.get(position);


        holder.title.setText(newsItem.getName());
        holder.sizeofVideo.setText(newsItem.getSize());

        if (position > 9) {
            counterone = position;
        }


        // holder.proimage.setImageBitmap(bMap);

        // holder.duration.setText(milliSecondsToTimer(getVideoDuration(newsItem.getData())));


        String path = Environment.getExternalStorageDirectory().getPath() + "/.thumbs/";

        path = path + newsItem.getName() + ".png";


        try {
                Picasso.get().load("file://" + path).fit().placeholder(R.drawable.head).error(R.drawable.head).centerInside().into(holder.proimage);

//            Glide.with(context)
//                    .load("file://" + path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
//
//                    .into(holder.proimage);

        } catch (Exception fddd) {

        }


        new Handler().post(new Runnable() {
            @Override
            public void run() {


                try {
                    holder.duration.setText(milliSecondsToTimer(getVideoDuration(newsItem.getData())));
                } catch (Exception f) {

                }

            }
        });


        // System.out.println( "time of view "+ getVideoDuration(newsItem.getDuration()));

        holder.cardView.setVisibility(View.VISIBLE);
        //holder.lLayout.setVisibility(View.GONE);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPrefs = context.getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                onevideo = mSharedPrefs.getInt("oneattime", 0);

                try {
                    if (multiple == false) {
                        if (onevideo == 0) {
                            int id = position;
                            Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                            intent.putExtra("videofilename", mFilteredList.get(position).getData());
                            intent.putExtra("title", mFilteredList.get(position).getName());
                            intent.putExtra("id", id);
                            intent.putExtra("mylistVideo", mFilteredList);
                            intent.putExtra("Showbuttons", false);
                            intent.putExtra("ShowNoti", true);
                            System.out.println("Song id " + id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    }
                } catch (Exception f) {

                }

            }
        });


    }

    static class ViewHolder {
      /*
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
        ImageView imageView;
        */

        TextView title;
        TextView duration;
        TextView artist, id, sizeofVideo;
        RelativeLayout lLayout;
        LinearLayout main_content;
        RelativeLayout cardView;
        AdView mAdView;
        ImageView proimage;
    }


    public long getVideoDuration(String path) {

        retriever = new MediaMetadataRetriever();
        long timeInMillisec = 0;
//use one of overloaded setDataSource() functions to set your data source

        try {
            retriever.setDataSource(context, Uri.fromFile(new File(path)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            timeInMillisec = Long.parseLong(time);

            retriever.release();

        } catch (Exception f) {

        }


        return timeInMillisec;
        /*

        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
        int duration = mp.getDuration();
        mp.release();

        System.out.println("duration of view "+duration);

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );


*/


    }

    /*public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }*/

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        if (milliseconds == 0) {
            return finalTimerString;

        } else {
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
        }

// return timer string
        return finalTimerString;
    }


    private void getFiles(String songsName, final ViewHolder holder) {

//        System.out.println("MAPing "+dirPath + "=media="+ MediaStore.Video.Media.EXTERNAL_CONTENT_URI );


        String[] selectionArgs = {"%" + songsName + "%"};
//        String[] selectionArgs=new String[]{"%Swag-Se-Swagat-Song--Tiger-Zinda-Hai--Salman-Khan--Katrina-Kaif.mp4%"};

        //  Log.i("Files", "Video files" + Arrays.toString(selectionArgs));
        Cursor videoCursorActivity = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        int totalvideoscount = videoCursorActivity.getCount();
//        folders_list.clear();
        while (videoCursorActivity.moveToNext()) {
            String filename = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String title = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            final String dura = videoCursorActivity.getString(

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
            final Uri albumArtUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
           /*
            System.out.println("albumUri "+ albumArtUri+" "+songsName +" "+milliSecondsToTimer(Long.parseLong(dura))+" "+getFileSize(size));
            System.out.println("Total SOngs" + totalvideoscount);
            System.out.println("Title" + title);
*/


            AppClass.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    holder.duration.setText(milliSecondsToTimer(Long.parseLong(dura)));


                    Picasso.get().load(albumArtUri.toString()).resize(150, 150).noFade().placeholder(R.drawable.logo).error(R.drawable.logo).centerInside().into(holder.proimage);
//                    Glide.with(context)
//                            .load(albumArtUri.toString()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
//                            .into(holder.proimage);
                }
            });

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


    @Override
    public Filter getFilter() {


        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                // mFilteredList = (ArrayList<VideoSongs>) results.values;


                if (constraint.equals("")) {

                    mFilteredList = listData;


                } else {
                    mFilteredList = (ArrayList<VideoSongs>) results.values;
                }


                //  notifyDataSetChanged();

                // System.out.println("onTextChanged 2 "+constraint.toString()+" "+results.count+" "+ mFilteredList.size());



                /*
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        notifyDataSetChanged();

                    }
                });
                */


            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<VideoSongs> FilteredArrList = new ArrayList<VideoSongs>();

                if (listData == null) {
                    listData = new ArrayList<VideoSongs>(mFilteredList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = listData.size();
                    results.values = listData;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < listData.size(); i++) {
                        String data = listData.get(i).name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new VideoSongs(listData.get(i).data, listData.get(i).image, listData.get(i).name, listData.get(i).duration, listData.get(i).artist, listData.get(i).album, listData.get(i).size, listData.get(i).resol));

                            //    new  VideoSongs(String data ,String image, String name, String duration, String artist,String album,String size,String resol)
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }




        /*
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = listData;
                } else {

                    ArrayList<VideoSongs> filteredList = new ArrayList<>();

                    for (VideoSongs androidVersion : listData) {

                        if (androidVersion.getName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);


                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<VideoSongs>) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }

   */


    private class BigComputationTask3 extends AsyncTask<Void, Void, Void> {

        List<String> list2 = null;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            // Runs on the background thread
            //doBigComputation();


            try {


            } catch (Exception f) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void res) {

        }

    }


}
