package com.roche.internal.concurrency.Basics.RaceCondition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BankAccount implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BankAccount.class);
    private int balance;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void run() {
        makeWithdrawal(75);
        if (balance < 0) {
            logger.info("Money overdrawn!!!");
        }
    }

    private synchronized void makeWithdrawal(int amount) {
        if (balance >= amount) {
            logger.info(Thread.currentThread().getName() + " is about to withdraw ...");
            balance -= amount;
            logger.info(Thread.currentThread().getName() + " has withdrawn " + amount + " bucks");
        } else {
            logger.info("Sorry, not enough balance for " + Thread.currentThread().getName());
        }
    }
}
