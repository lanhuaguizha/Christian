package com.christian.adapter

import android.content.Intent
import android.os.Build
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.TextView
import com.christian.R
import com.christian.data.Nav
import com.christian.detail.DetailActivity
import com.christian.util.ChristianUtil
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

class DetailAdapter(val navs: List<Nav>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    private var mLastPosition: Int = 0
    private val mAnimateItems = false
    private var mLastAnimatedPosition = 0

    class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        @ViewInject(R.id.content_subtitle)
        internal val tvHomeTitle: TextView? = null
        @ViewInject(R.id.more_action)
        internal val btnHomeMore: AppCompatImageButton? = null

        @Event(value = *intArrayOf(R.id.more_action, R.id.cl_item_container))
        private fun onClick(v: View) {
            when (v.id) {
                R.id.more_action -> {
                    val popupMenu = PopupMenu(v.context, v.findViewById(R.id.more_action))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popupMenu.gravity = Gravity.CENTER_HORIZONTAL
                    }
                    popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { false }
                    popupMenu.show()
                }
                R.id.cl_item_container -> {
                    Log.d(TAG, "Element $adapterPosition clicked.")
                    val intent = Intent(v.context, DetailActivity::class.java)
                    v.context.startActivity(intent)
                }
                else -> {
                }
            }
        }

        init {
            x.view().inject(this, v)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.detail_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    private fun runEnterAnimation(view: View, position: Int) {
        //        if (!mAnimateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
        //            return;
        //        }

        if (position >= mLastAnimatedPosition) {
            mLastAnimatedPosition = position
            view.translationY = ChristianUtil.getScreenHeight(view.context).toFloat()
            view.animate()
                    .translationY(0f)
                    .setStartDelay((400 * position).toLong())
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(400)
                    .start()
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val adapterPosition = viewHolder.adapterPosition
        viewHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val screenItems = ChristianUtil.getScreenHeight(viewHolder.itemView.context) / viewHolder.itemView.measuredHeight
        var animation: Animation? = null
        // 3 conditions no animation: 1. last position < adapter position 2. screen items in front 3. destroy view
        if (adapterPosition >= mLastPosition && adapterPosition >= screenItems) {
            Log.i(TAG, "onBindViewHolder: $mLastPosition$adapterPosition")
            //            if (adapterPosition >= screenItems) {
            //                if (adapterPosition >= mLastPosition) {
            // Load animation form xml
            animation = AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.up_from_bottom)
            //                } else {
            //                    animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.stay);
            //                }

            // Load animator form xml
            //                ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(viewHolder.itemView.getContext(), R.animator.up_from_bottom);
            //                animator.setTarget(viewHolder.itemView);
            //                animator.start();

            //                viewHolder.itemView.setTranslationY(viewHolder.itemView.getMeasuredHeight());
            //                viewHolder.itemView.animate()
            //                        .translationY(0)
            //                        .setInterpolator(new DecelerateInterpolator(3.f))
            //                        .setDuration(200)
            //                        .start();
            //            } else {
            //                runEnterAnimation(viewHolder.itemView, adapterPosition);
            //            }
        } else {
            Log.i(TAG, "onBindViewHolder stay: $mLastPosition$adapterPosition")
            animation = AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.stay)
        }
        if (animation != null)
            viewHolder.itemView.startAnimation(animation)
        mLastPosition = adapterPosition
        // Get element from your dataset at this adapterPosition and replace the contents of the view
        // with that element
        //        viewHolder.getTvHomeTitle().setText(mDataSet[adapterPosition]);
    }

    override fun getItemCount(): Int {
        return navs.size
    }

    companion object {
        private val TAG = "DetailAdapter"
        private val ANIMATED_ITEMS_COUNT = 4
    }

}
