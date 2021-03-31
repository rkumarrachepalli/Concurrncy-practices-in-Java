package com.roche.internal.concurrency.Basics.MyFirstThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MyRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MyRunnable.class);

    @Override
    public void run() {
        logger.info("inside run");
        try {
            TimeUnit.SECONDS.sleep(9);

        } catch (InterruptedException e) {
            logger.info("Interrupted!!");
        }
        function();
    }

    private void function() {
        logger.info("inside function");
        more();
    }

    private void more() {
        logger.info("inside more");
    }
}

