package com.roche.internal.concurrency.Basics.ThreadCooperationUsingWaitAndNotify;

import com.roche.internal.concurrency.Basics.Commons.Download;
import com.roche.internal.concurrency.Basics.Commons.Weblink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * For N web links, this approach creates 2 * N threads.
 * <p>
 * Benefit: Better coordination of control flow between threads. Relinquishes lock on wait()!
 * <p>
 * Note: htmlPage is NOT declared volatile in Weblink as unlock on a monitor
 * 'happens before' every subsequent lock on that same monitor.
 * <p>
 * Limitation:
 * Solves task cooperation in a low-level fashion. Josh Bloch says it is like
 * programming in "concurrency assembly language"
 * Synchronized blocks are needed
 */

public class WaitNotifyIndexer {

    private static final Logger logger = LoggerFactory.getLogger(WaitNotifyIndexer.class);
    private Deque<Weblink> queue = new ArrayDeque<>();

    public void go() {
        while (queue.size() > 0) {
            Weblink weblink = queue.remove();
            Thread downloaderThread = new Thread(new Download(weblink));
            Thread indexerThread = new Thread(new Index(weblink));

            downloaderThread.start();
            indexerThread.start();
        }
    }

    public void add(Weblink link) {
        queue.add(link);
    }

    public static void main(String[] args) {
        WaitNotifyIndexer waitNotifyIndexer = new WaitNotifyIndexer();
        Weblink weblink = new Weblink();
        waitNotifyIndexer.add(weblink.createWeblink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com"));
        waitNotifyIndexer.add(weblink.createWeblink(2001, "How do I import a pre-existing Java project into Eclipse and get up and running?", "http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running", "http://www.stackoverflow.com"));
        waitNotifyIndexer.add(weblink.createWeblink(2002, "Interface vs Abstract Class", "http://mindprod.com/jgloss/interfacevsabstract.html", "http://mindprod.com"));
        waitNotifyIndexer.add(weblink.createWeblink(2004, "Virtual Hosting and Tomcat", "http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html", "http://tomcat.apache.org"));
        waitNotifyIndexer.go();
    }
}
