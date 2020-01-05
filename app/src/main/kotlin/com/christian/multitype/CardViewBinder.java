package com.christian.multitype;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.christian.R;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;
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
        // obtain an instance of Markwon
//        final String markdown = "`A code` that is rendered differently\n\n```\nHello!\n```";

//        final Markwon markwon = Markwon.builder(context)
//                .usePlugin(new AbstractMarkwonPlugin() {
//                    @Override
//                    public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
//                        builder
//                                .codeBackgroundColor(Color.BLACK)
//                                .codeTextColor(Color.RED);
//                    }
//                })
//                .build();
//        final String markdown = "![image](myownscheme://en.wikipedia.org/static/images/project-logos/enwiki-2x.png)";
        final Markwon markwon = Markwon.builder(context)
                // automatically create Glide instance
                .usePlugin(GlideImagesPlugin.create(context))
                // use supplied Glide instance
                .usePlugin(GlideImagesPlugin.create(Glide.with(context)))
                .build();
        // set markdown
        markwon.setMarkdown(holder.content, card.content.toString());
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