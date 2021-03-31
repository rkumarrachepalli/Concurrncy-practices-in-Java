package com.roche.internal.concurrency.Basics.ThreadCooperationUsingWaitAndNotify;

import com.roche.internal.concurrency.Basics.Commons.Indexer;
import com.roche.internal.concurrency.Basics.Commons.Weblink;
import com.roche.internal.concurrency.Basics.ThreadPriority.ThreadPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Index extends Indexer {

    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    private Weblink weblink;

    Index(Weblink weblink) {
        this.weblink = weblink;
    }

    public void run() {
        String htmlPage = weblink.getHtmlPage();
        synchronized (weblink) {
            while (htmlPage == null) {
                try {
                    logger.info(weblink.getId() + " not yet downloaded!");
                    weblink.wait();
                    logger.info(weblink.getId() + "awakened!");
                    htmlPage = weblink.getHtmlPage();
                } catch (InterruptedException e) {
                    logger.error("Error while thread trying to get html page");
                }
            }
        }
    }

    public void indexing(String text) {
        if (text != null) {
            logger.info("\nIndexed: " + weblink.getId() + "\n");
        }
    }
}
