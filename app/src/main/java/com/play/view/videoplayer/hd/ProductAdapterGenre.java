package com.play.view.videoplayer.hd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


//import com.bumptech.glide.Glide;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by azhar on 10/11/2017.
 */

public class ProductAdapterGenre extends RecyclerView.Adapter<ProductAdapterGenre.myViewGenre> {
    final boolean clear_cache;
    LayoutInflater inflater;
    Context context;
    Songs songsClass;
    ArrayList<Songs> list;
    int selectedPosition = -1;
    int lastPosition = -1;
    int audioIndex = -1;
    StorageUtil storage ;

    public ProductAdapterGenre(Context context, ArrayList<Songs> arraylist) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;
        storage = new StorageUtil(context.getApplicationContext());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);
        System.out.println("MainActivity Switch State" + clear_cache);
    }

    @Override
    public myViewGenre onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_grid, parent, false);
        return new myViewGenre(view);

    }


    @Override
    public void onBindViewHolder(myViewGenre holder, final int ItemPos) {
//        Animation animation = AnimationUtils.loadAnimation(context,
//                (ItemPos > lastPosition) ? R.anim.up_from_bottom
//                        : R.anim.down_from_top);
//        holder.itemView.startAnimation(animation);

        Log.d(getClass().getSimpleName(), "#" + ItemPos);
        holder.bind(ItemPos);
        songsClass = list.get(ItemPos);
        lastPosition = ItemPos;

        audioIndex = storage.loadAudioIndex();
        if(audioIndex != -1 && audioIndex < list.size()){
            if(audioIndex == ItemPos)
            {
                holder.title.setTextColor(Color.parseColor("#fb0303"));
                holder.duration.setTextColor(Color.parseColor("#fb0303"));
                holder.title.setTypeface(null, Typeface.BOLD);
                holder.headphoneimage.setImageResource(R.drawable.download);
            }
            else{
                holder.title.setTypeface(null, Typeface.NORMAL);
                holder.headphoneimage.setImageResource(R.drawable.logo);
                holder.title.setTextColor(Color.parseColor("#000000"));
                holder.duration.setTextColor(Color.parseColor("#000000"));
            }
        }
//        if (selectedPosition == ItemPos) {
////            holder.title.setBackgroundColor(Color.parseColor("#fb0303"));
//            holder.title.setTextColor(Color.parseColor("#fb0303"));
//            holder.duration.setTextColor(Color.parseColor("#fb0303"));
//            holder.title.setTypeface(null, Typeface.BOLD);
//            holder.headphoneimage.setImageResource(R.drawable.download);
//        } else {
//            holder.title.setTypeface(null, Typeface.NORMAL);
//            holder.headphoneimage.setImageResource(R.drawable.head);
//            holder.title.setTextColor(Color.parseColor("#000000"));
//            holder.duration.setTextColor(Color.parseColor("#000000"));
////            holder.title.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }

        holder.title.setText(songsClass.getName());
        if (clear_cache) {
            Picasso.get().load(String.valueOf(songsClass.getImage()))
                    .placeholder(R.drawable.headicon).error(R.drawable.headicon).into(holder.proimage);
//            Glide.with(context)
//                    .load(String.valueOf(songsClass.getImage())).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.headicon)
//                    .into(holder.proimage);
        } else {
            holder.proimage.setImageResource(R.mipmap.logo);
        }
        holder.duration.setText(songsClass.getDuration());
        holder.setItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(View view, int position) {


                int id = position;
                Intent intent = new Intent(context, AudioDetail.class);
                intent.putExtra("filename", list.get(position).getData());
                intent.putExtra("title", list.get(position).getName());
                intent.putExtra("id", id);
                intent.putExtra("mylist", list);
                intent.putExtra("Showbuttons", false);
                intent.putExtra("ShowNoti", true);
                intent.putExtra("artist", list.get(position).getArtist());
                intent.putExtra("bmp_Image", list.get(position).getImage());
                context.startActivity(intent);
//                selectedPosition = position;
                audioIndex = position;
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


    public class myViewGenre extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, duration, artist, id;

        //        TextView proname;
        ImageView proimage, headphoneimage;
        onItemClickListener itemClickListener;

        public myViewGenre(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.last_text);
            duration = (TextView) itemView.findViewById(R.id.last_text_time);

            proimage = (ImageView) itemView.findViewById(R.id.past_icon);
            headphoneimage = (ImageView) itemView.findViewById(R.id.headgrid);

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

    public void setLv(ArrayList<Songs> listdata) {
        list = listdata;
        notifyDataSetChanged();
    }
}