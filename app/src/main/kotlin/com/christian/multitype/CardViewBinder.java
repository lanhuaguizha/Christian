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
                .usePlugin(ImagesPlugin.create())
                // use supplied Glide instance
                .usePlugin(GlideImagesPlugin.create(Glide.with(context)))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(HtmlPlugin.create())
//                // if you need more control
//                .usePlugin(GlideImagesPlugin.create(new GlideImagesPlugin.GlideStore() {
//                    @NonNull
//                    @Override
//                    public RequestBuilder<Drawable> load(@NonNull AsyncDrawable drawable) {
//                        return Glide.with(context).load(drawable.getDestination());
//                    }
//
//                    @Override
//                    public void cancel(@NonNull Target<?> target) {
//                        Glide.with(context).clear(target);
//                    }
//                }))
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureSpansFactory(@NonNull MarkwonSpansFactory.Builder builder) {
                        builder.addFactory(Image.class, (configuration, props) -> new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER));
                    }
                })
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