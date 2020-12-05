package com.christian.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.christian.R
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.google.android.material.snackbar.Snackbar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.BlurViewExtendsConstraintLayout
import eightbitlab.com.blurview.BlurViewExtendsNavigationView
import eightbitlab.com.blurview.BlurViewExtendsVerticalRecyclerView
import org.jetbrains.anko.dip
private var loadNextPage: Boolean = true
var mPosition = 0

var menuFragmentName = ""

/**
 * utils to blur a view
 */
fun makeViewBlur(view: BlurView, parent: ViewGroup, window: Window, boolean: Boolean = false) {
    val windowBackground = window.decorView.background
    val radius = 25f
    view.setupWith(parent)
        .setFrameClearDrawable(windowBackground)
        .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
        .setBlurRadius(radius)
        .setHasFixedTransformationMatrix(boolean)
}

//为导航试图继承实现高斯模糊设计的工具方法
fun makeViewBlurExtendsNavigationView(
    navView: BlurViewExtendsNavigationView,
    parent: CoordinatorLayout,
    window: Window,
    boolean: Boolean = false
) {
    val windowBackground = window.decorView.background
    val radius = 25f
    navView.setupWith(parent)
        .setFrameClearDrawable(windowBackground)
        .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
        .setBlurRadius(radius)
        .setHasFixedTransformationMatrix(boolean)
}

fun makeViewBlurExtendsConstraintLayout(
    navView: BlurViewExtendsConstraintLayout,
    parent: ViewGroup,
    window: Window,
    boolean: Boolean = false
) {
    val windowBackground = window.decorView.background
    val radius = 25f
    navView.setupWith(parent)
        .setFrameClearDrawable(windowBackground)
        .setBlurAlgorithm(SupportRenderScriptBlur(parent.context))
        .setBlurRadius(radius)
        .setHasFixedTransformationMatrix(boolean)
}

//为底部栏作为二级菜单定制的Snackbar，内部调用show省的使用者忘记调用
fun snackbar(cl_nav_2: CoordinatorLayout, s: String) {
    val snackbar = Snackbar.make(cl_nav_2, s, Snackbar.LENGTH_LONG)
    val snackbarView = snackbar.view
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        snackbarView.elevation = cl_nav_2.context.dip(3).toFloat()
    }
    // Snackbar
//        val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
//        params.anchorId = R.id.bnv_nav
//        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
//        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL// 相对锚点的位置
//        params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // 锚点的位置
//        snackbarView.layoutParams = params
    return snackbar.show()
}

//为底部栏作为二级菜单定制的Toast，内部调用show省的使用者忘记调用
fun toast(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_LONG).show()
}

//详情页返回按钮
//fun setToolbarAsUp(
//    activity: BaseDetailActivity,
//    toolbar: androidx.appcompat.widget.Toolbar,
//    title: String
//) {
//    activity.setSupportActionBar(toolbar)
//    activity.supportActionBar?.title = title
//    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//}

fun EditText.editable(editable: Boolean) {
    if (editable) {
        isFocusableInTouchMode = true;
        isFocusable = true;
        isLongClickable = true
        requestFocus()
        inputType = InputType.TYPE_CLASS_TEXT
    } else {
        isFocusable = false;
        isFocusableInTouchMode = false;
        isLongClickable = false
        inputType = InputType.TYPE_NULL
    }
}

