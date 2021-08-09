package com.play.view.player.videoplayer4k.CursorUtils;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.play.view.player.videoplayer4k.FontContm;
import com.play.view.player.videoplayer4k.Model.Video;
import com.play.view.player.videoplayer4k.R;
import com.play.view.player.videoplayer4k.VideoDetailActivityFliper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VideosListAdapterForGrid extends RecyclerView.Adapter<VideosListAdapterForGrid.VideoViewHolder> {
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    public List<Video> videos;
    private AdRequest adRequest;
    int folderposition;
    private int lastPosition = -1;
    private LayoutInflater inflater;
    int firsttime = 0;
    int firsttime1 = 0;
    int counterone = 0;
    Video videoSong;


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

    private class AsyncLoadThumbs extends AsyncTask<VideoAndView, Void, VideoAndView> {
        private AsyncLoadThumbs() {
        }

        protected void onPreExecute() {
        }

        protected final VideoAndView doInBackground(VideoAndView... args) {
            VideoAndView container = args[0];
            container.thumbnail = ExtractThumbUtility.getThumbnailPathForLocalFile(VideosListAdapterForGrid.this.context, container.uri);
            return container;
        }

        protected void onPostExecute(VideoAndView result) {
            Uri thumbnailUri = null;
            if (!isCancelled()) {
                try {
                    thumbnailUri = Uri.fromFile(new File(result.thumbnail));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Glide.with(context)
//                        .load(thumbnailUri).placeholder(R.drawable.ic_video_icon)
//                        .into(result.imageView);
                Picasso.get().load(thumbnailUri).placeholder(R.drawable.ic_video_icon).into(result.imageView);
            }
        }
    }

    class VideoAndView {
        ImageView imageView;
        String thumbnail;
        Uri uri;

        VideoAndView() {
        }
    }

    static class VideoViewHolder extends ViewHolder {
        AsyncLoadThumbs asyncLoadThumbs;
        CardView cvItem;
        ImageView ivVideoThumbnail;
        RelativeLayout rlytVideoItemLayout;
        TextView tvDuration;
        TextView tvResolution;
        TextView tvTitle;
        AdView adView;
        RelativeLayout lLayout;
        TextView tvSize;

        VideoViewHolder(View v) {
            super(v);
            this.cvItem = (CardView) v.findViewById(R.id.video_item);
            this.rlytVideoItemLayout = (RelativeLayout) v.findViewById(R.id.video_item_layout);
            this.tvTitle = (TextView) v.findViewById(R.id.tv_title);
            this.tvDuration = (TextView) v.findViewById(R.id.tv_duration);
            this.tvResolution = (TextView) v.findViewById(R.id.tv_resolution);
            this.ivVideoThumbnail = (ImageView) v.findViewById(R.id.iv_video_thumb);
            this.lLayout = (RelativeLayout) v.findViewById(R.id.advertie_layout);
            this.tvSize = (TextView) v.findViewById(R.id.tv_size);
            this.adView = (AdView) v.findViewById(R.id.adView_banner);
            new FontContm(v.getContext(), tvTitle);
            new FontContm(v.getContext(), tvDuration);
            new FontContm(v.getContext(), tvResolution);
            new FontContm(v.getContext(), tvSize);
        }

        void setItemActivated() {
            this.rlytVideoItemLayout.setBackgroundResource(R.color.colorAccent);
        }

        void setItemDeactivated() {
            this.rlytVideoItemLayout.setBackgroundResource(R.color.Lightblack);
        }
    }

    public VideosListAdapterForGrid(Context context, List<Video> objects, int folderposition) {
        this.context = context;
        this.videos = objects;
        this.folderposition = folderposition;
        adRequest = new AdRequest.Builder()
                .build();
    }

    public int getItemCount() {
        return this.videos.size();
    }

    public void onBindViewHolder(final VideoViewHolder videoViewHolder, final int position) {
        try {
            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            videoViewHolder.itemView.startAnimation(animation);
            lastPosition = position;
            Video video = (Video) this.videos.get(position);
            this.videoSong = this.videos.get(position);
            videoViewHolder.tvTitle.setText(video.getTitle());
            videoViewHolder.tvDuration.setText(video.getDuration());
            videoViewHolder.tvResolution.setText(video.getResolution());
            videoViewHolder.tvSize.setText(video.getSizeReadable());
            Log.i("allDataValues",
                    video.getData()
                            + " = " + video.getDateAdded()
                            + " = " + video.getSizeReadable()
                            + " = " + video.getSize()
                            + " = " + video.get_ID()
                            + " = " + video.getName()
                            + " = " + video.getDuration()
                            + " = " + video.getMime()
                            + " = " + video.getTotalvideos()
                            + " = " + video.getResolution());
            Uri videoUri = ExtractThumbUtility.getVideoContentUri(this.context, new File(video.getData()));
            VideoAndView container = new VideoAndView();
            container.uri = videoUri;
            int id = Integer.parseInt(videoUri.toString().substring(videoUri.toString().lastIndexOf("/")+1));
            ContentResolver crThumb = this.context.getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            videoViewHolder.ivVideoThumbnail.setImageBitmap(curThumb);
            /*if (videoViewHolder.asyncLoadThumbs == null) {
                videoViewHolder.asyncLoadThumbs = new AsyncLoadThumbs();
            } else {
                videoViewHolder.asyncLoadThumbs.cancel(true);
                videoViewHolder.asyncLoadThumbs = new AsyncLoadThumbs();
            }
            videoViewHolder.asyncLoadThumbs.execute(new VideoAndView[]{
                    container
            });*/
            if (this.selectedItems.get(position, false)) {
                Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                intent.putExtra("videofilename", this.videoSong);
                context.startActivity(intent);
                videoViewHolder.setItemActivated();
            } else

            {
                videoViewHolder.setItemDeactivated();
            }
            videoViewHolder.rlytVideoItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    videoContext(videos.get(position).getData(),position);
                    return true;
                }
            });
            videoViewHolder.rlytVideoItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mark = lastPosition;
                    String i = videos.get(position).getData();
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                    intent.putExtra("videofilename", videos.get(position).getData());
                    intent.putExtra("title", videos.get(position).getName());
                    intent.putExtra("id", videos.get(position).get_ID());
                    intent.putExtra("folderposition", folderposition);
                    intent.putExtra(Constants.VIDEO_INDEX, position);
                    intent.putExtra("Showbuttons", false);
                    intent.putExtra("ShowNoti", true);
                    System.out.println("Song id " + videos.get(position).get_ID());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
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

    void videoContext(String data, int position) {
        String filename = data.substring(data.lastIndexOf("/") + 1);
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Delete Video : " + filename)
                .setMessage("Do you want to do delete this view?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ContentResolver resolver = context.getContentResolver();
                            Uri uri = getFileUri(context, data);
                            int a = resolver.delete(uri, null, null);
                            if (a > 0) {
                                Toast.makeText(context, "Video" + filename + " deleted  successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Video" + filename + " not deleted  successfully", Toast.LENGTH_SHORT).show();
                            }
                            VideosListAdapterForGrid.this.videos.remove(position);
                            VideosListAdapterForGrid.this.notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (SecurityException e) {
                        }
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video_grid, viewGroup, false));
    }

    public void onViewRecycled(VideoViewHolder holder) {
        super.onViewRecycled(holder);
//        Glide.with(context)
//                .load((int) R.drawable.ic_video_icon).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.video_icon)
//                .into(holder.ivVideoThumbnail);
//        Picasso.get().cancelRequest(holder.ivVideoThumbnail);
//        Picasso.get().load((int) R.drawable.ic_video_icon).into(holder.ivVideoThumbnail);
    }

    public void removeItem(int position) {
        this.videos.remove(position);
        notifyItemRemoved(position);
    }

    public void toggleSelection(int pos) {
        if (this.selectedItems.get(pos, false)) {
            this.selectedItems.delete(pos);

        } else {
            this.selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        this.selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return this.selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList(this.selectedItems.size());
        for (int i = 0; i < this.selectedItems.size(); i++) {
            items.add(Integer.valueOf(this.selectedItems.keyAt(i)));
        }
        return items;
    }

    public void sortByName() {
        // Collections.sort(StorageUtil.folder,null);
        Collections.sort(videos, new Comparator<Video>() {
            public int compare(Video obj1, Video obj2) {
                // ## Ascending order
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });
    }

    public void sortByPrice() {
        Collections.sort(videos, (l1, l2) -> {
            if (l1.getSize() > l2.getSize()) {
                return 1;
            } else if (l1.getSize() < l2.getSize()) {
                return -1;
            } else {
                return 0;
            }
        });
    }
}
