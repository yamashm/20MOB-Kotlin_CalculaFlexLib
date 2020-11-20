package br.com.example.calculaflexlib.components.customdialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.method.LinkMovementMethod
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import br.com.example.calculaflexlib.R
import br.com.example.calculaflexlib.extensions.fromHtml

class CustomDialog : View.OnClickListener {
    private lateinit var ivAlertDialogImage: ImageView
    private lateinit var tvAlertDialogTitle: TextView
    private lateinit var tvAlertDialogMessage: TextView
    private lateinit var btAlertDialogConfirm: TextView
    private var dialog: Dialog? = null
    private var activity: Activity? = null

    fun showDialog(
        activity: Activity,
        resId: Int?,
        title: String?,
        msg: String?,
        buttonPrimaryText: String?,
        primaryClickListener: View.OnClickListener?,
        isCancelable: Boolean
    ) {
        createDialog(
            activity, resId, title, msg, buttonPrimaryText,
            primaryClickListener, isCancelable
        )
    }

    private fun createDialog(
        activity: Activity,
        isCancelable: Boolean
    ) {
        this.activity = activity
        dialog = Dialog(activity, R.style.Dialog_No_Border)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(isCancelable)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setContentView(R.layout.custom_dialog)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog?.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = lp
    }

    private fun createDialog(
        activity: Activity,
        resId: Int?,
        title: String?,
        msg: String?,
        buttonPrimaryText: String?,
        primaryClickListener: View.OnClickListener?,
        isCancelable: Boolean
    ) {
        if (dialog != null) {
            dismissDialog()
        }
        createDialog(activity, isCancelable)
        ivAlertDialogImage = dialog?.findViewById(R.id.ivAlertDialogImage) as ImageView
        tvAlertDialogTitle = dialog?.findViewById(R.id.tvAlertDialogTitle) as TextView
        tvAlertDialogMessage = dialog?.findViewById(R.id.tvAlertDialogMessage) as TextView
        btAlertDialogConfirm = dialog?.findViewById(R.id.btAlertDialogConfirm) as TextView
        resId?.let {
            val bm = ResourcesCompat.getDrawable(activity.resources, it, null)
            ivAlertDialogImage.setImageDrawable(bm)
        }

        tvAlertDialogTitle.fromHtml(title)
        tvAlertDialogTitle.movementMethod = LinkMovementMethod.getInstance()
        tvAlertDialogMessage.fromHtml(msg?.replace("\n", "<br />"))
        tvAlertDialogMessage.movementMethod = LinkMovementMethod.getInstance()
        btAlertDialogConfirm.text = buttonPrimaryText ?: "OK"
        if (primaryClickListener == null) {
            btAlertDialogConfirm.visibility = View.GONE
            btAlertDialogConfirm.setOnClickListener { dialog?.dismiss() }
        } else {
            btAlertDialogConfirm.visibility = View.VISIBLE
            btAlertDialogConfirm.setOnClickListener(primaryClickListener)
        }
        if (isCancelable) {
            dialog?.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP)
                    dialog.dismiss()
                false
            }
        }

        if (!(activity).isFinishing) {
            if (dialog?.isShowing == false) {
                dialog?.show()
            }
        }
    }

    fun dismissDialog() {
        if (dialog != null) {
            if (!isActivityFinish() && dialog!!.isShowing) {
                dialog?.dismiss()
                dialog = null
            }
        }
    }

    private fun isActivityFinish(): Boolean {
        if (activity == null)
            return true
        return if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.JELLY_BEAN_MR1
        ) {
            activity?.isDestroyed!!
        } else {
            activity?.isFinishing!!
        }
    }

    override fun onClick(view: View) {
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }

        fun isShowing(): Boolean? {
            if (dialog == null) return false
            return dialog?.isShowing
        }
    }
}