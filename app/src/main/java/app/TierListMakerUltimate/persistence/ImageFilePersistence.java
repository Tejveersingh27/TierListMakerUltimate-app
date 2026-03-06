package app.TierListMakerUltimate.persistence;

import java.io.IOException;
import java.io.InputStream;

public interface ImageFilePersistence {
    void saveImage(InputStream inputStream, String fileName) throws IOException;

    void deleteImage(String fileName) throws IOException;
}



