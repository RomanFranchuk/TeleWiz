package com.arcticwolf.telewiz;


public class ShowInfo{


    private String title;
    private String time;
    private String description;
    private String imgUrl;
    private String dateShow;
    private String dateParse;
    private String showLink;
    private String channelLink;
    private String channelTitle;
    private int uniqueInt;
    private int posnt;
    private int logoForTablePath;
    private boolean checkForSaved;
    public ShowInfo(){

    }

    public ShowInfo(String title, String time, String description, String imgUrl, String dateShow, String dateParse, String showLink, String channelLink, String channelTitle, int uniqueInt){

        this.title = title;
        this.time = time;
        this.description = description;
        this.imgUrl = imgUrl;
        this.dateShow = dateShow;
        this.dateParse = dateParse;
        this.showLink = showLink;
        this.channelLink = channelLink;
        this.channelTitle = channelTitle;
        this.uniqueInt = uniqueInt;

    }

    public boolean isCheckForSaved() {
        return checkForSaved;
    }

    public void setCheckForSaved(boolean checkForSaved) {
        this.checkForSaved = checkForSaved;
    }

    public int getLogoForTablePath() {
        return logoForTablePath;
    }

    public void setLogoForTablePath(int logoForTablePath) {
        this.logoForTablePath = logoForTablePath;
    }

    public int getPosnt() {
        return posnt;
    }

    public void setPosnt(int posnt) {
        this.posnt = posnt;
    }

    public String getDateParse() {
        return dateParse;
    }

    public void setDateParse(String dateParse) {
        this.dateParse = dateParse;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDateShow() {
        return dateShow;
    }

    public void setDateShow(String dateShow) {
        this.dateShow = dateShow;
    }

    public String getShowLink() {
        return showLink;
    }

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

    public String getChannelLink(){return channelLink;}

    public void setChannelLink(String channelLink){this.channelLink = channelLink;}

    public String getChannelTitle(){
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle){
        this.channelTitle = channelTitle;
    }

    public int getUniqueInt(){ return uniqueInt;}

    public void setUniqueInt(int uniqueInt){
        this.uniqueInt = uniqueInt;

    }

    @Override
    public String toString() {
        return "ShowInfo{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", dateShow='" + dateShow + '\'' +
                ", dateParse='" + dateParse + '\'' +
                ", showLink='" + showLink + '\'' +
                ", channelLink='" + channelLink + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", uniqueInt=" + uniqueInt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowInfo showInfo = (ShowInfo) o;

        if (getUniqueInt() != showInfo.getUniqueInt()) return false;
        if (!getTitle().equals(showInfo.getTitle())) return false;
        if (!getTime().equals(showInfo.getTime())) return false;
        return getChannelTitle().equals(showInfo.getChannelTitle());

    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getTime().hashCode();
        result = 31 * result + getChannelTitle().hashCode();
        result = 31 * result + getUniqueInt();
        return result;
    }
}
