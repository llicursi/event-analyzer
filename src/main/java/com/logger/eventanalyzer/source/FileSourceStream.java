package com.logger.eventanalyzer.source;

import java.io.*;
import java.util.stream.Stream;

public class FileSourceStream implements SourceStream {

    private BufferedReader bufferedReader;

    public FileSourceStream(File file) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    @Override
    public Stream<String> stream() {
        return bufferedReader.lines();
    }
}
