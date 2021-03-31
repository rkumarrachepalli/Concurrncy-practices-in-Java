package com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Configuration;

import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Producer.FileWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class PropertiesProvider implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FileWatcher.class);
    private Properties properties;

    public PropertiesProvider() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("common.properties"));
        } catch (IOException e) {
            logger.warn("Failed to load properties", e.getMessage());
        }
    }

    public String getInputFolder() {
        return properties.getProperty("source.folder");
    }

    public String getOutputFolder() {
        return properties.getProperty("sink.folder");
    }

    public int getFileMaxQueueSize() {
        return Integer.parseInt(properties.getProperty("max.file.queue.size"));
    }

    public int getWatcherThreadCount() {
        return Integer.parseInt(properties.getProperty("watcher.thread.count"));
    }

    public int getInputReaderThreadCount() {
        return Integer.parseInt(properties.getProperty("input.reader.thread.count"));
    }

    public int getOutputWriterThreadCount() {
        return Integer.parseInt(properties.getProperty("output.writer.thread.count"));
    }

    public int getMaxBlockingQueueSize() {
        return Integer.parseInt(properties.getProperty("max.blocking.queue.size"));
    }

    public String getFileModificationName() {
        return properties.getProperty("file.name");
    }

    public String getFileExtension() {
        return properties.getProperty("file.extension");
    }

    public boolean isRenamingEnabled() {
        return Boolean.parseBoolean(properties.getProperty("enable.file.renaming"));
    }

    public boolean extensionModificationEnabled() {
        return Boolean.parseBoolean(properties.getProperty("enable.file.extension.modification"));
    }
}
