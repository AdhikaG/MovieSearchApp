package com.first.movieapp.Database;

public class  Movie {

    private String Title;
    private String Year;
    private String Imdbid;
    private String Thumbnail;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImdbid() {
        return Imdbid;
    }

    public void setImdbid(String imdbid) {
        Imdbid = imdbid;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public Movie(String title, String year, String imdbid, String thumbnail) {
        Title = title;
        Year = year;
        Imdbid = imdbid;
        Thumbnail = thumbnail;
    }


}
