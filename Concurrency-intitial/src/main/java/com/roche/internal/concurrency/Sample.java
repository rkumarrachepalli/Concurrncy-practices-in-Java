package com.roche.internal.concurrency;

import java.lang.Runnable;
import java.util.concurrent.TimeUnit;


public class Sample {

    public static void main(String[] args) {
        System.out.println("hello concurrency");
//        Runnable task = new MyRunnable();
        /*
        Thread in New state, there will be two thread here, one is main thread and the one we are creating here
the thread scheduler is completing the main thread to completion and then running the second thread, when you run multiple
the thread scheduler will switch from one thread to other and does not maintain consistency
*/
//        Thread thread1 = new Thread(task);
//        Thread thread2 = new Thread(task);
//        thread1.setName("A");
//        thread2.setName("B");
//        thread1.start();
//        thread2.start();
////        try {
////            Thread.sleep(3000);      // here main thread will sleep for 3 sec
////            TimeUnit.SECONDS.sleep(3); // another way of using sleep
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        System.out.println(Thread.currentThread().getName() + "inside main");
        String a = "abc.txt";
        String fileName = a.substring(0, a.lastIndexOf('.'));
        String extension = a.substring(a.lastIndexOf('.'));
        System.out.println(fileName);
        System.out.println(extension);

    }
}

class MyRunnable implements Runnable {
    int id;
    public void run() {
        System.out.println(Thread.currentThread().getName() + "inside run");
        getNewId();
        System.out.println(Thread.currentThread().getName() + id);
    }

    private synchronized long getNewId() {
        System.out.println(Thread.currentThread().getName() + "inside getNewId");
        id++;
        System.out.println(Thread.currentThread().getName() + "here" + id);
        return id;
    }

//    private void more() {
//        System.out.println("inside more");
//    }

}
