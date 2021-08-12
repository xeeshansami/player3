package com.play.view.videoplayer.hd;

import android.content.ContentUris;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListItemAdapter extends ArrayAdapter<VideoSongs> {
	boolean multiple = false;
	protected Context activity;
	protected boolean showDynamicTags = false;
	private AdRequest adRequest;
	SharedPreferences sharedPreferences;
    private SharedPreferences mSharedPrefs = null;
	static int onevideo;
	CardView cardView;
	RelativeLayout rlLayout;
	private LayoutInflater layoutInflater;

	ArrayList<VideoSongs> items;

	String selection = MediaStore.Video.VideoColumns.DATA + " like?";
	String []projection= {MediaStore.Video.VideoColumns._ID,
			MediaStore.Video.VideoColumns.DATA,
			//     MediaStore.Video.VideoColumns.DISPLAY_NAME,
			//   MediaStore.Video.VideoColumns.ARTIST,
			//   MediaStore.Video.VideoColumns.RESOLUTION,
			//  MediaStore.Video.VideoColumns.ALBUM,
			//  MediaStore.Video.VideoColumns.DESCRIPTION,
			MediaStore.Video.VideoColumns.TITLE,
			MediaStore.Video.VideoColumns.DURATION,
			MediaStore.Video.VideoColumns.SIZE};


	Map<Integer,Integer> adsShows=new HashMap<Integer,Integer>();

	VideoSongs songsClass;
	private OnTagClickedListener mOnTagClickedListener = null;
	private View.OnClickListener onTagTvClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view instanceof TextView && mOnTagClickedListener != null) {
				TextView tv = (TextView) view;
				final String tag = tv.getText().toString();
				mOnTagClickedListener.onTagClicked(tag);
			}
		}
	};

	public ListItemAdapter(Context context, ArrayList<VideoSongs> objects) {
		super(context, 0, objects);
		this.activity = context;
		this.items=objects;
		layoutInflater = LayoutInflater.from(context);
		MobileAds.initialize(activity, activity.getResources().getString(R.string.banner_ad_unit_id));
		adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
				.build();

		adsShows.clear();

		for(int k=0;k<items.size();k++)
		{
			if (k % 8 == 0)
			{

				if(k!=0) {
					adsShows.put(k, 0);
				}
			}
		}





	}


	@Override
	public int getCount() {
		return items.size();
	}

	@Nullable
	@Override
	public VideoSongs getItem(int position) {

		return items.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {


		 // to reference the child views for later actions


		ViewHolder holder;
		holder = new ViewHolder();

		if (view == null) {

			System.out.println("postion of if  "+position  );




			view=	layoutInflater.inflate(R.layout.list_item_video_list, null);



			//view = vi.inflate(R.layout.list_item_video_list, null);



			holder.title = (TextView) view.findViewById(R.id.last_text);
			holder.duration = (TextView) view.findViewById(R.id.last_text_time);
			holder.sizeofVideo = (TextView) view.findViewById(R.id.sizeOfSons);

			holder.proimage = (ImageView) view.findViewById(R.id.past_icon);

			holder.mAdView = (AdView) view.findViewById(R.id.adView_banner);

			holder.lLayout = (RelativeLayout) view.findViewById(R.id.advertie_layout);

			holder.cardView = (CardView) view.findViewById(R.id.cardView);

			/*

			try {


				//System.out.println("postion of add if "+position +" "+adsShows.get(position) );
				 if (adsShows.get(position) == 0 )
				 {
					v = vi.inflate(R.layout.adview_item, null);


					holder=new ViewHolder();


					holder.mAdView = (AdView) v.findViewById(R.id.adView_banner);

					holder.lLayout = (RelativeLayout) v.findViewById(R.id.advertie_layout);
					holder.main_content = (LinearLayout) v.findViewById(R.id.main_content);



					// associate the holder with the view for later lookup
					v.setTag(holder);
				}
				else {

					System.out.println("postion of add else "+position +" "+getItem(position).getName() );

					v = vi.inflate(R.layout.list_item_video_list, null);

					holder = new ViewHolder();
					holder.title = (TextView) v.findViewById(R.id.last_text);
					holder.duration = (TextView) v.findViewById(R.id.last_text_time);
					holder.sizeofVideo = (TextView) v.findViewById(R.id.sizeOfSons);

					holder.proimage = (ImageView) v.findViewById(R.id.past_icon);


					//  if (clear_cache) {
					//getFiles(songsClass.getData(),holder);

					//        holder.title.setText(songs.get(position).get("displayname"));
//        holder.artist.setText(songs.get(position).get("artist"));

					//getFiles(songsClass.data,holder);


					// associate the holder with the view for later lookup
					v.setTag(holder);





				}


			}
			catch (Exception d)
			{

				System.out.println("postion of add exception "+position +" "+getItem(position).getName() );
				v = vi.inflate(R.layout.list_item_video_list, null);

				holder = new ViewHolder();
				holder.title = (TextView) v.findViewById(R.id.last_text);
				holder.duration = (TextView) v.findViewById(R.id.last_text_time);
				holder.sizeofVideo = (TextView) v.findViewById(R.id.sizeOfSons);

				holder.proimage = (ImageView) v.findViewById(R.id.past_icon);



				//getFiles(songsClass.getData(),holder);

				//  if (clear_cache) {


				//        holder.title.setText(songs.get(position).get("displayname"));
//        holder.artist.setText(songs.get(position).get("artist"));

				//getFiles(songsClass.data,holder);


				// associate the holder with the view for later lookup
				v.setTag(holder);



			}


			// cache view fields into the holder


/*/

		}
		else {
			System.out.println("postion of else  "+position  );
			// view already exists, get the holder instance from the view
			holder = (ViewHolder) view.getTag();
		}




		songsClass = getItem(position);

		System.out.println("holder values " + songsClass.getName() + " " + songsClass.getData() + " " + songsClass.getSize() + " " + songsClass.getDuration());

		//holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
		holder.sizeofVideo.setText(songsClass.getSize());

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSharedPrefs = activity.getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mSharedPrefs.edit();
				onevideo = mSharedPrefs.getInt("oneattime", 0);

				if (multiple == false) {
					if (onevideo == 0) {
						int id = position;
						Intent intent = new Intent(activity, VideoDetailActivityFliper.class);
						intent.putExtra("videofilename", items.get(position).getData());
						intent.putExtra("title", items.get(position).getName());
						intent.putExtra("id", id);
						intent.putExtra("mylistVideo", items);
						intent.putExtra("Showbuttons", false);
						intent.putExtra("ShowNoti", true);
						System.out.println("Song id " + id);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(intent);
					}
				}

			}
		});


	//	try {


		/*
			if (position == 0) {
				holder.mAdView.loadAd(adRequest);


				holder.mAdView.setAdListener(new AdListener() {

					@Override
					public void onAdLoaded() {

						System.out.println("Add loaded");


						new Handler().post(new Runnable() {
							@Override
							public void run() {

								v.getLayoutParams().height = 140;
								v.getLayoutParams().width = SplashScreen.width;
								v.requestLayout();


								//holder.lLayout.getLayoutParams().height = 160;
								//	holder.lLayout.getLayoutParams().width = SplashScreen.width;
								//	holder.lLayout.requestLayout();


								// holder.lLayout.setLayoutParams(layout_description);


								//	holder.lLayout.setVisibility(View.VISIBLE);
							}
						});


						// advertise.getLayoutParams().width= RelativeLayout.LayoutParams.MATCH_PARENT;
						//advertise.getLayoutParams().height= 60;


					}
					// Implement AdListener
				});

			}
			*/

	/*
		try {
			if (adsShows.get(position) == 0)
			{
				holder.mAdView.loadAd(adRequest);


				holder.mAdView.setAdListener(new AdListener() {

					@Override
					public void onAdLoaded() {

						System.out.println("Add loaded");


						new Handler().post(new Runnable() {
							@Override
							public void run() {

								v.getLayoutParams().height = 140;
								v.getLayoutParams().width = SplashScreen.width;
								v.requestLayout();


								//holder.lLayout.getLayoutParams().height = 160;
								//	holder.lLayout.getLayoutParams().width = SplashScreen.width;
								//	holder.lLayout.requestLayout();


								// holder.lLayout.setLayoutParams(layout_description);


								//	holder.lLayout.setVisibility(View.VISIBLE);
							}
						});


						// advertise.getLayoutParams().width= RelativeLayout.LayoutParams.MATCH_PARENT;
						//advertise.getLayoutParams().height= 60;


					}
					// Implement AdListener
				});


			} else {
				songsClass = getItem(position);

				System.out.println("holder values " + songsClass.getName() + " " + songsClass.getData() + " " + songsClass.getSize() + " " + songsClass.getDuration());

				holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
				holder.sizeofVideo.setText(songsClass.getSize());

				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mSharedPrefs = activity.getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
						SharedPreferences.Editor mEditor = mSharedPrefs.edit();
						onevideo = mSharedPrefs.getInt("oneattime", 0);

						if (multiple == false) {
							if (onevideo == 0) {
								int id = position;
								Intent intent = new Intent(activity, VideoDetailActivityFliper.class);
								intent.putExtra("videofilename", items.get(position).getData());
								intent.putExtra("title", items.get(position).getName());
								intent.putExtra("id", id);
								intent.putExtra("mylistVideo", items);
								intent.putExtra("Showbuttons", false);
								intent.putExtra("ShowNoti", true);
								System.out.println("Song id " + id);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								activity.startActivity(intent);
							}
						}

					}
				});
			}
		}
		catch (Exception f)
		{

			System.out.println("postion of add exception2 "+position+" "+f.getMessage()  );

			songsClass = getItem(position);

			System.out.println("holder values " + songsClass.getName() + " " + songsClass.getData() + " " + songsClass.getSize() + " " + songsClass.getDuration());

			holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
			holder.sizeofVideo.setText(songsClass.getSize());

			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mSharedPrefs = activity.getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
					SharedPreferences.Editor mEditor = mSharedPrefs.edit();
					onevideo = mSharedPrefs.getInt("oneattime", 0);

					if (multiple == false) {
						if (onevideo == 0) {
							int id = position;
							Intent intent = new Intent(activity, VideoDetailActivityFliper.class);
							intent.putExtra("videofilename", items.get(position).getData());
							intent.putExtra("title", items.get(position).getName());
							intent.putExtra("id", id);
							intent.putExtra("mylistVideo", items);
							intent.putExtra("Showbuttons", false);
							intent.putExtra("ShowNoti", true);
							System.out.println("Song id " + id);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(intent);
						}
					}

				}
			});



		}
		//}

			/*
			songsClass = getItem(position);

			System.out.println("holder values " +f.getMessage());

			holder.title.setText(songsClass.getName());
//        holder.proimage.setImageBitmap(songsClass.getImage());
			holder.sizeofVideo.setText(songsClass.getSize());

			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mSharedPrefs = activity.getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
					SharedPreferences.Editor mEditor = mSharedPrefs.edit();
					onevideo = mSharedPrefs.getInt("oneattime", 0);

					if (multiple == false) {
						if (onevideo == 0) {
							int id = position;
							Intent intent = new Intent(activity, VideoDetailActivityFliper.class);
							intent.putExtra("videofilename", items.get(position).getData());
							intent.putExtra("title",items.get(position).getName());
							intent.putExtra("id", id);
							intent.putExtra("mylistVideo", items);
							intent.putExtra("Showbuttons", false);
							intent.putExtra("ShowNoti", true);
							System.out.println("Song id " + id);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(intent);
						}
					}

				}
			});
			*/






		return view;
	}





	public interface OnTagClickedListener {
		void onTagClicked(String tag);
	}

	static class ViewHolder {
		TextView title;
				TextView duration;
		TextView artist, id, sizeofVideo;
		RelativeLayout lLayout;
		LinearLayout main_content;
		CardView cardView;
		AdView mAdView;
		ImageView proimage;
		onItemClickListener itemClickListener;



	}

	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}


	/*public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}*/

	public String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

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

// return timer string
		return finalTimerString;
	}


	private void getFiles(String songsName,final ViewHolder holder) {

//        System.out.println("MAPing "+dirPath + "=media="+ MediaStore.Video.Media.EXTERNAL_CONTENT_URI );



		String[] selectionArgs = {"%" + songsName + "%"};
//        String[] selectionArgs=new String[]{"%Swag-Se-Swagat-Song--Tiger-Zinda-Hai--Salman-Khan--Katrina-Kaif.mp4%"};

		//  Log.i("Files", "Video files" + Arrays.toString(selectionArgs));
		Cursor videoCursorActivity = activity.getContentResolver().query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				projection,
				selection,
				selectionArgs,
				null);

		int totalvideoscount = videoCursorActivity.getCount();
//        folders_list.clear();
		while (videoCursorActivity.moveToNext()) {
			String  filename = videoCursorActivity.getString(
					videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
			String   title = videoCursorActivity.getString(
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



				   Picasso.get().load(albumArtUri.toString()).noFade().placeholder(R.drawable.logo).error(R.drawable.logo).centerInside().into(holder.proimage);
//				   Glide.with(activity)
//						   .load(albumArtUri.toString()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
//						   .into(holder.proimage);
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

}
