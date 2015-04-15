package com.neobile.bestlivenews.domain;

import java.io.Serializable;

/**
 * This is a representation of a users video off YouTube
 *
 * @author paul.blundell
 */
public class Video implements Serializable {
    // The title of the video
    private String title;
    // A link to the video on youtube
    private String url;
    // A link to a still image of the youtube video
    private String thumbUrl;

    private String datePublished;

    private String duration;

    public Video(String title, String url, String thumbUrl, String datePublished) {
        super();
        this.title = title;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.datePublished = datePublished;
    }

    public Video(String title, String url, String thumbUrl, String datePublished, String duration) {
        super();
        this.title = title;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.datePublished = datePublished;
        this.duration = duration;
    }

    public Video(String title, String url, String thumbUrl) {
        super();
        this.title = title;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.datePublished = "";
    }
    /**
     * @return the title of the video
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the url to this video on youtube
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the thumbUrl of a still image representation of this video
     */
    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getDuration(){
        return duration;
    }

}