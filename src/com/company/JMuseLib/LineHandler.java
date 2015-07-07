package com.company.JMuseLib;

import org.json.JSONException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTT on 7/7/2015.
 */
public class LineHandler
{
    private String next;

    public List<Line> getDataList() {
        return dataList;
    }

    public void clearDataList()
    {
        dataList.clear();
    }

    private List<Line> dataList = new ArrayList<Line>();
    private Line currentDataPoint;
    public void runConcentrationListener()
    {
        System.out.println("[i]Attempting to connect to database before starting server.");
        //DatabaseController dbController = new DatabaseController();

        System.out.println("Attempting to start synched threaded telnet server");
        OSCCommandQueueManager q = new OSCCommandQueueManager();

        synchronized (this) {
            try {
                this.wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //q.sendCommand("TEST!!!!!!!!!!!!!");


            System.out.println("CommandQueue created");
            while (true) {
                next = q.readNext();
                if (next != null) {
                    //System.out.println("Line: " + next);




                        currentDataPoint = new Line(next);
                        dataList.add(currentDataPoint);





                } else {
                    try {
                        this.wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }}
        }
    }

}
