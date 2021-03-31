package com.roche.internal.concurrency.Basics.ExecutorFramework;

import com.roche.internal.concurrency.Basics.Commons.Indexer;
import com.roche.internal.concurrency.Basics.Commons.Weblink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Index extends Indexer {

    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    private Weblink weblink;

    public Index(Weblink weblink) {
        this.weblink = weblink;
    }

    public void run() {
        while (true) {
            String htmlPage = weblink.getHtmlPage();
            if (htmlPage != null) {
                indexing(htmlPage);

                break;
            } else {
                logger.info(weblink.getId() + " not yet downloaded!");
            }
        }
    }

    public void indexing(String text) {
        if (text != null) {
            logger.info("\nIndexed: " + weblink.getId() + "\n");
        }
    }


}
