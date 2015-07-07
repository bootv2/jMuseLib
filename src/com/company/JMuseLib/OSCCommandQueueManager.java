package com.company.JMuseLib;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by TTT on 6/27/2015.
 */
public class OSCCommandQueueManager
{
    private Queue<String> inputCommandQueue;
    private Queue<String> outputCommandQueue;
    //private Thread tTelnet;
    private Thread tOSCStreamHandler;
    private OSCStreamHandler jStream;
    private OSCServer telInstance;

    public OSCCommandQueueManager()
    {
        jStream = new OSCStreamHandler();
        telInstance = jStream.getOscServer();

        System.out.println("[S]About to start telnet thread");


        System.out.println("[Y]OSCServer thread started!");
        telInstance.run();
        System.out.println("[Y]OSCServer Thread finished!");

        System.out.println("[Y] OSCServer thread started without problems!\nStarting OSCStreamHandler thread...");
        tOSCStreamHandler = new Thread(jStream);
        System.out.println("OSCStreamHandler Started!");
        try {
            jStream.setInputStream(telInstance.getInputStream());
            jStream.setOutStream(telInstance.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("OSCStreamHandlers dataStreams have been set!");

        inputCommandQueue = new LinkedList<String>();

        jStream.setCommandQueue(inputCommandQueue);

        System.out.println("OSCStreamHandler commandQueue set");

        outputCommandQueue = new LinkedList<String>();

        jStream.setOutputQueue(outputCommandQueue);

        System.out.println("Starting OSCStreamHandler thread");
        tOSCStreamHandler.start();
    }

    public String readNext()
    {
        //synchQueues();
        synchronized (tOSCStreamHandler) {
            if (inputCommandQueue.isEmpty()) return null;
            else return inputCommandQueue.poll();
        }
    }

    public void sendCommand(String command)
    {
        synchronized (tOSCStreamHandler) {
            outputCommandQueue.add(command);
        }
    }

    public void clear() {
        inputCommandQueue.clear();
        outputCommandQueue.clear();
    }
}
