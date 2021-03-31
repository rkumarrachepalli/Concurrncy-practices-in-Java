package com.roche.internal.concurrency.Basics.ThreadPriority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EmailCampaign implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EmailCampaign.class);
    
    public void run() {
        for (int i = 1; i <= 10; i++) {
            logger.info(Thread.currentThread().getName());
            if (i == 5) {
                // Hint to scheduler that thread is willing to yield its current use of CPU
                Thread.currentThread().yield();
            }
        }
    }
}
