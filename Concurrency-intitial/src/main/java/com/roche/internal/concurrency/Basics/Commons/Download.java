package com.roche.internal.concurrency.Basics.Commons;


import com.roche.internal.concurrency.Basics.ThreadCooperationUsingNaive.HttpConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class Download implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Download.class);
    private Weblink weblink;

    public Download(Weblink weblink) {
        this.weblink = weblink;
    }

    @Override
    public void run() {
        try {
            String htmlPage = HttpConnect.download(weblink.getUrl());
            weblink.setHtmlPage(htmlPage);
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error("Failed to download hml page from URL", e.getMessage());
        }
    }
}
