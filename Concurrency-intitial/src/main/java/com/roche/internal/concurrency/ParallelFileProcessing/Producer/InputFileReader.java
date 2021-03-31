package com.roche.internal.concurrency.ParallelFileProcessing.Producer;

import com.roche.internal.concurrency.ParallelFileProcessing.Configuration.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class InputFileReader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InputFileReader.class);
    private static final int GROUP_LIMIT = 102400;
    private BlockingQueue<Map<String, List<String>>> blockingQueue;
    private ArrayDeque<String> fileNames;
    private PropertiesProvider propertiesProvider;

    public InputFileReader(BlockingQueue<Map<String, List<String>>> blockingQueue, ArrayDeque<String> fileNames, PropertiesProvider propertiesProvider) {
        this.blockingQueue = blockingQueue;
        this.fileNames = fileNames;
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * Override the Runnable run method to initiate the input reader process.
     */
    @Override
    public void run() {
        while (true) {
            synchronized (fileNames) {
                if (!fileNames.isEmpty()) {
                    File inputFile = new File(propertiesProvider.getInputFolder() + fileNames.poll());
                    if (!inputFile.exists()) {
                        logger.error("cat not find such file: {}", inputFile.getName());
                        return;
                    }
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
                        String line;
                        List<String> dataGroup = new ArrayList<>(GROUP_LIMIT);
                        while ((line = bufferedReader.readLine()) != null) {
                            if (dataGroup.size() >= GROUP_LIMIT) {
                                addDataGroup(inputFile.getName(), dataGroup);
                                dataGroup = new ArrayList<>(GROUP_LIMIT);
                            }
                            dataGroup.add(line);
                        }
                        addDataGroup(inputFile.getName(), dataGroup);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        logger.error("Error while reading file");
                        throw new RuntimeException(e);
                    }
                } else {
                    logger.info("still empty");
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted while sleeping");
            }
        }
    }

    /**
     * Blocking queue data added: waiting queue is full
     *
     * @param dataGroup set of data dataGroup
     * @throws Exception time.sleep
     */
    private void addDataGroup(String fileName, List<String> dataGroup) throws Exception {
        while (blockingQueue.size() >= propertiesProvider.getMaxBlockingQueueSize()) {
            TimeUnit.SECONDS.sleep(2);
        }
        Map<String, List<String>> a = new HashMap<>();
        a.put(fileName, dataGroup);
        blockingQueue.add(a);
        logger.info(new Timestamp(System.currentTimeMillis()) + " " + Thread.currentThread().getName() + " processing file: " + fileName);
        logger.info("blocking queue size" + blockingQueue.size());
    }
}
