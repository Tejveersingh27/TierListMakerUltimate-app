package app.TierListMakerUltimate.persistence.stubs;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.persistence.ImageFilePersistence;

public class ImageFilePersistenceStub implements ImageFilePersistence {
    private Context context;

    public ImageFilePersistenceStub(Context context) {
        this.context = context;
    }


    @Override
    public void saveImage(InputStream inputStream, String fileName) throws IOException {
        try (FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            outputStream.write(inputStream.readAllBytes());
        }
    }

    @Override
    public void deleteImage(String fileName) throws IOException {
        context.deleteFile(fileName);
    }
}
