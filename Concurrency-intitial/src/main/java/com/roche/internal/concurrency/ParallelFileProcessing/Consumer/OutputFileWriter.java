package com.roche.internal.concurrency.ParallelFileProcessing.Consumer;


import com.roche.internal.concurrency.ParallelFileProcessing.Configuration.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class OutputFileWriter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OutputFileWriter.class);
    private static final List<String> END = new ArrayList<>();
    private static BufferedWriter bufferedWriter;
    private BlockingQueue<Map<String, List<String>>> blockingQueue;
    private PropertiesProvider propertiesProvider;
    private FileOperations fileOperations;
    private String modifiedFile;

    public OutputFileWriter(BlockingQueue<Map<String, List<String>>> blockingQueue, PropertiesProvider propertiesProvider, FileOperations fileOperations) {
        this.blockingQueue = blockingQueue;
        this.propertiesProvider = propertiesProvider;
        this.fileOperations = fileOperations;
    }

    /**
     * Override the Runnable run method to initiate the writer process and write to the output.
     */
    @Override
    public void run() {
        while (true) {
            if (!blockingQueue.isEmpty()) {
                Map<String, List<String>> dataGroup = blockingQueue.poll();
                String outputFileName = dataGroup.keySet().iterator().next();
                synchronized (outputFileName) {
                    if (dataGroup == null) {
                        return;
                    }
                    if (dataGroup.isEmpty()) {
                        blockingQueue.add((Map<String, List<String>>) END);
                        return;
                    }
                    if (propertiesProvider.isRenamingEnabled()) {
                        modifiedFile = fileOperations.fileRenamingProcessor(outputFileName);
                        logger.info("after renaming" + modifiedFile);
                        if (propertiesProvider.extensionModificationEnabled()) {
                            modifiedFile = fileOperations.fileExtensionProcessor(modifiedFile);
                            logger.info("after changing extension " +modifiedFile);
                        }
                    } else {
                        modifiedFile = outputFileName;
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesProvider.getOutputFolder() + modifiedFile))) {
                        for (String line : dataGroup.get(outputFileName)) {
                            writer.write(line);
                        }
                        TimeUnit.SECONDS.sleep(1);
                        logger.info("Written data to output file: " + modifiedFile);
                    } catch (IOException | InterruptedException e) {
                        logger.error("Error while writing data to output file");
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                logger.error("Thread interruption error", e.getMessage());
            }
        }
    }
}

