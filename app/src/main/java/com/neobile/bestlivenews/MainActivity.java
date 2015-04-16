package com.neobile.bestlivenews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.neobile.bestlivenews.domain.Video;
import com.neobile.bestlivenews.task.GetYouTubeMultiUsersVideosTask;
import com.neobile.bestlivenews.task.GetYouTubeUserVideosTask;
import com.neobile.bestlivenews.widget.VideosListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity implements VideoClickListener {

    String url = "http://www.wsj.com/video/browse/top-news";
    String[] videoTitles;
    public ProgressDialog mProgressDialog;
    public List<Video> videos = new ArrayList<Video>();
    private VideosListView listView;

    private static String YT_KEY = "AIzaSyCl5CE_ZG-wy1QTqnrcLIVwoVrnVrPONAg";
    private String mCategory;

    //drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // VideoInfoDownloader videoInfoDownloader = new VideoInfoDownloader();
        //videoInfoDownloader.execute();

        listView = (VideosListView) findViewById(R.id.videosListView);
        listView.setOnVideoClickListener(this);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mCategory = "News";

        //GetYouTubeUserVideosTask getYouTubeUserVideosTask = new GetYouTubeUserVideosTask(MainActivity.this, mProgressDialog, "WSJDigitalNetwork", mCategory);
        //getYouTubeUserVideosTask.execute();

        final String[] drawerItemName = getResources().getStringArray(R.array.navigation_drawer_items_name_array);
        final String[] drawerItemCategory = getResources().getStringArray(R.array.navigation_drawer_items_category_array);
        getSupportActionBar().setTitle(drawerItemName[0]);
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                GetYouTubeMultiUsersVideosTask getYouTubeMultiUsersVideosTask;
                GetYouTubeUserVideosTask getYouTubeUserVideosTask;
                String[] usernames;
                String[]categories;
                switch(position)
                {
                    case 0:
                        usernames = new String[]{"CNN", "ABCNews",  "WSJDigitalNetwork"};
                        categories = new String[]{"News", "News", "News"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    case 3:
                        usernames = new String[]{"WSJDigitalNetwork","ENTV"};
                        categories = new String[]{"Entertainment", "Entertainment"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    case 4:
                        usernames = new String[]{ "WSJDigitalNetwork","Bloomberg" };
                        categories = new String[]{"Tech", "tech"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    case 5:
                        usernames = new String[]{"NationalGeographic","NationalGeographic","WSJDigitalNetwork"};
                        categories = new String[]{"People","Travel","Howto"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    case 1:
                        usernames = new String[]{"CBSSports"};
                        categories = new String[]{"Sports"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "40");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    case 2:
                        usernames = new String[]{"Bloomberg","CNNMoney","FinancialTimesVideos"};
                        categories = new String[]{"News","News","News"};
                        getYouTubeMultiUsersVideosTask =
                                new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                        getYouTubeMultiUsersVideosTask.execute();
                        break;
                    default:
                        break;
                }
                /*
                if (position == 0) {
                    String[] usernames = {"CNN", "ABCNews", "Bloomberg", "WSJDigitalNetwork"};
                    String[] categories = {"News", "News", "News", "News"};
                    GetYouTubeMultiUsersVideosTask getYouTubeMultiUsersVideosTask =
                            new GetYouTubeMultiUsersVideosTask(MainActivity.this, mProgressDialog, usernames, categories, "10");
                    getYouTubeMultiUsersVideosTask.execute();
                } else {
                    GetYouTubeUserVideosTask getYouTubeUserVideosTask = new GetYouTubeUserVideosTask(MainActivity.this, mProgressDialog, "WSJDigitalNetwork", drawerItemCategory[position]);
                    getYouTubeUserVideosTask.execute();
                }
                */
                // VideosListView videosListView = (VideosListView) findViewById(R.id.videosListView);
                // videosListView.setVideos(videos);
                mActivityTitle = drawerItemName[position];
                getSupportActionBar().setTitle(mActivityTitle);
                mDrawerLayout.closeDrawer(mDrawerList);

                AdView mAdView = (AdView) findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.performItemClick(mDrawerList.getAdapter().getView(0,null,null),0,mDrawerList.getItemIdAtPosition(0));
    }

    private void addDrawerItems() {
        String[] drawerItemName = getResources().getStringArray(R.array.navigation_drawer_items_name_array);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItemName);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Browse");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onVideoClicked (Video video){
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(video.getUrl()));
        startActivity(intent);
        */
            String youtubeID;
            String url = video.getUrl();
            int startIndex = url.indexOf("?v=") + 3;
            int endIndex = url.indexOf("&");
            if (endIndex > startIndex) {
                youtubeID = url.substring(startIndex, endIndex);
            } else {
                youtubeID = url.substring(startIndex);
            }
            Intent intent = com.google.android.youtube.player.YouTubeStandalonePlayer.createVideoIntent(this, YT_KEY, youtubeID, 0, true, false);
            startActivity(intent);

        }


        private class VideoInfoDownloader extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(MainActivity.this);
                //mProgressDialog.setTitle("Android Basic JSoup Tutorial");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Connect to the web site
                    Document document = Jsoup.connect(url).get();
                    // Get the html document title
                    // title = document.title();

                    Elements elements = document.select("a.video-thumb");
                    int i = 0;
                    for (Element element : elements) {

                        String VideoTitle = element.attr("title");
                        String DatePublished = element.select("span.date").text();
                        String ThumbnailUrl = element.select("img").attr("data-src-retina");
                        String URL = element.attr("href");
                        Video video = new Video(VideoTitle, URL, ThumbnailUrl, DatePublished);
                        videos.add(video);
                        i++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                VideosListView videosListView = (VideosListView) findViewById(R.id.videosListView);
                videosListView.setVideos(videos);

                mProgressDialog.dismiss();
            }
        }


    }
