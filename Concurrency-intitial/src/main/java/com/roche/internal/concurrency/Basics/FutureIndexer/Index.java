package com.roche.internal.concurrency.Basics.FutureIndexer;

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
        String htmlPage = weblink.getHtmlPage();
        indexing(htmlPage);
    }

    public void indexing(String text) {
        if (text != null) {
            logger.info("\nIndexed: " + weblink.getId() + "\n");
        }
    }

}
