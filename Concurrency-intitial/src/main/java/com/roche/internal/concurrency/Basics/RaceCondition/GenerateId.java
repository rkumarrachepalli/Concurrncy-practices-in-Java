package com.roche.internal.concurrency.Basics.RaceCondition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateId {

    private static final Logger logger = LoggerFactory.getLogger(GenerateId.class);

    public static void main(String[] args) {
        logger.info("hello concurrency");
        Runnable task = new GenIdRaceCondition();
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.setName("A");
        thread2.setName("B");
        thread1.start();
        thread2.start();
        logger.info(Thread.currentThread().getName() + "inside main");
    }
}
