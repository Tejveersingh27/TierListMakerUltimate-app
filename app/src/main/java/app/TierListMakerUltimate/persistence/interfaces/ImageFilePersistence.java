package app.TierListMakerUltimate.persistence.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles persistence for image files.
 */
public interface ImageFilePersistence {

    /**
     * Saves an image to the file system and returns the file name.
     */
    String saveImage(InputStream inputStream, String extension) throws IOException;

    /**
     * Deletes an image from the file system.
     */
    void deleteImage(String fileName);
}