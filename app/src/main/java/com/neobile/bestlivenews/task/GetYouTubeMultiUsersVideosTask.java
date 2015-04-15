package com.neobile.bestlivenews.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.neobile.bestlivenews.R;
import com.neobile.bestlivenews.domain.Video;
import com.neobile.bestlivenews.widget.VideosListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetYouTubeMultiUsersVideosTask extends AsyncTask<Void, Void, Void> {

    private String[] usernames;
    private String maxResults;
    private String[] categories;
    private String[] requestUrl;
    private Activity activity;
    public List<Video> videos = new ArrayList<Video>();
    private ProgressDialog mProgressDialog;

    public GetYouTubeMultiUsersVideosTask(Activity activity, ProgressDialog progressDialog, String[] usernames, String[] categories, String maxResults){
        this.usernames = usernames;
        this.activity = activity;
        this.categories = categories;
        this.maxResults = maxResults;
        this.requestUrl = new String[usernames.length];
        int i=0;
        for(String username:usernames) {
            this.requestUrl[i] = "https://gdata.youtube.com/feeds/api/videos?author=" + usernames[i] + "&orderby=published&category=" + categories[i]
                    + "&max-results=" + maxResults + "&v=2&alt=jsonc";
            i++;
        }
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
            for(String mUrl:requestUrl) {
                String jsonString = Jsoup.connect(mUrl).ignoreContentType(true).execute().body();
                // Create a JSON object that we can use from the String
                JSONObject json = new JSONObject(jsonString);

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

                    String thumbUrl = jsonObject.getJSONObject("thumbnail").getString("hqDefault");
                    String datePublished = jsonObject.getString("uploaded");
                    String duration = jsonObject.getString("duration");
                    datePublished = ConvertDateTimeString(datePublished);
                    duration = ConvertDurationString(duration);
                    Video video = new Video(title, url, thumbUrl, datePublished, duration);
                    // Create the video object and add it to our list
                    int index = getVideoLocation(video);
                    videos.add(index,video);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getVideoLocation(Video video) {
        int index = 0;
        if(videos.size()==0) {
            return 0;
        }
        else
        {
            for(Video addedVideo:videos)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date addedVideoPublishedDate = null;
                Date newVideoPublishedDate = null;
                try {
                    addedVideoPublishedDate = sdf.parse(addedVideo.getDatePublished());
                    newVideoPublishedDate = sdf.parse(video.getDatePublished());
                    if(newVideoPublishedDate.before(addedVideoPublishedDate)){
                        index++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return index;
        }
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