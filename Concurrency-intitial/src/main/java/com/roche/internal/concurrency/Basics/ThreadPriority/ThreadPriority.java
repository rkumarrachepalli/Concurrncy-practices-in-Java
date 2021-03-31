package com.roche.internal.concurrency.Basics.ThreadPriority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPriority {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPriority.class);
    
    public static void main(String[] args) {
        logger.info(Thread.currentThread().getName());

        Thread t1 = new Thread(new EmailCampaign());
        Thread t2 = new Thread(new DataAggregator());

        t1.setName("EmailCampaign");
        t2.setName("DataAggregator");

        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);

        t1.start();
        t2.start();

        try {
            // main thread is telling i want to  suspended until t2 DIES or completes its execution
            t2.join();
        } catch (InterruptedException e) {
            logger.error("Error while main thread is waiting to sleep or while sleeping");
        }
        logger.info("Inside main ... ");
    }
}

