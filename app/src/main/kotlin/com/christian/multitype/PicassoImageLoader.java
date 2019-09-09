package com.christian.multitype;

import androidx.annotation.NonNull;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.christian.multitype.ImageLoader;

/**
 * @author drakeet
 */
public class PicassoImageLoader implements ImageLoader {

  @Override
  public void load(@NonNull ImageView imageView, @NonNull String url) {
    Picasso.get().load(url).into(imageView);
  }
}
