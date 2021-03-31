package com.roche.internal.concurrency.Basics.ThreadPriority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DataAggregator implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataAggregator.class);
    
    public void run() {
        for (int i = 1; i <= 10; i++) {
            logger.info(Thread.currentThread().getName());
        }
    }
}
