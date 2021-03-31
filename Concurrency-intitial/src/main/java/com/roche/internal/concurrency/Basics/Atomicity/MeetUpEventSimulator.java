package com.roche.internal.concurrency.Basics.Atomicity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MeetUpEventSimulator {

    private static final Logger logger = LoggerFactory.getLogger(MeetUpEventSimulator.class);

    public static void main(String[] args) {
        MeetUpEvent jugBoston = new MeetUpEvent("The Boston Java User Group");

        Thread user1 = new Thread(new Runnable() {
            @Override
            public void run() {
                jugBoston.attending(4);
                logger.info(Thread.currentThread().getName() + " : " + jugBoston.getCount());
            }
        });

        Thread user2 = new Thread(() -> {
            jugBoston.attending(3);
            logger.info(Thread.currentThread().getName() + " : " + jugBoston.getCount());
            jugBoston.notAttending(3);
            logger.info(Thread.currentThread().getName() + " : " + jugBoston.getCount());
        });
        Thread user3 = new Thread(() -> {
            jugBoston.attending(1);
            logger.info(Thread.currentThread().getName() + " : " + jugBoston.getCount());
        });

        user1.setName("User 1");
        user2.setName("User 2");
        user3.setName("User 3");

        user1.start();
        sleep(10);
        user2.start();
        sleep(2);
        user3.start();
        sleep(2);
        logger.info("Total attending : " + jugBoston.getCount());
    }

    private static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            logger.error("Interrupted when thread is sleeping", e);
            throw new RuntimeException();
        }
    }
}

