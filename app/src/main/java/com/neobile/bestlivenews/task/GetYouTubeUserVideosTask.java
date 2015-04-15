package com.neobile.bestlivenews.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.neobile.bestlivenews.MainActivity;
import com.neobile.bestlivenews.R;
import com.neobile.bestlivenews.domain.Video;
import com.neobile.bestlivenews.util.StreamUtils;
import com.neobile.bestlivenews.widget.VideosListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neobile.bestlivenews.MainActivity;

public class GetYouTubeUserVideosTask extends AsyncTask<Void, Void, Void> {

	private final String username;
	private String maxResults;
	private String category;
	private String requestUrl;
	private Activity activity;
	public List<Video> videos = new ArrayList<Video>();
	private ProgressDialog mProgressDialog;

	public GetYouTubeUserVideosTask(Activity activity, ProgressDialog progressDialog, String username){
		this.username = username;
		this.activity = activity;
		this.requestUrl = "https://gdata.youtube.com/feeds/api/videos?author="+username+"&orderby=published&v=2&alt=jsonc";
		this.mProgressDialog = progressDialog;
	}
	public GetYouTubeUserVideosTask(Activity activity, ProgressDialog progressDialog, String username, String category){
		this.username = username;
		this.activity = activity;
		this.category = category;
		this.requestUrl = "https://gdata.youtube.com/feeds/api/videos?author="+username+"&orderby=published&category="+ category +"&v=2&alt=jsonc";
		this.mProgressDialog = progressDialog;
	}

	public GetYouTubeUserVideosTask(Activity activity, ProgressDialog progressDialog, String username, String category, String maxResults){
		this.username = username;
		this.activity = activity;
		this.category = category;
		this.maxResults = maxResults;
		this.requestUrl = "https://gdata.youtube.com/feeds/api/videos?author="+username+"&orderby=published&category="+ category
				+"&max-results="+maxResults +"&v=2&alt=jsonc";
		this.mProgressDialog = progressDialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {

			String jsonString = Jsoup.connect(requestUrl).ignoreContentType(true).execute().body();
			// Create a JSON object that we can use from the String
			JSONObject json = new JSONObject(jsonString);

			// For further information about the syntax of this request and JSON-C
			// see the documentation on YouTube http://code.google.com/apis/youtube/2.0/developers_guide_jsonc.html

			// Get are search result items
			JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

			// Loop round our JSON list of videos creating Video objects to use within our app
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				// The title of the video
				String title = jsonObject.getString("title");
				// The url link back to YouTube, this checks if it has a mobile url
				// if it doesnt it gets the standard url
				String url;
				url = jsonObject.getJSONObject("player").getString("default");
				/*
				try {
					url = jsonObject.getJSONObject("player").getString("mobile");
				} catch (JSONException ignore) {
					url = jsonObject.getJSONObject("player").getString("default");
				}
				*/
				// A url to the thumbnail image of the video
				// We will use this later to get an image using a Custom ImageView
				// Found here http://blog.blundell-apps.com/imageview-with-loading-spinner/
				String thumbUrl = jsonObject.getJSONObject("thumbnail").getString("hqDefault");
				String datePublished = jsonObject.getString("uploaded");
				String duration = jsonObject.getString("duration");
				datePublished = ConvertDateTimeString(datePublished);
				duration = ConvertDurationString(duration);

				// Create the video object and add it to our list
				videos.add(new Video(title, url, thumbUrl,datePublished,duration));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String ConvertDurationString(String duration) {
		int durationInSeconds = Integer.parseInt(duration);
		String minutes;
		String seconds;
		int secondsInt;
		minutes = String.valueOf(durationInSeconds/60);
		secondsInt = durationInSeconds % 60;
		if(secondsInt > 9) {
			seconds = String.valueOf(durationInSeconds % 60);
		}
		else{
			seconds = "0" + String.valueOf(secondsInt);
		}

		return minutes + ":" + seconds;
	}

	private String ConvertDateTimeString(String datePublished) {
		String date;
		String time;
		date = datePublished.substring(0,10);
		time = datePublished.substring(11,19);

		return date+ " " + time;
	}

	@Override
	protected void onPostExecute(Void result) {

		VideosListView videosListView = (VideosListView) activity.findViewById(R.id.videosListView);
		videosListView.setVideos(videos);

		mProgressDialog.dismiss();
	}
}