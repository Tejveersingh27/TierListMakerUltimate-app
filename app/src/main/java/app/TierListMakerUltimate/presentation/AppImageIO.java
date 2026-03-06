package app.TierListMakerUltimate.presentation;

import android.content.Context;
import android.widget.ImageView;

public class AppImageIO {
    private final Context context;

    public AppImageIO(Context context) {
        this.context = context;
    }

    public void load(String uri, ImageView imageView) {
        if (uri.startsWith("android.resource://")) {

        }
    }
}
