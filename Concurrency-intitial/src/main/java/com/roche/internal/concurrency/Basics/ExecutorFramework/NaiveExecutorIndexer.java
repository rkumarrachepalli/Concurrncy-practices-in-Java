package com.roche.internal.concurrency.Basics.ExecutorFramework;

import com.roche.internal.concurrency.Basics.Commons.Download;
import com.roche.internal.concurrency.Basics.Commons.Weblink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NaiveExecutorIndexer {

    private static final Logger logger = LoggerFactory.getLogger(NaiveExecutorIndexer.class);
    private Deque<Weblink> queue = new ArrayDeque<>();

    Executor downloadExecutor = Executors.newFixedThreadPool(2);
    Executor indexerExecutor = Executors.newFixedThreadPool(2);

    public void go() {
        while (queue.size() > 0) {
            Weblink weblink = queue.remove();
            downloadExecutor.execute(new Download(weblink));
            indexerExecutor.execute(new Index(weblink));
        }
    }

    public void add(Weblink link) {
        queue.add(link);
    }

    public static void main(String[] args) {
        NaiveExecutorIndexer naiveExecutorIndexer = new NaiveExecutorIndexer();
        Weblink weblink = new Weblink();
        naiveExecutorIndexer.add(weblink.createWeblink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com"));
        naiveExecutorIndexer.add(weblink.createWeblink(2001, "How do I import a pre-existing Java project into Eclipse and get up and running?", "http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running", "http://www.stackoverflow.com"));
        naiveExecutorIndexer.add(weblink.createWeblink(2002, "Interface vs Abstract Class", "http://mindprod.com/jgloss/interfacevsabstract.html", "http://mindprod.com"));
        naiveExecutorIndexer.add(weblink.createWeblink(2004, "Virtual Hosting and Tomcat", "http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html", "http://tomcat.apache.org"));
        naiveExecutorIndexer.go();
    }
}
