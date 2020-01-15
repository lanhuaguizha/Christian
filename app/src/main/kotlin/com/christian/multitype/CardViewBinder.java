package com.christian.multitype;

import android.content.Context;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.christian.R;
import com.christian.util.UtilsKt;

import org.commonmark.node.Image;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.glide.GlideImagesPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author drakeet
 */
@SuppressWarnings("WeakerAccess")
public class CardViewBinder extends ItemViewBinder<Card, CardViewBinder.ViewHolder> {

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        context = parent.getContext();
        View root = inflater.inflate(R.layout.about_page_item_card, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Card card) {
        UtilsKt.setMarkdownToTextView(context, holder.content, card.content.toString());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    @Override
    public long getItemId(@NonNull Card item) {
        return item.hashCode();
    }
}