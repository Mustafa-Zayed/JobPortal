package com.mustafaz.JobPortal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileDownloadUtil {

    private Path foundfile;

    public Resource getFileAsResource(String downloadDir, String fileName)
            throws IOException {

        Path path = Path.of(downloadDir);

        try (Stream<Path> filesInDirectory = Files.list(path)){

            filesInDirectory.forEach(file -> {
                if (file.getFileName().toString().startsWith(fileName))
                    foundfile = file;
            });
        }

        if (foundfile != null) {
            return new UrlResource(foundfile.toUri());
        }

        return null;
    }
}
