package com.roche.internal.concurrency.Basics.MyFirstThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* This is tight coupling so we should not use it */

public class MyFirstThread2 extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(MyFirstThread2.class);

    public void run() {
        logger.info("inside run");
        function();
    }

    private void function() {
        logger.info("inside function");
        more();
    }

    private void more() {
        logger.info("inside more");
    }

    public static void main(String[] args) {
        Thread myThread = new MyFirstThread2();
        myThread.start();
        logger.info("inside main");
    }
}
