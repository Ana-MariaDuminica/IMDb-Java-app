package org.example;

public class Episode {
    private String episodeName;
    private String duration;
    private Episode() {

    }
    public Episode(String episodeName, String duration) {
        this.duration = duration;
        this.episodeName = episodeName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }
}
