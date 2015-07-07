package com.company.JMuseLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by TTT on 6/27/2015.
 */
public class OSCStreamHandler implements Runnable
{
    private Queue<String> lineQueue;

    public Queue<String> getOutputQueue() {
        return outputQueue;
    }

    public void setOutputQueue(Queue<String> outputQueue) {
        this.outputQueue = outputQueue;
    }

    private Queue<String> outputQueue;
    private static final int CONN_PORT = 5555;
    private InputStream dataStream;

    public OutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    private OutputStream outStream;
    private OSCServer oscServer;
    private boolean isFirstLine = true;
    public OSCStreamHandler()
    {
        //lineQueue = new LinkedList<String>();
        try {
            oscServer = new OSCServer(CONN_PORT);

            System.out.println("oscServer server created...");

            //before now, the oscServer thread was started here.

            //telnetThread.

            //System.out.println("oscServer server running!");

            //dataStream = oscServer.getInputStream();

            //System.out.println("DataStream set.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCommandQueue(Queue<String> cq)
    {
        lineQueue = cq;
    }

    public void setInputStream(InputStream i)
    {
        dataStream = i;
    }

    public OSCServer getOscServer()
    {
        return oscServer;
    }

    public Queue<String> getLineQueue()
    {
        return lineQueue;
    }

    public String getNextLine()
    {
        if(lineQueue.isEmpty())     return null;
        else                        return lineQueue.poll();
    }

    private void scanInput()
    {
        String curLine      = "";
        int lineChars       = 0;
        if (dataStream != null) {
            try {
                if (dataStream.available() > 0) {
                    lineChars = dataStream.available();
                    for (int i = 0; i < lineChars; i++) {
                        //and add the char to the current line String
                        curLine += (char) dataStream.read();
                    }
                    if (!isFirstLine) {
                        lineQueue.add(curLine);
                        //System.out.println("a new line should be added to the line queue.");
                        curLine = "";
                        lineChars = 0;
                    } else {
                        curLine = "";
                        lineChars = 0;
                        isFirstLine = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                System.out.println("Setting data stream");
                dataStream = oscServer.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendOutput()
    {
        String line = "";
        while(true)
        {
            if(outputQueue.isEmpty()) break;
            else
            {
                line = outputQueue.poll();
                try {
                    outStream.write(line.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //outStream.write();
    }

    @Override
    public void run() {


        while(true) {
            scanInput();
            sendOutput();
        }
    }
}
