package com.roche.internal.concurrency.Basics.Atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class MeetUpEvent {

    private String name;
    private AtomicInteger count = new AtomicInteger(1); // 1 for organizer

    public MeetUpEvent(String name) {
        this.name = name;
    }

    public void attending(int guestCount) {
        if (guestCount == 1) {
            count.incrementAndGet();
        } else {
            count.addAndGet(guestCount);
        }
    }

    public void notAttending(int guestCount) {
        if (guestCount == 1) {
            count.decrementAndGet();
        } else {

            boolean updated = false;

            while (!updated) {
                int currentCount = count.get();
                int newCount = currentCount - guestCount;
                updated = count.compareAndSet(currentCount, newCount);
            }
        }
    }

    public int getCount() {
        return count.get();
    }
}
