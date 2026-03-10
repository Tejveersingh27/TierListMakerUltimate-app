package app.TierListMakerUltimate.presentation.utils;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.io.File;

public class ImageHelper {
    private final Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    public void loadImage(String uri, ImageView imageView) {
        if (uri == null || uri.isEmpty()) {
            return;
        }

        if (uri.startsWith("android.resource://")) {
            // Load drawable resources
            imageView.setImageURI(Uri.parse(uri));
        } else {
            // Load internal images
            File file = context.getFileStreamPath(uri);
            if (file.exists()) {
                imageView.setImageURI(Uri.fromFile(file));
            }
        }
    }

    public String getFileExtension(String uri) {
        String mimeType = context.getContentResolver().getType(Uri.parse(uri));
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

}
