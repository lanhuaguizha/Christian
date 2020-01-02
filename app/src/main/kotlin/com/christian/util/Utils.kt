package com.christian.util

import android.Manifest
import com.christian.R
import com.kotlinpermissions.KotlinPermissions
import ren.qinc.markdowneditors.AppContext
import ren.qinc.markdowneditors.view.EditorActivity

fun requestStoragePermission(editorActivity: EditorActivity, path: String) {
    KotlinPermissions.with(editorActivity) // where this is an FragmentActivity instance
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .onAccepted { permissions ->
                //List of accepted permissions
                editorActivity.requestStoragePermissionAccepted(path)
            }
            .onDenied { permissions ->
                //List of denied permissions
                AppContext.showSnackbar(editorActivity.mViewPager, editorActivity.getString(R.string.no_acccess_to_read))
                requestStoragePermission(editorActivity, path)
            }
            .onForeverDenied { permissions ->
                //List of forever denied permissions
                AppContext.showSnackbar(editorActivity.mViewPager, R.string.no_acccess_to_read_image_to_display)
            }
            .ask()
}