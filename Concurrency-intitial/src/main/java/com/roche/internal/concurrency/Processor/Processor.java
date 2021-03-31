package com.roche.internal.concurrency.Processor;

public interface Processor {
    String fileRenamingProcessor(String fileName);
    String fileExtensionProcessor(String fileName);
}
