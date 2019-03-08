package com.christian.gospeldetail.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.gospeldetail.data.GospelDetailBean
import com.christian.gospeldetail.ui.main.GospelDetailFragment
import com.google.firebase.firestore.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.warn
import com.christian.R

/**
 * Adapter for the RecyclerView in GospelDetailFragment
 */
abstract class GospelDetailAdapter(private var query: Query, private val gospelDetailBeanList: GospelDetailFragment) : RecyclerView.Adapter<GospelDetailAdapter.ViewHolder>(), EventListener<QuerySnapshot>, AnkoLogger {

    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()

    override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            warn { "onEvent:error$e" }
            onError(e)
            return
        }

        if (documentSnapshots == null) {
            return
        }

        // Dispatch the event
        debug { "onEvent:numChanges:$documentSnapshots.documentChanges.size" }
        for (change in documentSnapshots.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

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
            registration = query.addSnapshotListener(this)
        }
    }

    private fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        notifyDataSetChanged()
    }

    fun setQuery(query: Query) {
        // Stop listening
        stopListening()

        // Clear existing data
        snapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        this.query = query
        startListening()
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gospel_detail_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> VIEW_TYPE_GOSPEL_DETAIL_TITLE
            snapshots.size - 2 -> VIEW_TYPE_GOSPEL_DETAIL_AMEN
            snapshots.size - 1 -> VIEW_TYPE_GOSPEL_DETAIL_AUTHOR
            snapshots.size -> VIEW_TYPE_GOSPEL_DETAIL_DATE
        }
        return VIEW_TYPE_GOSPEL_DETAIL_CONTENT
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(snapshot: DocumentSnapshot) {

            val gospelDetailBean = snapshot.toObject(GospelDetailBean::class.java)
            val resources = itemView.resources

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
//                    listener!!.onRestaurantSelected(snapshot)
//                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_GOSPEL_DETAIL_TITLE = 0
        const val VIEW_TYPE_GOSPEL_DETAIL_CONTENT = 1
        const val VIEW_TYPE_GOSPEL_DETAIL_AMEN = 2
        const val VIEW_TYPE_GOSPEL_DETAIL_AUTHOR = 3
        const val VIEW_TYPE_GOSPEL_DETAIL_DATE = 4
    }

    abstract fun onDataChanged()
    abstract fun onError(e: FirebaseFirestoreException)
}