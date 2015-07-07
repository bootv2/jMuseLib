/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.company.JMuseLib;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/***
 * Simple TCP server.
 * Waits for connections on a TCP port in a separate thread.
 *

 * @author TTT BBB
 ***/
public class OSCServer implements Runnable
{
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    Thread listener = null;
    InputStream incomingData;

    /***
     * test of client-driven subnegotiation.
     *


     * @param port - server port on which to listen.
     ***/
    public OSCServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);

        listener = new Thread (this);

        listener.start();
    }

    /***
     * Run for the thread. Waits for new connections
     ***/
    public void run()
    {
        while(true)
        {
            try
            {
                clientSocket = serverSocket.accept();
                synchronized (clientSocket)
                {
                    try
                    {
                        System.out.println("Connection accepted... waiting for commands...");
                        //clientSocket.wait();
                        //System.out.println("[T] the socket associated with the telnet client is now waiting");
                        break;
                    }
                    catch (Exception e)
                    {
                        System.err.println("Exception in wait, "+ e.getMessage());
                    }
                    stop();
                }
            }
            catch (IOException e)
            {
                System.out.println("Error accepting connection...");
                e.printStackTrace();
                System.out.println("Stopping server...");
                stop();
            }
        }
        System.out.println("[!]Server connected to client Success!!!");
        return;
    }

    /***
     * Stop the listener thread
     ***/
    public void stop()
    {
        listener.interrupt();
        try
        {
            serverSocket.close();
        }
        catch (Exception e)
        {
            System.err.println("Exception in close, "+ e.getMessage());
        }
    }


    /***
     * Disconnects the client socket
     ***/
    public void disconnect()
    {
        synchronized (clientSocket)
        {
            try
            {
                clientSocket.notify();
            }
            catch (Exception e)
            {
                System.err.println("Exception in notify, "+ e.getMessage());
            }
        }
    }

    /***
     * Gets the input stream for the client socket
     ***/
    public InputStream getInputStream() throws IOException
    {
        if(clientSocket != null)
        {
            return(clientSocket.getInputStream());
        }
        else
        {
            return(null);
        }
    }

    /***
     * Gets the output stream for the client socket
     ***/
    public OutputStream getOutputStream() throws IOException
    {
        if(clientSocket != null)
        {
            return(clientSocket.getOutputStream());
        }
        else
        {
            return(null);
        }
    }
}