fun AutoCompleteTextView.editable(editable: Boolean) {
    if (editable) {
        isFocusableInTouchMode = true;
        isFocusable = true;
        isLongClickable = true
        requestFocus()
        inputType = InputType.TYPE_CLASS_TEXT
    } else {
        isFocusable = false;
        isFocusableInTouchMode = false;
        isLongClickable = false
        inputType = InputType.TYPE_NULL
    }
}
/*
class ExitDialogFragment(private val act: Activity) : DialogFragment() {

    private lateinit var inflate: View

    override fun onStart() {

//            因为View在添加后,对话框最外层的ViewGroup并不知道我们导入的View所需要的的宽度。 所以我们需要在onStart生命周期里修改对话框尺寸参数

        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        super.onStart()

        val window = dialog!!.window
//        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        inflate =
//            inflater.inflate(R.layout.card_investigation_plan_detail_member, container, false)
//        inflate.button.text = "取消"
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        makeViewBlurExtendsConstraintLayout(
            inflate as BlurViewExtendsConstraintLayout,
            act.window.decorView as ViewGroup,
            dialog?.window!!
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(act)

        inflate =
            act.layoutInflater.inflate(R.layout.card_investigation_plan_detail_member, null)
        inflate.button.text = "取消"
        inflate.button.setOnClickListener { dialog?.cancel() }
//        makeViewBlurExtendsConstraintLayout(inflate as BlurViewExtendsConstraintLayout, act.window.decorView as ViewGroup, dialog?.window!!)

        builder.setView(inflate)
        return builder
//            .setTitle("添加人员")
            .create()
    }
}

class AddMemberDialogFragment(private val act: Activity) : DialogFragment() {

    private lateinit var inflate: View

    override fun onStart() {

//            因为View在添加后,对话框最外层的ViewGroup并不知道我们导入的View所需要的的宽度。 所以我们需要在onStart生命周期里修改对话框尺寸参数

        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        super.onStart()

        val window = dialog!!.window
//        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        inflate =
//            inflater.inflate(R.layout.card_investigation_plan_detail_member, container, false)
//        inflate.button.text = "取消"
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        makeViewBlurExtendsConstraintLayout(
            inflate as BlurViewExtendsConstraintLayout,
            act.window.decorView as ViewGroup,
            dialog?.window!!
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(act)

        inflate =
            act.layoutInflater.inflate(R.layout.card_investigation_plan_detail_member, null)
        inflate.button.text = "取消"
        inflate.button.setOnClickListener { dialog?.cancel() }
//        makeViewBlurExtendsConstraintLayout(inflate as BlurViewExtendsConstraintLayout, act.window.decorView as ViewGroup, dialog?.window!!)

        builder.setView(inflate)
        return builder
//            .setTitle("添加人员")
            .create()
    }
}


class AddCompanyDialogFragment(private val act: Activity) : DialogFragment() {

    private lateinit var inflate: View

    override fun onStart() {

//            因为View在添加后,对话框最外层的ViewGroup并不知道我们导入的View所需要的的宽度。 所以我们需要在onStart生命周期里修改对话框尺寸参数

        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        super.onStart()

        val window = dialog!!.window
//        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        makeViewBlurExtendsConstraintLayout(
            inflate as BlurViewExtendsConstraintLayout,
            act.window.decorView as ViewGroup,
            dialog?.window!!
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(act)

        inflate =
            act.layoutInflater.inflate(R.layout.card_investigation_plan_detail_company, null)
        inflate.button4.text = "取消"
        inflate.button4.setOnClickListener { dialog?.cancel() }
//        makeViewBlurExtendsConstraintLayout(inflate as BlurViewExtendsConstraintLayout, act.window.decorView as ViewGroup, dialog?.window!!)

        builder.setView(inflate)
        return builder
//            .setTitle("添加企业")
            .create()
    }
}*/


fun showPopupMenu(v: View, activity: Activity, array: Array<String>) {

    XPopup.setPrimaryColor(activity.resources.getColor(R.color.colorAccent))
    val asAttachList = XPopup.Builder(activity)
        .hasShadowBg(false)
        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
        //                        .isDarkTheme(true)
        //                        .popupAnimation(PopupAnimation.NoAnimation) //NoAnimation表示禁用动画
        //                        .isCenterHorizontal(true) //是否与目标水平居中对齐
        .offsetY(activity.dip(activity.dip(-14)))
        .offsetX(activity.dip(0))
        //                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
        .atView(v) // 依附于所点击的View，内部会自动判断在上方或者下方显示
        .asAttachList(array,
            intArrayOf(),
            object : OnSelectListener {
                override fun onSelect(position: Int, text: String) {
                    toast(activity, "click $text")
                }
            })
    eightbitlab.com.blurview.makeViewBlur(
            asAttachList.findViewById(com.lxj.xpopup.R.id.recyclerView),
            activity.window.decorView as ViewGroup, activity.window
    )
//                                .bindLayout(R.layout.my_custom_attach_popup)
    //                        .bindItemLayout(R.layout.my_custom_attach_popup)
    asAttachList.show()

}
