package com.christian.extension;

import androidx.annotation.Keep;
import java.util.List;
import com.christian.Recommendation;

/**
 * @author drakeet
 */
@Keep
public class RecommendationResponse {
  public int code;
  public List<Recommendation> data;
}
