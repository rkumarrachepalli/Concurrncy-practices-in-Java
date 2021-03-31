package com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Producer;

import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Configuration.PropertiesProvider;
import com.sun.nio.file.SensitivityWatchEventModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class FileWatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileWatcher.class);
    private BlockingQueue<File> blockingQueue;
    private int maxFileQueueSize;
    private PropertiesProvider propertiesProvider;

    public FileWatcher(BlockingQueue<File> blockingQueue, PropertiesProvider propertiesProvider) {
        this.blockingQueue = blockingQueue;
        this.propertiesProvider = propertiesProvider;
        this.maxFileQueueSize = propertiesProvider.getFileMaxQueueSize();
    }

    /**
     * start the watcher process in background to monitor the files in this directoryPath.
     */
    public void startWatcher() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path dir = Paths.get(propertiesProvider.getInputFolder());
            dir.register(watcher, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY, ENTRY_CREATE}, SensitivityWatchEventModifier.HIGH);
            logger.info("Watch Service registered for dir: " + propertiesProvider.getInputFolder());
            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    logger.info(kind.name() + ": " + fileName);
                    blockingQueue.add(fileName.toFile());
                }
                if (blockingQueue.size() == maxFileQueueSize) {
                    TimeUnit.SECONDS.sleep(2);
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException ex) {
            logger.info("Failed while finding event");
        }
    }

    /**
     * Override the Runnable run method to initiate the watcher process. Here
     * provide the fileWatcher source i.e the directory path to be monitored
     */
    @Override
    public void run() {
        FileWatcher fileWatcher = null;
        fileWatcher = new FileWatcher(blockingQueue, propertiesProvider);
        fileWatcher.startWatcher();
    }

}
