package com.play.view.player.videoplayer4k.CursorUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.play.view.player.videoplayer4k.FontContm;
import com.play.view.player.videoplayer4k.Model.Video;
import com.play.view.player.videoplayer4k.R;
import com.play.view.player.videoplayer4k.VideoDetailActivityFliper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideosListAdapterForMediaPlayer extends RecyclerView.Adapter<VideosListAdapterForMediaPlayer.VideoViewHolder> {
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
            container.thumbnail = ExtractThumbUtility.getThumbnailPathForLocalFile(VideosListAdapterForMediaPlayer.this.context, container.uri);
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
            this.ivVideoThumbnail = (ImageView) v.findViewById(R.id.iv_video_thumb);
            this.lLayout = (RelativeLayout) v.findViewById(R.id.advertie_layout);
            this.adView = (AdView) v.findViewById(R.id.adView_banner);

            this.tvTitle.setVisibility(View.GONE);
            new FontContm(v.getContext(), tvTitle);
        }

        void setItemActivated() {
            this.rlytVideoItemLayout.setBackgroundResource(R.color.colorAccent);
        }

        void setItemDeactivated() {
            this.rlytVideoItemLayout.setBackgroundResource(R.color.Lightblack);
        }
    }

    public VideosListAdapterForMediaPlayer(Context context, List<Video> objects, int folderposition) {
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
            Uri videoUri = ExtractThumbUtility.getVideoContentUri(this.context, new File(video.getData()));
            int id = Integer.parseInt(videoUri.toString().substring(videoUri.toString().lastIndexOf("/")+1));
            ContentResolver crThumb = this.context.getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            videoViewHolder.ivVideoThumbnail.setImageBitmap(curThumb);
            if (this.selectedItems.get(position, false))

            {
                Intent intent = new Intent(context, VideoDetailActivityFliper.class);
                intent.putExtra("videofilename", this.videoSong);
                context.startActivity(intent);
                videoViewHolder.setItemActivated();
            } else

            {
                videoViewHolder.setItemDeactivated();
            }

            {
//            Mint.logException("VideosListAdapterForList", "onBindViewHolder", e);
            }


            videoViewHolder.rlytVideoItemLayout.setOnClickListener(new View.OnClickListener()

            {
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
        } catch (Exception e) {
        }
    }

    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video_for_mediaplayer, viewGroup, false));
    }

    public void onViewRecycled(VideoViewHolder holder) {
        super.onViewRecycled(holder);
//        Glide.with(context)
//                .load((int) R.drawable.ic_video_icon).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_video_icon)
//                .into(holder.ivVideoThumbnail);
        Picasso.get().cancelRequest(holder.ivVideoThumbnail);
        Picasso.get().load((int) R.drawable.ic_video_icon).into(holder.ivVideoThumbnail);
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
}
