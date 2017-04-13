package com.arcticwolf.telewiz;


public class Channel {

    private int id;
    private String title;

    public Channel(){

    }

    public Channel(int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (getId() != channel.getId()) return false;
        return getTitle().equals(channel.getTitle());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getTitle().hashCode();
        return result;
    }
}

