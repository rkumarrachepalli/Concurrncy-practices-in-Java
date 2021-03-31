package com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Consumer;

import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Configuration.PropertiesProvider;
import com.roche.internal.concurrency.Processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


public class FileOperations implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(FileOperations.class);
    private PropertiesProvider propertiesProvider;

    public FileOperations(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    @Override
    public String fileRenamingProcessor(String fileName) {
        logger.info("rename files");
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        Function<String, String> func = x -> x.substring(0, x.lastIndexOf('.')) + propertiesProvider.getFileModificationName() + extension;
        return func.apply(fileName);
    }

    @Override
    public String fileExtensionProcessor(String fileName) {
        logger.info("change extensions");
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        Function<String, String> func = x -> x.substring(0, x.lastIndexOf('.')) + "." + propertiesProvider.getFileExtension();
        return func.apply(fileName);
    }
}
