package com.christian.library.multitype;

import androidx.annotation.NonNull;
import android.widget.ImageView;

/**
 * @author drakeet
 */
public interface ImageLoader {

  void load(@NonNull ImageView imageView, @NonNull String url);
}
