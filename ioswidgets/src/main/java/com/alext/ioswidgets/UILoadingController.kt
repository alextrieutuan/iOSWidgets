package com.axonvibe.sojocommon.widget.ioscosplay

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.alext.ioswidgets.R

class UILoadingController constructor(
    context: Context,
    cancelable: Boolean = false
) {
    val dialog: Dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.layout_ui_alert_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
    }

    fun show() {
        dialog.show()
    }

}
