package com.example.mynotepade.entity;

public class NotesInfo {

    public String content;
    //    public String path;
    public String video;
    public String _id;
    public String time;

    public NotesInfo() {

    }

    public NotesInfo(String content, String video, String _id, String time) {
        this.content = content;
        this.video = video;
        this._id = _id;
        this.time = time;
    }

}
