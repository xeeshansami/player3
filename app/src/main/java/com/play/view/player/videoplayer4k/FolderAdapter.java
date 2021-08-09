package com.play.view.player.videoplayer4k;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.play.view.player.videoplayer4k.CursorUtils.SelectedFolderVideoActivity;
import com.play.view.player.videoplayer4k.Model.Folder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuskySolution on 12/15/2017.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyHolder> {

    private ActionMode actionMode;
    private static WeakReference<FolderAdapter> wrActivity = null;
    static Products productsClass;
    private View view;
    static List<Folder> folders_list;
    List<String> folders_count;
    private LayoutInflater inflater;
    private static Context context;
    static int no;
    static boolean multiple = false;
    static ArrayList<Integer> strings;
    String adding = "false";


    String insert_query, select_query, delete_query;
    Cursor c;
    // RelativeLayout card;
    // DataBaseManager db;
    static SharedPreferences sharedPreferences = null;

    boolean isResumed = false;


    public FolderAdapter(Context context, List<Folder> folders_list, boolean isResumed) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.folders_list = folders_list;
        this.isResumed = isResumed;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        View view = inflater.inflate(R.layout.list_item_folders, parent, false);
        strings = new ArrayList<>();
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        try {
            Log.d(getClass().getSimpleName(), "#" + position);

            final String folder = folders_list.get(position).getName();
            final String folderPath = folders_list.get(position).getPath();
            no = position;
            Log.i("foldername", folder);
            System.out.println("Postionf of view " + position);

            holder.bind(position);

            if (position == 0) {


                if (isOnline()) {
                    if (isResumed) {

//                    System.out.println("add  loaded  before");
//
//                    try {
//
//                        holder.mAddVie.loadAd(adRequest);
//
//                        holder.mAddVie.setAdListener(new AdListener() {
//
//                            @Override
//                            public void onAdLoaded() {
//
//
//                                System.out.println("add  loaded " + holder.mAddVie.isShown());
//
//
//                                holder.addLa.setVisibility(View.VISIBLE);
//
//
//                            }
//                            // Implement AdListener
//                        });
//
//                    } catch (Exception ff) {
//
//                    }


                    }
                }


                // holder.mainLa.setVisibility(View.GONE);
                holder.folders_name.setText(folder);
                new FontContm(context, holder.folders_name);
                holder.folders_path.setText(folderPath);
                new FontContm(context, holder.folders_path);

            } else {

                holder.folders_path.setText(folderPath);
                new FontContm(context, holder.folders_path);
                holder.folders_name.setText(folder);
                new FontContm(context, holder.folders_name);

            }

            holder.mainLa.setVisibility(View.VISIBLE);

            holder.setItemClickListener(new onItemClickListener() {
                @Override
                public void onClick(View view, int itemPos) {
                    Intent intent = new Intent(context, SelectedFolderVideoActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("header", folder);
                    context.startActivity(intent);
                }
            });
        } catch (IndexOutOfBoundsException e) {
        }
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


    @Override
    public int getItemCount() {
        return folders_list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        public TextView folders_name, total_count, folders_path;
        String insert_query, select_query, delete_query;
        RelativeLayout mainLa, addLa;
        String root;
        String dbname, dbpath, dbpath2;

        onItemClickListener itemClickListener;

        public MyHolder(final View itemView) {
            super(itemView);
          /*
            db = new DataBaseManager(itemView.getContext());

            try {
                db.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            try {
//                mAddVie = (AdView) itemView.findViewById(R.id.adView_banner);
            } catch (Exception f) {

            }
            folders_name = (TextView) itemView.findViewById(R.id.folders_name);
            folders_path = (TextView) itemView.findViewById(R.id.folders_path);

            mainLa = (RelativeLayout) itemView.findViewById(R.id.mainlay);

            addLa = (RelativeLayout) itemView.findViewById(R.id.advertie_layout);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");//groupId, itemId, order, title
            try{
            menu.add(0, v.getId(), 0, "Delete").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, v.getId(), 0, "Mark").setOnMenuItemClickListener(onEditMenu);
            } catch (IndexOutOfBoundsException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (ActivityNotFoundException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (SecurityException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (IllegalStateException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (NullPointerException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            }catch (OutOfMemoryError e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (RuntimeException e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } catch (Exception e) {
                Log.e("ExceptionError"," = "+e.getMessage());
            } finally {
                Log.e("ExceptionError"," = Finally");
            }
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String a = String.valueOf(folders_list.get(getAdapterPosition()).getPath());
                // int b = Integer.parseInt(a);

                //strings.add(0);

                String item1 = item.toString();

                if (item1.equalsIgnoreCase("Delete")) {

                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
                    alertDialog.setTitle("Delete Item");
                    alertDialog.setMessage("Are you sure you want to delete this file?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkSystemWritePermission();
                            boolean retVal = true;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                retVal = Settings.System.canWrite(itemView.getContext());
                                Log.d("", "Can Write Settings: " + retVal);
                                if (retVal) {
                                    try {
//                                        ContentResolver resolver = context.getContentResolver();
//                                        Uri uri = getFileUri();
                                        int a = deleteFolder(folders_list.get(getAdapterPosition()).getPath());
//                                        int a = resolver.delete(uri, null, null);
                                        if (a > 0) {
                                            Toast.makeText(context, "Folder" + folders_list.get(getAdapterPosition()).getPath() + " deleted  successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Folder" + folders_list.get(getAdapterPosition()).getPath() + " not deleted  successfully", Toast.LENGTH_SHORT).show();
                                        }
//                                        File file=new File(folders_list.get(getAdapterPosition()).getPath());
//                                        deleteDir(file);
                                        FolderAdapter.folders_list.remove(getAdapterPosition()).getPath();
                                        FolderAdapter.this.notifyDataSetChanged();
                                        dialog.dismiss();

                                    } catch (SecurityException e) {
                                    }
                                } else {
                                    openAndroidPermissionsMenu();
                                    Toast.makeText(itemView.getContext(), "Write not allowed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    alertDialog.setIcon(R.drawable.ic_video_library_black_24dp);
                    alertDialog.show();

                } else if (item1.equalsIgnoreCase("Mark")) {
                    int idofvideo = 0;

                    final String i = String.valueOf(folders_list.get(getAdapterPosition()).getPath());
                    insert_query = "Insert into videopath(path) values(" + '"' + "" + i + "" + '"' + ")";
                    Log.e("Insert query", insert_query);
                    //  db.insert_update(insert_query);
                    Toast.makeText(itemView.getContext(), "" + i + "Marked", Toast.LENGTH_SHORT).show();
                    multiple = true;
                    //final String i = String.valueOf(folders_list.get(getAdapterPosition()));
                    String jj = "folder";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("folder", jj);
                    editor.commit();
                    //Toast.makeText(context, "Select Multiple", Toast.LENGTH_SHORT).show();
                    // productsClass.setSelected(!productsClass.isSelected());
                    itemView.setBackgroundColor(Color.BLUE);

                    select_query = "select * from folders where name='" + i + "'";
                    Log.e("select query", select_query);

                    /*
                    c = db.selectQuery(select_query);
                    if (c.getCount() > 0) {
                        if (c.moveToFirst() && c != null) {
                            do {
                                dbpath = c.getString(c.getColumnIndex("path"));
                                idofvideo = c.getInt(c.getColumnIndex("id"));
                                idofvideo--;
                                strings.add(idofvideo);

                            }
                            while (c.moveToNext());
                        }
                    }
                    */
                    editor.putInt("folderid", idofvideo);
                    editor.putString("foldername", i);
                    editor.putString("folderpath", dbpath);
                    editor.commit();

                }

                return true;
            }
        };

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

        public int deleteFolder(String foldername) {
            File dir = new File("file://"+ foldername);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
            if (dir.exists()) {
                return 0;
            } else {
                return 1;
            }
        }

        void bind(int listIndex) {
            folders_name.setText(String.valueOf(listIndex));
        }

        public boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if (dir != null && dir.isFile()) {
                return dir.delete();
            } else {
                return false;
            }
        }

        public void setItemClickListener(onItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getLayoutPosition());
        }

        private boolean checkSystemWritePermission() {
            boolean retVal = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                retVal = Settings.System.canWrite(itemView.getContext());
                Log.d("", "Can Write Settings: " + retVal);
                if (retVal) {
                    //Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show();
                } else {
                    openAndroidPermissionsMenu();
                    Toast.makeText(itemView.getContext(), "Write not allowed", Toast.LENGTH_LONG).show();
                }
            }
            return retVal;
        }

        private void openAndroidPermissionsMenu() {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + itemView.getContext().getPackageName()));
            itemView.getContext().startActivity(intent);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MediaFileFunctions {
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


}
