package com.christian.multitype;

import androidx.annotation.NonNull;

/**
 * @author drakeet
 */
public class Card {

  public @NonNull final CharSequence content;

  public Card(@NonNull CharSequence content) {
    this.content = content;
  }
}
