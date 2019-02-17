package com.christian.navitem

import android.view.View
import android.view.ViewGroup
import com.christian.BaseContract
import com.google.firebase.auth.FirebaseUser

class NavItemContract {

    interface IView : BaseContract.IView<IPresenter> {
        fun onCreateView(parent: ViewGroup, viewType: Int, itemView: View): NavItemView

        fun initView()

        fun deinitView()

        fun animateItemView(itemView: View)

        fun updateUserUI(currentUser: FirebaseUser?)
    }

    interface IPresenter : BaseContract.IPresenter<IView> {

        fun deinit()
        /**
         * UserBean logic
         */
        fun createUser()

        fun updateUser()

        fun deleteUser()

        fun getTitle(pos: Int): String

    }

}