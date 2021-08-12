package com.play.view.videoplayer.hd.CursorUtils;

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

import com.play.view.videoplayer.hd.FontContm;
import com.play.view.videoplayer.hd.Model.Video;
import com.play.view.videoplayer.hd.R;
import com.play.view.videoplayer.hd.VideoDetailActivityFliper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VideosListAdapterForList extends RecyclerView.Adapter<VideosListAdapterForList.VideoViewHolder> {
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private List<Video> videos;
    private AdRequest adRequest;
    int folderposition;
    int firsttime = 0;
    int firsttime1 = 0;
    int counterone = 0;
    Video videoSong;
    private int lastPosition = -1;

    private class AsyncLoadThumbs extends AsyncTask<VideoAndView, Void, VideoAndView> {
        private AsyncLoadThumbs() {
        }

        protected void onPreExecute() {
        }

        protected final VideoAndView doInBackground(VideoAndView... args) {
            VideoAndView container = args[0];
            container.thumbnail = ExtractThumbUtility.getThumbnailPathForLocalFile(VideosListAdapterForList.this.context, container.uri);
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
//                        .load(thumbnailUri).placeholder(R.drawable.video_icon)
//                        .into(result.imageView);
//                     Picasso.get().load(thumbnailUri).placeholder(R.drawable.ic_video_icon).into(result.imageView);
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

    public VideosListAdapterForList(Context context, List<Video> objects, int folderposition) {
        this.context = context;
        this.videos = objects;
        this.folderposition = folderposition;
        adRequest = new AdRequest.Builder()
                .build();
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    int videoSize;
    public int getItemCount() {
        try{
        videoSize=this.videos.size();
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
        return videoSize;
    }

    public void onBindViewHolder(final VideoViewHolder videoViewHolder, final int position) {

        try {
//            Log.i("adPosition", position + "");
//            videoViewHolder.adView.loadAd(adRequest);
//
//            firsttime1 = 1;
//
//            videoViewHolder.adView.setAdListener(new AdListener() {
//
//                @Override
//                public void onAdLoaded() {
//
//                    Log.i("positionAd", position + "" + firsttime1);
//                    int prime = 11;
//                    if (position == 0) {
//                        videoViewHolder.lLayout.setVisibility(View.GONE);
//                    } else if (position % prime == 0) {
//                        videoViewHolder.lLayout.setVisibility(View.VISIBLE);
//                    } else {
//
//                    }
//
//                }
//
//                @Override
//                public void onAdFailedToLoad(int errorCode) {
//                    Log.i("positionAd", position + " " + firsttime1 + " " + errorCode);
//                }
//            });
//
            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            videoViewHolder.itemView.startAnimation(animation);
            lastPosition = position;
            Video video = (Video) this.videos.get(position);
            this.videoSong = this.videos.get(position);
            videoViewHolder.tvTitle.setText(video.getTitle());
            videoViewHolder.tvDuration.setText("Duration: " + video.getDuration());
            videoViewHolder.tvResolution.setText("Resolution: " + video.getResolution());
            File file = new File(video.getData());
            long length = file.length();
            videoViewHolder.tvSize.setText("Size: " + getFileSize(length));
            Log.i("SizeMe",""+video.getSizeReadable());
            Log.i("allDataValues",
                    video.getData()
                            + " = " + video.getDateAdded()
                            + " = " + getFileSize(length)
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
//            container.imageView = videoViewHolder.ivVideoThumbnail;
//            if (videoViewHolder.asyncLoadThumbs == null) {
//                videoViewHolder.asyncLoadThumbs = new AsyncLoadThumbs();
//            } else {
//                videoViewHolder.asyncLoadThumbs.cancel(true);
//                videoViewHolder.asyncLoadThumbs = new AsyncLoadThumbs();
//            }
//            videoViewHolder.asyncLoadThumbs.execute(new VideoAndView[]
//
//                    {
//                            container
//                    });
            if (this.selectedItems.get(position, false)) {
                Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                intent.putExtra("videofilename", this.videoSong);
                context.startActivity(intent);
                videoViewHolder.setItemActivated();
            } else {
                videoViewHolder.setItemDeactivated();
            }

            {
//            Mint.logException("VideosListAdapterForList", "onBindViewHolder", e);
            }

            videoViewHolder.rlytVideoItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    videoContext(videos.get(position).getData(), position);
                    return true;
                }
            });
            videoViewHolder.rlytVideoItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mark = position;
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
      /*  if (position == this.videos.size() - 1) {
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            int topBottomMargins = (int) UnitConversionUtiil.convertDpToPixel(3.0f, this.context);
            int leftRightMargins = (int) UnitConversionUtiil.convertDpToPixel(10.0f, this.context);
            layoutParams.topMargin = topBottomMargins;
            layoutParams.leftMargin = leftRightMargins;
            layoutParams.rightMargin = leftRightMargins;
            layoutParams.bottomMargin = topBottomMargins;
            videoViewHolder.cvItem.setLayoutParams(layoutParams);
        }*/
        } catch (IndexOutOfBoundsException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (ActivityNotFoundException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (SecurityException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (RuntimeException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (Exception e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } finally {
            Log.e("ExceptionError", " = Finally");
        }
    }


    VideoViewHolder videoAndView;
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        try {
            videoAndView = new VideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video_list, viewGroup, false));
        } catch (IndexOutOfBoundsException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (ActivityNotFoundException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (SecurityException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (RuntimeException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (Exception e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } finally {
            Log.e("ExceptionError", " = Finally");
        }
        return videoAndView;
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
                            VideosListAdapterForList.this.videos.remove(position);
                            VideosListAdapterForList.this.notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (SecurityException e) {
                        }
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

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
}
