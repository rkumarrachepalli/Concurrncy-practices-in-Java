package com.roche.internal.concurrency.Basics.RaceCondition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenIdRaceCondition implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GenIdRaceCondition.class);
    private int id;

    public void run() {
        logger.info(Thread.currentThread().getName() + "inside run");
        getNewId();
        logger.info(Thread.currentThread().getName() + id);
    }

    private synchronized long getNewId() {
        logger.info(Thread.currentThread().getName() + "inside getNewId");
        id++;
        logger.info(Thread.currentThread().getName() + "here" + id);
        return id;
    }
}
