package com.christian.multitype;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.christian.R;

import com.christian.multitype.Card;

import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Image;
import org.commonmark.node.Paragraph;

import java.util.Collection;
import java.util.Collections;

import me.drakeet.multitype.ItemViewBinder;
import ru.noties.markwon.AbstractMarkwonPlugin;
import ru.noties.markwon.Markwon;
import ru.noties.markwon.MarkwonConfiguration;
import ru.noties.markwon.MarkwonSpansFactory;
import ru.noties.markwon.MarkwonVisitor;
import ru.noties.markwon.RenderProps;
import ru.noties.markwon.SpanFactory;
import ru.noties.markwon.core.CorePlugin;
import ru.noties.markwon.core.MarkwonTheme;
import ru.noties.markwon.image.AsyncDrawable;
import ru.noties.markwon.image.AsyncDrawableLoader;
import ru.noties.markwon.image.ImageItem;
import ru.noties.markwon.image.ImageProps;
import ru.noties.markwon.image.ImagesPlugin;
import ru.noties.markwon.image.SchemeHandler;
import ru.noties.markwon.image.network.NetworkSchemeHandler;

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
                .usePlugin(CorePlugin.create())
                .usePlugin(ImagesPlugin.create(context))
                .usePlugin(new AbstractMarkwonPlugin() {
//                    @Override
//                    public void configureImages(@NonNull AsyncDrawableLoader.Builder builder) {
//                        // we can have a custom SchemeHandler
//                        // here we will just use networkSchemeHandler to redirect call
//                        builder.addSchemeHandler("myownscheme", new SchemeHandler() {
//
//                            final NetworkSchemeHandler networkSchemeHandler = NetworkSchemeHandler.create();
//
//                            @Nullable
//                            @Override
//                            public ImageItem handle(@NonNull String raw, @NonNull Uri uri) {
//                                raw = raw.replace("myownscheme", "https");
//                                return networkSchemeHandler.handle(raw, Uri.parse(raw));
//                            }
//                        });
//                    }

                    @Override
                    public void configureImages(@NonNull AsyncDrawableLoader.Builder builder) {
                        builder.placeholderDrawableProvider(() -> {
                            // your custom placeholder drawable
                            final ColorDrawable placeholder = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));
                            // these bounds will be used to display a placeholder,
                            // so even if loading image has size `width=100%`, placeholder
                            // bounds won't be affected by it
                            placeholder.setBounds(0, 0, 10800, 480);
                            return placeholder;
//                                return new PlaceholderDrawable();
                        });
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