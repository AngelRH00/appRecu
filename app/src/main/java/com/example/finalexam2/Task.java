package com.example.finalexam2;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private int id, mData;
    private String name, date, done;
    byte[] image;

    public Task(int id, byte[] image,String name, String date, String done) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
        this.done = done;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Task(Parcel in) {
        mData = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData);
    }

}
