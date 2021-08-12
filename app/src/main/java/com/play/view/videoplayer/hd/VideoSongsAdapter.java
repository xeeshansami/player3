package com.play.view.videoplayer.hd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by azhar on 10/11/2017.
 */

public class VideoSongsAdapter extends RecyclerView.Adapter<VideoSongsAdapter.myView>   {
    private final boolean clear_cache;
    private LayoutInflater inflater;
    private Context context;
    VideoSongs songsClass;
    ArrayList<VideoSongs> list;
    static String nameofSOngs;
    static Bitmap sourceofImage;
    private AdView mAdView;
    private AdRequest adRequest, ar;
    static int pos;
    File file;
    private int lastPosition = -1;
    boolean multiple=false;
    ArrayList<Integer> strings;
    String adding="false";
    String insert_query,select_query,delete_query;
    Cursor c;
   // DataBaseManager db;
    String root;
    static int onevideo;
    private SharedPreferences mSharedPrefs = null;
    View v;
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;


    //
    public VideoSongsAdapter(Context context, ArrayList<VideoSongs> arraylist    ) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);
        System.out.println("MainActivity Switch State" + clear_cache);
    }

    @Override
    public myView onCreateViewHolder(ViewGroup parent, int viewType) {







         v = null;
        RecyclerView.ViewHolder vh=null;
        if (viewType == AD_TYPE)
        {

            v=inflater.inflate(R.layout.adview_item, parent, false);
            MobileAds.initialize(v.getContext(), v.getResources().getString(R.string.banner_ad_unit_id));
            mAdView = (AdView) v.findViewById(R.id.adView_banner);
            adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                    .build();
            //vh=new AdviewHolder(v);

        }
        else {
            v = inflater.inflate(R.layout.list_item_video_list, parent, false);
            //vh = new ContentViewHolder(v);
        }
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
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;



        if (getItemViewType(position) == AD_TYPE) {

            mAdView.loadAd(adRequest);


        } else {
            Log.d(getClass().getSimpleName(), "#" + position);
            holder.bind(position);
        songsClass = list.get(position);
        holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
        holder.sizeofVideo.setText(songsClass.getSize());

           //Picasso.with(context).load(String.valueOf(songsClass.getImage())).skipMemoryCache().placeholder(R.drawable.head).error(R.drawable.head).fit().centerInside().into(holder.proimage);


        //        holder.title.setText(songs.get(position).get("displayname"));
//        holder.artist.setText(songs.get(position).get("artist"));
        holder.duration.setText(songsClass.getDuration());
    }


        holder.setItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(View view, int itemPos) {
                Log.i("","onCLick" + itemPos);
                Log.i("NAME",list.get(itemPos).getName());
                Log.i("DATA",list.get(itemPos).getData());
                Log.i("DURATION",list.get(itemPos).getDuration());
                Log.i("DURATION",list.toString());
                int mark = itemPos;

                String i = list.get(itemPos).getData();


                mSharedPrefs = view.getContext().getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                onevideo = mSharedPrefs.getInt("oneattime", 0);
                if (onevideo == 0) {

                    for (int c = 0; c < strings.size(); c++) {
                        if (strings.get(c) == (itemPos)) {
                            view.setBackgroundColor(songsClass.isSelected() ? Color.WHITE : Color.WHITE);
                            strings.remove(strings.get(c));
                            adding = "true";
                            delete_query = "DELETE FROM videopath WHERE path='" + i + "'";
                            Log.e("delete query", delete_query);
                           // db.delete(delete_query);
                        }
                        else {
                            view.setBackgroundColor(songsClass.isSelected() ? Color.BLUE : Color.BLUE);
                        }
                    }
                    if (adding.equalsIgnoreCase("false")) {
                        strings.add(mark);
                        insert_query = "Insert into videopath(path) values("+'"'+""+i+""+'"'+")";
                        Log.e("Insert query", insert_query);
                        //db.insert_update(insert_query);
                    }

                    if (multiple == false) {

                        int id = itemPos;
                        Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                        intent.putExtra("videofilename", list.get(itemPos).getData());
                        intent.putExtra("title", list.get(itemPos).getName());
                        intent.putExtra("id", id);
                        intent.putExtra("mylistVideo", list);
                        intent.putExtra("Showbuttons", false);
                        intent.putExtra("ShowNoti", true);
                        System.out.println("Song id " + id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }

            }});
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


    public class myView extends RecyclerView.ViewHolder   implements View.OnClickListener,View.OnCreateContextMenuListener{
        public TextView title,duration,artist,id,sizeofVideo;
        String insert_query,select_query,delete_query;
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

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }


        void bind(int listIndex) {
            title.setText(String.valueOf(listIndex));
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getLayoutPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Details").setOnMenuItemClickListener(onEditMenu);//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Delete").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, v.getId(), 0, "Mark").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, v.getId(), 0, "Share").setOnMenuItemClickListener(onEditMenu);

        }


        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String a = String.valueOf(list.get(getAdapterPosition()));
                // int b = Integer.parseInt(a);

                //strings.add(0);

                String item1 = item.toString();
                if (item1.equalsIgnoreCase("Details"))
                {
                    Intent ii = new Intent(itemView.getContext(), Details.class);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    final String i = String.valueOf(list.get(getAdapterPosition()).getName());
                    ii.putExtra("vidid", i);
                    itemView.getContext().startActivity(ii);

                    Toast.makeText(context, "Details", Toast.LENGTH_SHORT).show();
                }
                else if (item1.equalsIgnoreCase("Delete"))
                {

                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
                    alertDialog.setTitle("Delete Item");
                    alertDialog.setMessage("Are you sure you want to delete this file?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {


                        } });
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent ii = new Intent(itemView.getContext(),VideoActivity.class);
                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            itemView.getContext().startActivity(ii);
                            final String i = String.valueOf(list.get(getAdapterPosition()).getData());
                            File file = new File(i);
                            boolean deleted = file.delete();
                            Toast.makeText(itemView.getContext(), ""+deleted, Toast.LENGTH_SHORT).show();
                            delete_query = "DELETE FROM videos where filename='"+i+"' ";
                            Log.e("delete query", delete_query);
                          //  db.delete(delete_query);
                            MediaFileFunctions  mediaFileFunctions = new MediaFileFunctions();
                            mediaFileFunctions.deleteViaContentProvider(itemView.getContext(),i);
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.setIcon(R.drawable.ic_video_library_black_24dp);
                    alertDialog.show();




                }
                else if (item1.equalsIgnoreCase("Mark"))
                {   int idofvideo;

                    final String i = String.valueOf(list.get(getAdapterPosition()).getData());
                    insert_query = "Insert into videopath(path) values("+'"'+""+i+""+'"'+")";
                    Log.e("Insert query", insert_query);
                    //db.insert_update(insert_query);
                    multiple = true;
                    Toast.makeText(context, "Select Multiple", Toast.LENGTH_SHORT).show();
                    songsClass.setSelected(!songsClass.isSelected());
                    itemView.setBackgroundColor(songsClass.isSelected() ? Color.BLUE : Color.BLUE);

                    select_query = "select * from videos where filename='"+i+"'";
                    Log.e("select query", select_query);

                    /*
                    c = db.selectQuery(select_query);
                    if (c.getCount() > 0) {
                        if (c.moveToFirst() && c != null) {
                            do {
                                idofvideo = c.getInt(c.getColumnIndex("id"));
                                idofvideo--;
                                strings.add(idofvideo);

                            }
                            while (c.moveToNext());
                        }
                    }
                    */
//                    for (int u =0 ; u<strings.size(); u++)
//                    {
//                        String name =  strings.get(u);
//                        MediaFileFunctions  mediaFileFunctions = new MediaFileFunctions();
//                        mediaFileFunctions.deleteViaContentProvider(itemView.getContext(),name);
//                    }

                }
                if (item1.equalsIgnoreCase("Share")) {
                    final String i = String.valueOf(list.get(getAdapterPosition()).getData());
                    shareVideoWhatsApp(i);
                }




                return true;
            }
        };

        public void setItemClickListener(onItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public void shareVideoWhatsApp(String v) {
            File file = new File(v);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.setPackage("com.whatsapp");
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
    public class MediaFileFunctions
    {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public  boolean deleteViaContentProvider(Context context, String fullname)
        {
            Uri uri=getFileUri(context,fullname);

            if (uri==null)
            {
                return false;
            }

            try
            {
                ContentResolver resolver=context.getContentResolver();

                // change type to image, otherwise nothing will be deleted
                ContentValues contentValues = new ContentValues();
                int media_type = 1;
                contentValues.put("media_type", media_type);
                resolver.update(uri, contentValues, null, null);

                return resolver.delete(uri, null, null) > 0;
            }
            catch (Throwable e)
            {
                return false;
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private  Uri getFileUri(Context context, String fullname)
        {
            // Note: check outside this class whether the OS version is >= 11
            Uri uri = null;
            Cursor cursor = null;
            ContentResolver contentResolver = null;

            try
            {
                contentResolver=context.getContentResolver();
                if (contentResolver == null)
                    return null;

                uri= MediaStore.Files.getContentUri("external");
                String[] projection = new String[2];
                projection[0] = "_id";
                projection[1] = "_data";
                String selection = "_data = ? ";    // this avoids SQL injection
                String[] selectionParams = new String[1];
                selectionParams[0] = fullname;
                String sortOrder = "_id";
                cursor=contentResolver.query(uri, projection, selection, selectionParams, sortOrder);

                if (cursor!=null)
                {
                    try
                    {
                        if (cursor.getCount() > 0) // file present!
                        {
                            cursor.moveToFirst();
                            int dataColumn=cursor.getColumnIndex("_data");
                            String s = cursor.getString(dataColumn);
                            if (!s.equals(fullname))
                                return null;
                            int idColumn = cursor.getColumnIndex("_id");
                            long id = cursor.getLong(idColumn);
                            uri= MediaStore.Files.getContentUri("external",id);
                        }
                        else // file isn't in the media database!
                        {
                            ContentValues contentValues=new ContentValues();
                            contentValues.put("_data",fullname);
                            uri = MediaStore.Files.getContentUri("external");
                            uri = contentResolver.insert(uri,contentValues);
                        }
                    }
                    catch (Throwable e)
                    {
                        uri = null;
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
            catch (Throwable e)
            {
                uri=null;
            }
            return uri;
        }
    }

}
