package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;

public class FileStore  implements BlobStore {

    private final Tika tika = new Tika();

@Override
public void put(Blob blob) throws IOException {
    File targetFile = new File(blob.name);

    targetFile.delete();
    targetFile.getParentFile().mkdirs();
    targetFile.createNewFile();


    try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
        IOUtils.copy(blob.inputStream, outputStream);
    }
 }

@Override
public Optional<Blob> get(String name) throws IOException {
    File file = new File(name);

    if (!file.exists()) {
        return Optional.empty();
    }

    return Optional.of(new Blob(
            name,
            new FileInputStream(file),
            tika.detect(file)
    ));
 }

@Override
public void deleteAll() {
        // ...
}
    private void saveUploadToFile(Blob blob, File targetFile) throws IOException {
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        byte[] targetArray = new byte[blob.inputStream.available()];
        blob.inputStream.read(targetArray);

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(targetArray);
        }
    }

    private File getCoverFile(String albumId) {
        //return new File(format("covers/%d", albumId));
        return new File("covers/" + albumId);
    }

    private Path getExistingCoverPath(String albumId) throws URISyntaxException {
        File coverFile = getCoverFile(albumId);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }
}