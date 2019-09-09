package com.christian.multitype;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.view.View;
import com.christian.multitype.Contributor;

public interface OnContributorClickedListener {

  @CheckResult
  boolean onContributorClicked(@NonNull View itemView, @NonNull Contributor contributor);
}
