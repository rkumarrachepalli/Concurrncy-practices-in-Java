package com.roche.internal.concurrency.Basics.FutureIndexer;

import com.roche.internal.concurrency.Basics.Commons.Weblink;
import com.roche.internal.concurrency.Basics.ThreadCooperationUsingNaive.HttpConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

public class Downloader<T extends Weblink> implements Callable<T> {

    private static final Logger logger = LoggerFactory.getLogger(Downloader.class);
    private T weblink;

    public Downloader(T weblink) {
        this.weblink = weblink;
    }

    public T call() {
        try {
            String htmlPage = HttpConnect.download(weblink.getUrl());
            weblink.setHtmlPage(htmlPage);
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error("Failed to download hml page from URL", e.getMessage());
        }
        return weblink;
    }
}
