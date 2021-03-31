package com.roche.internal.concurrency.Basics.ThreadCooperationUsingNaive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpConnect {

    private static final Logger logger = LoggerFactory.getLogger(HttpConnect.class);

    public static String download(String sourceUrl) throws MalformedURLException, URISyntaxException {
        logger.info("Downloading: " + sourceUrl);
        URL url = new URI(sourceUrl).toURL();

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                return IOUtil.read(con.getInputStream());
            }
        } catch (IOException e) {
            logger.error("Error while downloading html page");
            throw new RuntimeException(e);
        }
        return null;
    }
}
