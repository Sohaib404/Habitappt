package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Habit {
    private String title;
    private String reason;
    private long dateToStart;
    private ArrayList<Boolean> datesToDo;
    int id;

    public Habit(String title,String reason,long dateToStart,ArrayList<Boolean> datesToDo,int id){
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;
    }

    public Habit(){
        this.title = "New Habit";
        this.reason = "Habit Reason";
        this.dateToStart = GregorianCalendar.getInstance().getTimeInMillis();
        this.datesToDo = new ArrayList<>();
        for(int i=0;i<7;i++){
            datesToDo.add(false);
        }
        id = -1;
    }

    public int getId(){
        return id;
    }

    public ArrayList<Boolean> getWeekly(){
        return datesToDo;
    }

    public Boolean getDateSelected(int i){
        if(i > datesToDo.size()){
            return false;
        }

        return datesToDo.get(i);
    }

    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() && i < 0){
            return false;
        }

        datesToDo.set(i,b);

        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDateToStart() {
        return dateToStart;
    }

    public void setDateToStart(long dateToStart) {
        this.dateToStart = dateToStart;
    }
}

