package com.medsys.medsysapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class Error {
    public int id;
    public String title;
    public String description;
    public Date date;
    public Time time;
    public Boolean resolved;

    public Error(Map<String, Object> data) throws ParseException{
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("title")) {
            this.title = (String) data.get("title");
        }
        if(data.containsKey("description")) {
            this.description = (String) data.get("description");
        }
        if(data.containsKey("date")) {
            if(data.get("date") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.date = new java.sql.Date(dateFormat.parse((String) data.get("date")).getTime());
            } else {
                this.date = (java.sql.Date) data.get("date");
            }
        }
        if(data.containsKey("time")) {
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if(data.get("time") instanceof String) {
                try {
                    this.time = new Time(timeFormat.parse((String) data.get("time")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                this.time = new Time(timeFormat.parse(data.get("time").toString()).getTime());
            }
        }
        if(data.containsKey("resolved")) {
            this.resolved = (Boolean) data.get("resolved");
        }
    }

}
