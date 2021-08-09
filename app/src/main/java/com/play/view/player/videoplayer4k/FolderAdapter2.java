//package com.dhdvideo.player;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.io.File;
//import java.util.List;
//
///**
// * Created by DuskySolution on 12/15/2017.
// */
//
//public class FolderAdapter2 extends RecyclerView.Adapter<FolderAdapter.MyHolder> {
//
//    Products productsClass;
//    List<File> folders_list;
//    List<String> folders_count;
//    private LayoutInflater inflater;
//    private Context context;
//
//
//    public FolderAdapter2(Context context,List<File> folders_list ){
//        inflater = LayoutInflater.from(context);
//        this.folders_list = folders_list;
//    }
//
//    @Override
//    public FolderAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.list_item_folders, parent, false);
//        return new FolderAdapter.MyHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(FolderAdapter.MyHolder holder, int position) {
//        Log.d(getClass().getSimpleName(), "#" + position);
//        holder.bind(position);
////        productsClass = folders_list.get(position);
//       String b= folders_list.get(position).toString();
//        holder.folders_name.setText(b);
//        //holder.folders_name.setText((TextView) folders_list.get(position));
//    }
//
//
//
//
//
//    @Override
//    public int getItemCount() {
//        return folders_list.size();
//    }
//
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//    public class MyHolder extends RecyclerView.ViewHolder {
//
//        public TextView folders_name,total_count;
//
//        public MyHolder(View itemView) {
//            super(itemView);
//
//            folders_name = (TextView)itemView.findViewById(R.id.folders_name);
////            total_count = (TextView)itemView.findViewById(R.id.total_count);
//        }
//        void bind(int listIndex) {
//            folders_name.setText(String.valueOf(listIndex));
//        }
//    }
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//
//
//}
