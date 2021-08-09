package com.play.view.player.videoplayer4k;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by azhar on 10/11/2017.
 */

public class SearchVideoSongsAdapter extends RecyclerView.Adapter<SearchVideoSongsAdapter.myView> implements Filterable {
    private final boolean clear_cache;
    private LayoutInflater inflater;
    private Context context;
    VideoSongs songsClass;
    ArrayList<VideoSongs> list;
    private ArrayList<VideoSongs> mFilteredList;
    static String nameofSOngs;
    static Bitmap sourceofImage;
    static int pos;
    private int lastPosition = -1;


    //
    public SearchVideoSongsAdapter(Context context, ArrayList<VideoSongs> arraylist    ) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.mFilteredList = arraylist;
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);
        System.out.println("MainActivity Switch State" + clear_cache);
    }

    @Override
    public myView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_video_list, parent, false);
        return new myView(view);

    }


    @Override
    public void onBindViewHolder(final myView holder, final int position) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        Log.d(getClass().getSimpleName(), "#" + position);
        holder.bind(position);

        songsClass = mFilteredList.get(position);
        holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
        if (clear_cache) {
            //Picasso.with(context).load(String.valueOf(songsClass.getImage())).placeholder(R.drawable.head).error(R.drawable.head).fit().centerInside().into(holder.proimage);

        } else {
            holder.proimage.setImageResource(R.drawable.logo);
        }
        holder.sizeofVideo.setText(songsClass.getSize());
        //        holder.title.setText(songs.get(position).get("displayname"));
//        holder.artist.setText(songs.get(position).get("artist"));
        holder.duration.setText(songsClass.getDuration());
//        holder.title.setText(songs.get(position).get("title"));
//        String cove = songs.get(position).get("albumart");
//        Bitmap art = BitmapFactory.decodeFile(cove);
//        holder.proimage.setImageBitmap(art);
//         Picasso.with(context).load(String.valueOf(songsClass.getImage()))
//                .placeholder(com.example.azhar.playerapp.R.drawable.ic_android_black_24dp).into(holder.proimage);
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

        holder.setItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(View view, int itemPos) {
                Log.i("", "onCLick" + itemPos);
//                System.out.println("Name" + mFilteredList.get(position).getAlbum());
//                System.out.println("Name" + mFilteredList.get(position).getDuration());
//                System.out.println("Name" + mFilteredList.get(position).getArtist());
//                System.out.println("Name" + mFilteredList.get(position).getName());
//                System.out.println("Name" + mFilteredList.get(position).getData());
//                System.out.println("Name" + mFilteredList.get(position).getImage());


                String id = String.valueOf(position);
                Intent intent = new Intent(holder.itemView.getContext(), VideoDetailActivityFliper.class);
                intent.putExtra("videofilename", songsClass.getData());
                intent.putExtra("title", songsClass.getName());
                intent.putExtra("Showbuttons",false);
                intent.putExtra("ShowNoti",false);
                intent.putExtra("id", id);
//                intent.putExtra("artist", list.get(itemPos).getArtist());
//                intent.putExtra("bmp_Image", list.get(itemPos).getImage());
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return (mFilteredList == null) ? 0 : mFilteredList.size();

    }



    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = list;
                } else {

                    ArrayList<VideoSongs> filteredList = new ArrayList<>();

                    for (VideoSongs androidVersion : list) {

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

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<VideoSongs>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class myView extends RecyclerView.ViewHolder  implements View.OnClickListener   {
        public TextView title,duration,artist,id,sizeofVideo;

        //        TextView proname;
        ImageView proimage;
        onItemClickListener itemClickListener;
        public myView(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.last_text);
            duration = (TextView) itemView.findViewById(R.id.last_text_time);
            sizeofVideo = (TextView) itemView.findViewById(R.id.sizeOfSons);

            proimage = (ImageView) itemView.findViewById(R.id.past_icon);
//            proname = (TextView) itemView.findViewById(R.id.last_text);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(itemView.getContext(),AudioDetail.class);
//                    intent.putExtra("name",title.getText());
//                    intent.putExtra("imageId",);
//                    itemView.getContext().startActivity(intent);
//                }
//            });
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
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(myView holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();

    }
}
