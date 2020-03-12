package com.christian.nav.me;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.multitype.ItemViewBinder;

import com.bumptech.glide.Glide;
import com.christian.multitype.Contributor;
import com.christian.multitype.OnContributorClickedListener;

import static android.net.Uri.parse;

/**
 * @author drakeet
 */
@SuppressWarnings("WeakerAccess")
public class ContributorViewBinder extends ItemViewBinder<Contributor, ContributorViewBinder.ViewHolder> {

  private @NonNull final AbsAboutActivity activity;
  private Context context;

  public ContributorViewBinder(@NonNull AbsAboutActivity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
    context = parent.getContext();
    return new ViewHolder(inflater.inflate(com.christian.R.layout.about_page_item_contributor, parent, false), activity);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Contributor contributor) {
//    holder.avatar.setImageResource(contributor.avatarResId);
    Glide.with(context).load(contributor.avatarResId).into(holder.avatar);
    holder.name.setText(contributor.name);
    holder.desc.setText(contributor.desc);
    holder.data = contributor;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView avatar;
    public TextView name;
    public TextView desc;
    public Contributor data;
    protected @NonNull final AbsAboutActivity activity;

    public ViewHolder(View itemView, @NonNull AbsAboutActivity activity) {
      super(itemView);
      this.activity = activity;
      avatar = itemView.findViewById(com.christian.R.id.avatar);
      name = itemView.findViewById(com.christian.R.id.name);
      desc = itemView.findViewById(com.christian.R.id.desc);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      OnContributorClickedListener listener = activity.getOnContributorClickedListener();
      if (listener != null && listener.onContributorClicked(v, data)) {
        return;
      }
      if (data.url != null) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(parse(data.url));
        try {
          v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public long getItemId(@NonNull Contributor item) {
    return item.hashCode();
  }
}