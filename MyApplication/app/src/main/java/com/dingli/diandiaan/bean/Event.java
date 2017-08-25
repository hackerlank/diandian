package com.dingli.diandiaan.bean;

/**
 * Created by lpf on 2014/9/1 0001.
 */
public class Event {

    public static final int EVENT_SOUSUO = 1;
    public static final int EVENT_SOUSUOCall = 2;
    public static final int EVENT_GENDER = 3;
    public static final int EVENT_SHAKE_INFO = 4;

    public int event;
    public String string;

    public Event(int event, String string) {
        this.event = event;
        this.string = string;
    }

}
