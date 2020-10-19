package com.logger.eventanalyzer.source;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileSourceStreamTest {

    private static final String TEST_FILE_DIR = "/files/";

    @Test
    void givenWrongFile_expectsFileNotFound() {
        assertThatThrownBy(() -> {
            new FileSourceStream(new File("nom-existing-file"));
        }).isInstanceOf(FileNotFoundException.class);
    }

    @Test
    void givenSmallFile_with10validRecords_expectsStreamWith10Records() throws FileNotFoundException {
        FileSourceStream fileSourceStream = new FileSourceStream(getFileFromResources("valid-10-records.json"));
        long count = fileSourceStream.stream().count();
        assertThat(count).isEqualTo(10L);
    }

    @Test
    void givenEmptyFile_expectsStreamWith0Records() throws FileNotFoundException {
        FileSourceStream fileSourceStream = new FileSourceStream(getFileFromResources("empty-file.json"));
        long count = fileSourceStream.stream().count();
        assertThat(count).isEqualTo(0);
    }

    private File getFileFromResources(String filename) {
        return new File(this.getClass().getResource(TEST_FILE_DIR).getPath() + filename);
    }

}