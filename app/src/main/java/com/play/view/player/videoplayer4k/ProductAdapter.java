package com.play.view.player.videoplayer4k;

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

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by azhar on 10/11/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myView> {
    final boolean clear_cache;
    LayoutInflater inflater;
    Context context;
    Songs songsClass;
    ArrayList<Songs> list;
    int selectedPosition = -1;
    int lastPosition = -1;
    int audioIndex = -1;
    StorageUtil storage ;





    public ProductAdapter(Context context, ArrayList<Songs> arraylist) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;
        storage = new StorageUtil(context.getApplicationContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);
        System.out.println("MainActivity Switch State" + clear_cache);


    }

    @Override
    public myView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_audio, parent, false);

        return new myView(view);

    }


    @Override
    public void onBindViewHolder(myView holder, final int position) {


        Log.d(getClass().getSimpleName(), "#" + position);
        holder.bind(position);
        songsClass = list.get(position);

        audioIndex = storage.loadAudioIndex();
        if(audioIndex != -1 && audioIndex < list.size()){
            if(audioIndex == position)
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
//        lastPosition = position;
//        if (selectedPosition == position) {
//            holder.title.setTextColor(Color.parseColor("#fb0303"));
//            holder.duration.setTextColor(Color.parseColor("#fb0303"));
//            holder.title.setTypeface(null, Typeface.BOLD);
//            holder.headphoneimage.setImageResource(com.azhar.azhar.player.R.drawable.download);
//
//        } else {
//            holder.title.setTypeface(null, Typeface.NORMAL);
//            holder.headphoneimage.setImageResource(com.azhar.azhar.player.R.drawable.head);
//            holder.title.setTextColor(Color.parseColor("#000000"));
//            holder.duration.setTextColor(Color.parseColor("#000000"));
//
//        }


        holder.title.setText(songsClass.getName());
        holder.duration.setText(songsClass.getDuration());
        if (clear_cache) {

            /*
            Picasso.with(context).load(String.valueOf(songsClass.getImage())).
                    placeholder(R.drawable.headphones)
                    .error(R.drawable.headphones).into(holder.proimage);
                    */


        } else {
            holder.proimage.setImageResource(R.drawable.headphones);
        }

        holder.setItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(View view, int itemPos) {


                 int id = itemPos;

                Intent intent = new Intent(context, AudioDetail.class);
                intent.putExtra("filename", list.get(itemPos).getData());
                intent.putExtra("title", list.get(itemPos).getName());
                intent.putExtra("id", id);
                intent.putExtra("ShowNoti", true);
                intent.putExtra("Showbuttons", false);
                intent.putExtra("mylist", list);
                intent.putExtra("artist", list.get(itemPos).getArtist());
                intent.putExtra("bmp_Image", list.get(itemPos).getImage());
                context.startActivity(intent);
//                selectedPosition = itemPos;
//                audioIndex = itemPos;
//                notifyDataSetChanged();


            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);

    }


    public class myView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, duration, artist, id;

        //        TextView proname;
        ImageView proimage, headphoneimage;
        onItemClickListener itemClickListener;


        public myView(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.last_text);
            duration = (TextView) itemView.findViewById(R.id.last_text_time);

            proimage = (ImageView) itemView.findViewById(R.id.past_icon);

            headphoneimage = (ImageView) itemView.findViewById(R.id.headPhoneImage);
            itemView.setOnClickListener(this);

        }

        public void bind(int listIndex) {
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
