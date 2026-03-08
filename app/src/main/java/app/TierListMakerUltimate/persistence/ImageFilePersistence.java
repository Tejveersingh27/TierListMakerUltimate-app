package app.TierListMakerUltimate.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ImageFilePersistence {
    String saveImage(InputStream inputStream, String extension) throws IOException;

    void deleteImage(String fileName) throws IOException;
}



