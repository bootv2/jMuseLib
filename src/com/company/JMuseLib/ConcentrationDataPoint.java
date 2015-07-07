package com.company.JMuseLib;

import org.json.*;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by TTT on 6/27/2015.
 */
public class ConcentrationDataPoint
{
    private long time = -1;
    private double concentrationValue = -1.d;
    private JSONObject myObject;
    public static Calendar calendar = Calendar.getInstance();


    public ConcentrationDataPoint(String json) throws JSONException {
        myObject = new JSONObject(json);

        time = myObject.getLong("time");
        concentrationValue = myObject.getDouble("value");
    }

    public ConcentrationDataPoint() {
        //for debugging only
    }

    public JSONObject asJSON()
    {
        try {
            return new JSONObject("{\"value\":" + concentrationValue + ", \"time\":" + time + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getConcentrationValue() {
        return concentrationValue;
    }

    public void setConcentrationValue(double concentrationValue) {
        this.concentrationValue = concentrationValue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static Comparator<ConcentrationDataPoint> compareDate = new Comparator<ConcentrationDataPoint>() {
        @Override
        public int compare(ConcentrationDataPoint p1, ConcentrationDataPoint p2) {
            return p1.time < p2.time ? -1 : 1;
        }
    };

    public static Comparator<ConcentrationDataPoint> compareConcentration = new Comparator<ConcentrationDataPoint>() {
        @Override
        public int compare(ConcentrationDataPoint p1, ConcentrationDataPoint p2) {
            return p1.getConcentrationValue() < p2.getConcentrationValue() ? -1 : 1;
        }
    };

    public static Comparator<ConcentrationDataPoint> compareTimeOfDay = new Comparator<ConcentrationDataPoint>() {
        public int compare(ConcentrationDataPoint p1, ConcentrationDataPoint p2) {
            calendar.setTimeInMillis(p1.time);
            int seconds1 = calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
            calendar.setTimeInMillis(p2.time);
            int seconds2 = calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
            return seconds1 < seconds2 ? 1 : -1;
        }
    };
}

