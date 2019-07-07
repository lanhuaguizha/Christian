package com.christian.library.extension;

import androidx.annotation.Keep;
import java.util.List;
import com.christian.library.Recommendation;

/**
 * @author drakeet
 */
@Keep
public class RecommendationResponse {
  public int code;
  public List<Recommendation> data;
}
