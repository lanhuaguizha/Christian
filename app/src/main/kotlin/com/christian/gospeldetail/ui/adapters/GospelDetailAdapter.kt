package com.christian.gospeldetail.ui.adapters

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.warn
import com.christian.R
import com.christian.nav.NavActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gospel_detail_item.*
import org.jetbrains.anko.info
import java.util.HashMap

/**
 * Adapter for the RecyclerView in GospelDetailFragment
 */
abstract class GospelDetailAdapter(private var query: Query, val navActivity: NavActivity) : RecyclerView.Adapter<GospelDetailAdapter.ViewHolder>(), EventListener<QuerySnapshot>, AnkoLogger {

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

    fun stopListening() {
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

    override fun getItemCount(): Int {
        return snapshots.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        info { "position$position, snapshot${getSnapshot(position)}" }
        navActivity.hidePb()
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(snapshot: DocumentSnapshot) {

//            tv_gospel_detail_item.text = snapshot.data?.get("title")?.toString()
            tv_gospel_detail_item.text = ((snapshot.data?.get("detail") as java.util.ArrayList<*>)[adapterPosition] as HashMap<*, *>)["subtitle"].toString()
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
//                    listener!!.onRestaurantSelected(snapshot)
//                }
            }
        }
    }

    abstract fun onDataChanged()
    abstract fun onError(e: FirebaseFirestoreException)
}