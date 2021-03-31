package com.roche.internal.concurrency.ParallelFileProcessing;

import com.roche.internal.concurrency.ParallelFileProcessing.Configuration.PropertiesProvider;
import com.roche.internal.concurrency.ParallelFileProcessing.Consumer.FileOperations;
import com.roche.internal.concurrency.ParallelFileProcessing.Consumer.OutputFileWriter;
import com.roche.internal.concurrency.ParallelFileProcessing.Producer.FileWatcher;
import com.roche.internal.concurrency.ParallelFileProcessing.Producer.InputFileReader;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) {

        PropertiesProvider propertiesProvider = new PropertiesProvider();
        BlockingQueue<Map<String, List<String>>> blockingQueue = new LinkedBlockingQueue<>();
        ArrayDeque<String> fileNames = new ArrayDeque<>();
        FileWatcher fileWatcher = new FileWatcher(fileNames, propertiesProvider);
        InputFileReader inputFileReader = new InputFileReader(blockingQueue, fileNames, propertiesProvider);
        FileOperations fileOperations = new FileOperations(propertiesProvider);
        OutputFileWriter outputFileWriter = new OutputFileWriter(blockingQueue, propertiesProvider, fileOperations);

        int inputReaderThreads = propertiesProvider.getInputReaderThreadCount();
        int outputWriterThreads = propertiesProvider.getOutputWriterThreadCount();
        ExecutorService watcherExecutor = Executors.newFixedThreadPool(propertiesProvider.getWatcherThreadCount());
        watcherExecutor.submit(fileWatcher);
        ExecutorService readerExecutor = Executors.newFixedThreadPool(inputReaderThreads);
        ExecutorService writerExecutor = Executors.newFixedThreadPool(outputWriterThreads);

        for (int i = 0; i <= propertiesProvider.getInputReaderThreadCount(); i++) {
            readerExecutor.submit(inputFileReader);
        }

        for (int i = 0; i <= propertiesProvider.getOutputWriterThreadCount(); i++) {
            writerExecutor.submit(outputFileWriter);
        }
    }
}
