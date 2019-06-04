package com.christian.navdetailitem.gospel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.nav.NavActivity
import com.google.firebase.firestore.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gospel_detail_item.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.warn
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.get

/**
 * Adapter for the RecyclerView in NavDetailFragment
 */
abstract class GospelDetailAdapter(private var gospelRef: DocumentReference, val navActivity: NavActivity) : androidx.recyclerview.widget.RecyclerView.Adapter<GospelDetailAdapter.ViewHolder>(), EventListener<DocumentSnapshot>, AnkoLogger {

    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()
    private var snapshot: DocumentSnapshot? = null

    override fun onEvent(documentSnapshots: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            warn { "onEvent:error$e" }
            onError(e)
            return
        }

        if (documentSnapshots == null) {
            return
        }

        snapshot = documentSnapshots
        // Dispatch the event
        debug { "onEvent:numChanges:$documentSnapshots.documentChanges.size" }
//        for (change in documentSnapshots.documentChanges) {
//            when (change.type) {
//                DocumentChange.Type.ADDED -> onDocumentAdded(change)
//                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
//                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
//            }
//        }
//        onDocumentAdded(documentSnapshots)
        notifyDataSetChanged()
        onDataChanged()
    }

    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    private fun startListening() {
        if (registration == null) {
            registration = gospelRef.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        notifyDataSetChanged()
    }

    fun setQuery(gospelRef: DocumentReference) {
        // Stop listening
        stopListening()

        // Clear existing data
        snapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        this.gospelRef = gospelRef
        startListening()
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gospel_detail_item, parent, false))
    }

    override fun getItemCount(): Int {
        if (snapshot != null) {
            return (snapshot?.data?.get("detail") as java.util.ArrayList<*>).size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        snapshot?.let { holder.bind(it) }
    }

    class ViewHolder(override val containerView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(snapshot: DocumentSnapshot) {

            val subtitle = ((snapshot.data?.get("detail") as java.util.ArrayList<*>)[adapterPosition] as HashMap<*, *>)["subtitle"]
            if (subtitle != null) {
                tv_gospel_detail_item.visibility = View.VISIBLE
                tv_gospel_detail_item.text = subtitle.toString()
            } else {
                tv_gospel_detail_item.visibility = View.GONE
            }

            val image = ((snapshot.data?.get("detail") as java.util.ArrayList<*>)[adapterPosition] as HashMap<*, *>)["image"]
            if (image != null) {
                iv_gospel_detail_item.visibility = View.VISIBLE
                Glide.with(containerView.context).load(image).into(iv_gospel_detail_item)
            } else {
                iv_gospel_detail_item.visibility = View.GONE
            }
            tv2_detail_nav_item.text = ((snapshot.data?.get("detail") as java.util.ArrayList<*>)[adapterPosition] as HashMap<*, *>)["content"].toString()

//            val gospelDetailBean = snapshot.toObject(GospelDetailBean::class.java)
//            val resources = itemView.resources

            // Load image
//            Glide.with(imageView.getContext())
//                    .load(gospelDetailBean!!.getPhoto())
//                    .into(imageView)
//
//            nameView.setText(gospelDetailBean!!.getName())
//            ratingBar.setRating(gospelDetailBean!!.getAvgRating() as Float)
//            cityView.setText(gospelDetailBean!!.getCity())
//            categoryView.setText(gospelDetailBean!!.getCategory())
//            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
//                    gospelDetailBean!!.getNumRatings()))
//            priceView.setText(RestaurantUtil.getPriceString(gospelDetailBean))

            // Click listener
            itemView.setOnClickListener {
                //                if (listener != null) {
//                    listener!!.onGospelSelected(snapshot)
//                }
            }
        }
    }

    abstract fun onDataChanged()
    abstract fun onError(e: FirebaseFirestoreException)
}