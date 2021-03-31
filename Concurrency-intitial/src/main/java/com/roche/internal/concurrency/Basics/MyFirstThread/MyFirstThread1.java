package com.roche.internal.concurrency.Basics.MyFirstThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * This is an example to show when Thread in New state, there will be two thread here, one is main thread and the one we are creating here
 * the thread scheduler is completing the main thread to completion and then running the second thread, when you run multiple
 * the thread scheduler will switch from one thread to other and does not maintain consistency
 */
public class MyFirstThread1 {

    private static final Logger logger = LoggerFactory.getLogger(MyFirstThread1.class);

    public static void main(String[] args) {
        logger.info("hello concurrency");
        Runnable task = new MyRunnable();
        Thread thread = new Thread(task);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(3);
            thread.interrupt();
        } catch (InterruptedException e) {
            logger.error("Interrupted while sleeping", e.getMessage());
        }
        logger.info("inside main");
    }
}
