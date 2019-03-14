package com.christian.navitem.me

import com.christian.data.MeBean
import com.christian.navitem.NavItemPresenter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import java.util.*

/**
 * MeBean用于MeItemView数据实体类
 */
class MeItemPresenter(query: Query, listener: OnGospelSelectedListener, navs: MeBean, navId: Int) : NavItemPresenter<MeBean>(query, listener, navs, navId) {
    override fun onDataChanged() {
    }

    override fun onError(e: FirebaseFirestoreException) {
    }

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var navItemView: MeItemView
    private val providers = Arrays.asList(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
    )

    init {

        fireBaseAuth = FirebaseAuth.getInstance()
        // 检查用户是否登录和更新UI
        val currentUser = fireBaseAuth.currentUser
        navItemView.updateUserUI(currentUser)
    }

    override fun deinit() {
//        fireBaseAuth.
    }

    override fun createUser() {
        // Create and launch sign-in intent
        navActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }


}