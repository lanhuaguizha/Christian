package com.christian.disciple

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.me.SignInActivity
import com.christian.nav.NavFragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DiscipleFragment : NavFragment(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mUsername: String
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var mFirebaseUser: FirebaseUser? = null
    private lateinit var mPhotoUrl: String
    private var mGoogleApiClient: GoogleApiClient? = null

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mFirebaseDatabaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        mUsername = ANONYMOUS

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.getCurrentUser()

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(activity, SignInActivity::class.java))
//            activity?.finish()
//            return
        } else {
            mUsername = mFirebaseUser?.displayName ?: "firebase user's display name is null"
            if (mFirebaseUser?.photoUrl != null) {
                mPhotoUrl = mFirebaseUser?.photoUrl?.toString() ?: ""
            }
        }

        mGoogleApiClient = context?.let {
            activity?.let { it1 ->
                GoogleApiClient.Builder(it)
                        .enableAutoManage(it1 /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API)
                        .build()
            }
        }

        mLinearLayoutManager = LinearLayoutManager(context)
        mLinearLayoutManager.stackFromEnd = true

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference

//        val parser = SnapshotParser<DiscipleMessageBean> { dataSnapshot ->
//            val friendlyMessage = dataSnapshot.getValue<DiscipleMessageBean>(DiscipleMessageBean::class.java)
//            if (friendlyMessage != null) {
////                friendlyMessage!!.setId(dataSnapshot.key)
//            }
////            friendlyMessage
//        }

        val messagesRef = mFirebaseDatabaseReference!!.child(MESSAGES_CHILD)

//        val options = FirebaseRecyclerOptions.Builder<DiscipleMessageBean>()
//                .setQuery(messagesRef, parser)
//                .build()


        return inflater.inflate(R.layout.disciple_fragment, container, false)
    }

    companion object {

        private val TAG = "MainActivity"
        val MESSAGES_CHILD = "messages"
        private val REQUEST_INVITE = 1
        private val REQUEST_IMAGE = 2
        val DEFAULT_MSG_LENGTH_LIMIT = 10
        val ANONYMOUS = "anonymous"
        private val MESSAGE_SENT_EVENT = "message_sent"
        private val MESSAGE_URL = "http://friendlychat.firebase.google.com/message/"
        private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}