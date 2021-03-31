package com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Consumer;


import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Configuration.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class OutputFileWriter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OutputFileWriter.class);
    private static final List<String> END = new ArrayList<>();
    private BlockingQueue<File> blockingQueue;
    private PropertiesProvider propertiesProvider;
    private FileOperations fileOperations;
    private String modifiedFile;

    public OutputFileWriter(BlockingQueue<File> blockingQueue, PropertiesProvider propertiesProvider, FileOperations fileOperations) {
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
                File dataGroup = blockingQueue.poll();
                String inputFileName = propertiesProvider.getInputFolder() + dataGroup.getName();
                logger.info("file name is " + inputFileName);
                if (dataGroup == null) {
                    return;
                }
//                if (!dataGroup.getAbsoluteFile().exists()) {
//                    logger.info("File does not exists");
//                    return;
//                }
//                    logger.info("entering synchronized" + propertiesProvider.isRenamingEnabled());
//
//                    if (propertiesProvider.isRenamingEnabled()) {
//                        modifiedFile = fileOperations.fileRenamingProcessor(outputFileName);
//                        logger.info("after renaming" + modifiedFile);
//                    }
//                    if (propertiesProvider.extensionModificationEnabled()) {
//                        modifiedFile = fileOperations.fileExtensionProcessor(modifiedFile);
//                    }
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesProvider.getOutputFolder() + dataGroup.getName()))) {
                    int count;
                    char[] buffer = new char[8192];
                    while ((count = bufferedReader.read(buffer)) > 0)
                    {
                        writer.write(buffer, 0, count);
                    }
                    logger.info("Written data to output file: " + propertiesProvider.getOutputFolder() + dataGroup.getName());

                } catch (IOException e) {
                    logger.error("Error while writing data to output file");
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logger.error("Thread interruption error", e.getMessage());
                }
            }
        }
    }
}

