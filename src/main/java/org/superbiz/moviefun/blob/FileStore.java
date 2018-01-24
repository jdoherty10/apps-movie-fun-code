package org.superbiz.moviefun.blob;

import org.apache.tika.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.Files.readAllBytes;


@Service
public class FileStore implements BlobStore {
    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(IOUtils.toByteArray(blob.inputStream));
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File file = new File(name);
        Path path = file.toPath();

        Blob blob = new Blob(name, new FileInputStream(file), Files.probeContentType(path));

        return Optional.of(blob);
    }

    @Override
    public void deleteAll() {

    }
}
