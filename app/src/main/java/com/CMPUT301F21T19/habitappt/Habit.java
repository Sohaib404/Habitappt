package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Habit {
    private boolean isPrivate;
    private String title;
    private String reason;
    private long dateToStart;
    private ArrayList<Boolean> datesToDo;
    private ArrayList<HabitEvent> habitEvents;
    String id;

    private long score;

    public Habit(String title, String reason, long dateToStart, ArrayList<Boolean> datesToDo, String id, boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;
    }

    public Habit() {
        this.isPrivate = false;
        this.title = "";
        this.reason = "";
        this.dateToStart = GregorianCalendar.getInstance().getTimeInMillis();
        this.datesToDo = new ArrayList<>();
        for(int i=0;i<7;i++){
            datesToDo.add(false);
        }
        id = "-1";
    }

    public String getId(){
        return id;
    }

    public ArrayList<Boolean> getWeekly(){
        return datesToDo;
    }

    public Boolean getDateSelected(int i){
        if(i > datesToDo.size() || i < 0){
            return null;
        }
        return datesToDo.get(i);
    }

    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() || i < 0){
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

    public void setHabitEvents(ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents;
    }

    public ArrayList<HabitEvent> getHabitEvents(){
        return habitEvents;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * Calculates the score of a given habit to track progress of how often events are being completed
     * @return Percentage of of habit events completed over total number of events supposed to be done
     */

    public long calculateScore() {

        Date start_date = new Date(1000L * this.dateToStart);
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);

        Date current_date = new Date();

        long counter = 0; // Tracks total days habit is to be performed

        // iterates through all the dates from start to current
        // Checks to see if habit needs to be performed at each date
        // When loop if done, counter should have the total number of habit that should have been completed
        while (start_date.before(current_date)) {
            if (this.datesToDo.get(c.get(Calendar.DAY_OF_WEEK) - 1)) {
                counter += 1;
            }
            c.add(Calendar.DATE, 1);
            start_date = c.getTime();

        }

        // Checks if habit event is empty
        if (habitEvents == null) {
            return (long) -1;
        }

        return (long) (habitEvents.size() / counter) * 100;
    }

}

