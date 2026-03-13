package app.TierListMakerUltimate.persistence.files;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.persistence.interfaces.ImageFilePersistence;

import app.TierListMakerUltimate.business.utils.IUUIDGenerator;

public class AndroidImageFilePersistence implements ImageFilePersistence {
    private Context context;
    private IUUIDGenerator uuidGenerator;

    public AndroidImageFilePersistence(Context context, IUUIDGenerator uuidGenerator) {
        this.context = context;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public String saveImage(InputStream imageFile, String extension) throws IOException {
        String fileName = createFileName(extension);
        try (FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            outputStream.write(imageFile.readAllBytes());
        }
        return fileName;
    }

    private String createFileName(String extension) {
        return uuidGenerator.generateUUID().toString() + extension;
    }

    @Override
    public void deleteImage(String fileName) {
        context.deleteFile(fileName);
    }
}