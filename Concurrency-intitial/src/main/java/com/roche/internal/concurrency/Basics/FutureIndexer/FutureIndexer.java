package com.roche.internal.concurrency.Basics.FutureIndexer;

import com.roche.internal.concurrency.Basics.Commons.Weblink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Uses only 4 threads. Downloader now implements Callable.
 * <p>
 * Note: htmlPage is NOT declared volatile in Weblink due to the below
 * memory visibility guarantee
 * <p>
 * Memory Visibility: Actions in a thread prior to the submission of a
 * Runnable or Callable task to an ExecutorService happen-before any
 * actions taken by that task, which in turn happen-before the result
 * is retrieved via Future.get().
 * <p>
 * Benefit:
 * Executor framework is used. So, we get thread management benefit!
 * Better coordination of control flow between threads. Cleaner than wait-notify
 * <p>
 * Limitation:
 * Thread blocking can be an issue
 */

public class FutureIndexer {

    private static final Logger logger = LoggerFactory.getLogger(FutureIndexer.class);
    private Deque<Weblink> queue = new ArrayDeque<>();

    // Executors
    ExecutorService downloadExecutor = Executors.newFixedThreadPool(2);
    ExecutorService indexerExecutor = Executors.newFixedThreadPool(2);

    public void go() {
        List<Future<Weblink>> futures = new ArrayList<>();

        while (queue.size() > 0) {
            Weblink weblink = queue.remove();
            futures.add(downloadExecutor.submit(new Downloader<>(weblink)));
        }

        for (Future<Weblink> future : futures) {
            try {
                indexerExecutor.execute(new Index(future.get()));
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while indexing");
            }
        }

        downloadExecutor.shutdown();
        indexerExecutor.shutdown();
    }

    public void add(Weblink link) {
        queue.add(link);
    }

    public static void main(String[] args) {
        FutureIndexer futureIndexer = new FutureIndexer();
        Weblink weblink = new Weblink();
        futureIndexer.add(weblink.createWeblink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com"));
        futureIndexer.add(weblink.createWeblink(2001, "How do I import a pre-existing Java project into Eclipse and get up and running?", "http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running", "http://www.stackoverflow.com"));
        futureIndexer.add(weblink.createWeblink(2002, "Interface vs Abstract Class", "http://mindprod.com/jgloss/interfacevsabstract.html", "http://mindprod.com"));
        futureIndexer.add(weblink.createWeblink(2004, "Virtual Hosting and Tomcat", "http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html", "http://tomcat.apache.org"));
        futureIndexer.go();
    }
}
