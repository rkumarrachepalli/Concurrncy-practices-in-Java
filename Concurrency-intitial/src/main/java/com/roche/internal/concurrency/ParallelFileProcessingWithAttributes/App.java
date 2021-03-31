package com.roche.internal.concurrency.ParallelFileProcessingWithAttributes;

import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Configuration.PropertiesProvider;
import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Consumer.FileOperations;
import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Consumer.OutputFileWriter;
import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Producer.FileWatcher;
import com.roche.internal.concurrency.ParallelFileProcessingWithAttributes.Producer.InputFileReader;

import java.io.File;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class App {

    public static void main(String[] args) {

        PropertiesProvider propertiesProvider = new PropertiesProvider();
        BlockingQueue<File> blockingQueue = new LinkedBlockingQueue<>();
        FileWatcher fileWatcher = new FileWatcher(blockingQueue, propertiesProvider);
        FileOperations fileOperations = new FileOperations(propertiesProvider);
        OutputFileWriter outputFileWriter = new OutputFileWriter(blockingQueue, propertiesProvider, fileOperations);

       // int inputReaderThreads = propertiesProvider.getInputReaderThreadCount();
        int outputWriterThreads = propertiesProvider.getOutputWriterThreadCount();
        ExecutorService watcherExecutor = Executors.newFixedThreadPool(propertiesProvider.getWatcherThreadCount());
        watcherExecutor.submit(fileWatcher);
        //ExecutorService readerExecutor = Executors.newFixedThreadPool(inputReaderThreads);
        ExecutorService writerExecutor = Executors.newFixedThreadPool(outputWriterThreads);

//        for (int i = 0; i <= propertiesProvider.getInputReaderThreadCount(); i++) {
//            readerExecutor.submit(inputFileReader);
//        }

        for (int i = 0; i <= propertiesProvider.getOutputWriterThreadCount(); i++) {
            writerExecutor.submit(outputFileWriter);
        }
    }
}
