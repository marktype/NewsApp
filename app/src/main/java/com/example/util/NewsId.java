package com.example.util;

import java.util.ArrayList;

/**
 * Created by FJ on 2015/12/10.
 */
public class NewsId {

    private String docid;
    private String title;
    private String imgsrc;
    private ArrayList<AdsData> ads;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AdsData> getAds() {
        return ads;
    }

    public void setAds(ArrayList<AdsData> ads) {
        this.ads = ads;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
