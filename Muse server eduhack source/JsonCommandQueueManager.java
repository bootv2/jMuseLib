package com.company;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by TTT on 6/27/2015.
 */
public class JsonCommandQueueManager
{
    private Queue<String> inputCommandQueue;
    private Queue<String> outputCommandQueue;
    //private Thread tTelnet;
    private Thread tJsonStream;
    private JsonStream jStream;
    private Telnet telInstance;

    public JsonCommandQueueManager()
    {
        jStream = new JsonStream();
        telInstance = jStream.getTelnet();

        System.out.println("[S]About to start telnet thread");


        System.out.println("[Y]Telnet thread started!");
        telInstance.run();
        System.out.println("[Y]Telnet Thread finished!");

        System.out.println("[Y] Telnet thread started without problems!\nStarting JSONStream thread...");
        tJsonStream = new Thread(jStream);
        System.out.println("JSONStream Started!");
        try {
            jStream.setInputStream(telInstance.getInputStream());
            jStream.setOutStream(telInstance.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSONStreams dataStreams have been set!");

        inputCommandQueue = new LinkedList<String>();

        jStream.setCommandQueue(inputCommandQueue);

        System.out.println("JSONStream commandQueue set");

        outputCommandQueue = new LinkedList<String>();

        jStream.setOutputQueue(outputCommandQueue);

        System.out.println("Starting JSONStream thread");
        tJsonStream.start();
    }

    public String readNext()
    {
        //synchQueues();
        synchronized (tJsonStream) {
            if (inputCommandQueue.isEmpty()) return null;
            else return inputCommandQueue.poll();
        }
    }

    public void sendCommand(String command)
    {
        synchronized (tJsonStream) {
            outputCommandQueue.add(command);
        }
    }

    public void clear() {
        inputCommandQueue.clear();
        outputCommandQueue.clear();
    }
}
