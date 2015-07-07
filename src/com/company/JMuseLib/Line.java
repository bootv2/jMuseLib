package com.company.JMuseLib;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by TTT on 7/7/2015.
 */
public class Line
{
    private String oscLine = "";
    private byte[] oscBytes;
    private ArrayList<String> dataPath = new ArrayList<String>();
    private Long measurementMoment;
    Calendar cal = Calendar.getInstance();
    private float floats = 0;
    //private int ints = null;

    public float getFloat()
    {
        return floats;
    }
    public long getTime()
    {
        return measurementMoment;
    }

    public Line(String oscLine) {
        measurementMoment = cal.getTime().getTime();
        this.oscLine = oscLine;
        seperateLine();
    }



    private void seperateLine()
    {
        int lastIndex = 0;
        for(int i = 0; i < oscLine.length(); i++)
        {
            if(oscLine.charAt(i) == '/')
                dataPath.add(oscLine.substring(lastIndex, i));
            else if(oscLine.charAt(i) == ',')
            {
                dataPath.add(oscLine.substring(lastIndex, i));
                lastIndex = i;
                break;
            }
        }
        intrepetValue(lastIndex);

    }

    private void intrepetValue(int index)
    {
        int floatBytes = 0;
        boolean stepOut = false;
        int doubleBytes = 0;
        int dataIndex = 0;
        for(int i = index; i < oscLine.length(); i++)
        {
            switch(oscLine.charAt(i))
            {
                case 'f':
                    floatBytes++;
                    stepOut = true;
                    dataIndex = i+3;
                    break;
                case ' ':
                    stepOut = true;
                    dataIndex = i+3;
                    break;
                default:
                    break;
            }
            if(stepOut) break;
        }
        switch(oscLine.length() - dataIndex)
        {
            case 1:
                oscBytes = new byte[]{0x00, 0x00, 0x00, (byte)oscLine.charAt(dataIndex)};
                break;
            case 2:
                oscBytes = new byte[]{0x00, 0x00, (byte)oscLine.charAt(dataIndex + 1), (byte)oscLine.charAt(dataIndex)};
                break;
            case 3:
                oscBytes = new byte[]{0x00, (byte)oscLine.charAt(dataIndex + 2), (byte)oscLine.charAt(dataIndex + 1), (byte)oscLine.charAt(dataIndex)};
                break;
            case 4:
                oscBytes = new byte[]{(byte)oscLine.charAt(dataIndex + 3), (byte)oscLine.charAt(dataIndex + 2), (byte)oscLine.charAt(dataIndex + 1), (byte)oscLine.charAt(dataIndex)};
                break;
            default:
                oscBytes = new byte[]{0x00, 0x00, 0x00, 0x00};
                System.out.println("Error reading data, zero bytes used.");
                break;
        }
        //oscBytes = new byte[floatBytes]{(byte)oscLine.};
        floats = ByteBuffer.wrap(oscBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        System.out.println(floats);
    }


}
