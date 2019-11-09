package com.alext.ioswidgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView

class UIAlertController private constructor(
    context: Context,
    private val title: String?,
    private val message: String?,
    cancelable: Boolean = true,
    private val dialogActions: ArrayList<UIAlertAction>
) {
    val dialog: Dialog
    private val dialogStyle: UIAlertDialogType

    init {
        dialogActions.sortWith(Comparator { action1, action2 ->
            action1.style.orderNumber.compareTo(action2.style.orderNumber)
        })

        val dialogLayout = when (dialogActions.size) {
            1 -> {
                dialogStyle = UIAlertDialogType.ONE
                R.layout.layout_ui_alert_one_button
            }

            2 -> {
                dialogStyle = UIAlertDialogType.TWO
                R.layout.layout_ui_alert_two_buttons
            }

            in 3..Int.MAX_VALUE -> {
                dialogStyle = UIAlertDialogType.MORE
                R.layout.layout_ui_alert_more_buttons
            }

            else -> {
                throw IllegalArgumentException("We currently don't support dialog without any action")
            }
        }

        dialog = Dialog(context)
        dialog.setContentView(dialogLayout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
        initView()
    }

    fun show() {
        dialog.show()
    }

    private fun Context.dpToPixel(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)
    }

    private fun initView() {
        val textTitle = dialog.findViewById<TextView?>(R.id.text_dialog_title)
        val textMessage = dialog.findViewById<TextView?>(R.id.text_dialog_message)
        title?.let {
            textTitle?.visibility = View.VISIBLE
            textTitle?.text = it
        } ?: kotlin.run {
            textTitle?.visibility = View.GONE
        }
        message?.let {
            textMessage?.visibility = View.VISIBLE
            textMessage?.text = it
        } ?: kotlin.run {
            textMessage?.visibility = View.GONE
        }
        when (dialogStyle) {
            UIAlertDialogType.ONE -> initViewForOne()
            UIAlertDialogType.TWO -> initViewForTwo()
            UIAlertDialogType.MORE -> initViewForMore()
        }
    }

    private fun initViewForOne() {
        val actionView = dialog.findViewById<TextView?>(R.id.text_dialog_action)
            ?: throw IllegalArgumentException("Could not find view with id ${R.id.text_dialog_action}")
        val action = dialogActions.first()
        actionView.setOnClickListener {
            action.doAction?.invoke()
            dialog.dismiss()
        }
        actionView.text = action.text
        actionView.setTextColor(action.style.textColor)
        actionView.typeface = Typeface.DEFAULT_BOLD
        actionView.setSelectableItemBackground()
    }

    private fun initViewForTwo() {
        val firstView = dialog.findViewById<TextView?>(R.id.text_dialog_action_first)
            ?: throw IllegalArgumentException("Could not find view with id ${R.id.text_dialog_action_first}")

        val secondView = dialog.findViewById<TextView?>(R.id.text_dialog_action_second)
            ?: throw IllegalArgumentException("Could not find view with id ${R.id.text_dialog_action_second}")

        val first = dialogActions[0]
        val second = dialogActions[1]

        firstView.setTextColor(first.style.textColor)
        firstView.typeface = Typeface.DEFAULT
        firstView.text = first.text
        firstView.setSelectableItemBackground()
        firstView.setOnClickListener {
            first.doAction?.invoke()
            dialog.dismiss()
        }

        secondView.setTextColor(second.style.textColor)
        secondView.typeface = Typeface.DEFAULT_BOLD
        secondView.text = second.text
        secondView.setSelectableItemBackground()
        secondView.setOnClickListener {
            second.doAction?.invoke()
            dialog.dismiss()
        }
    }

    @Suppress("LocalVariableName")
    private fun initViewForMore() {
        val container = dialog.findViewById<LinearLayout?>(R.id.layout_action_container)
            ?: throw IllegalArgumentException("Could not find view with id ${R.id.layout_action_container}")
        container.removeAllViews()
        dialogActions.forEachIndexed { index, action ->
            val actionView = TextView(container.context)
            val lp = LinearLayout.LayoutParams(MATCH_PARENT, container.context.dpToPixel(50.0f).toInt())
            actionView.layoutParams = lp
            actionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
            actionView.setOnClickListener {
                action.doAction?.invoke()
                dialog.dismiss()
            }
            actionView.setTextColor(action.style.textColor)
            actionView.text = action.text
            actionView.gravity = Gravity.CENTER
            actionView.setSelectableItemBackground()
            actionView.typeface = Typeface.DEFAULT

            container.addView(actionView)

            if (index == dialogActions.size - 1) {
                actionView.typeface = Typeface.DEFAULT_BOLD
            } else {
                val divider = View(container.context)
                divider.setBackgroundColor(DIVIDER_BG_COLOR)
                divider.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, container.context.dpToPixel(1.0f).toInt())
                container.addView(divider)
            }
        }
    }

    private fun View.setSelectableItemBackground() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        this.setBackgroundResource(outValue.resourceId)
    }

    class UIAlertAction(
        val text: String,
        val style: UIAlertActionStyle = UIAlertActionStyle.DEFAULT,
        val doAction: (() -> Unit)? = null
    )

    private enum class UIAlertDialogType {
        ONE, TWO, MORE
    }

    enum class UIAlertActionStyle : UIAlertDialogStyleAttrs {
        DEFAULT {
            override val orderNumber: Int
                get() = 0

            override val textColor: Int
                get() = DEFAULT_ACTION_TEXT_COLOR
        },

        DESTRUCTIVE {
            override val orderNumber: Int
                get() = 1

            override val textColor: Int
                get() = DESTRUCTIVE_ACTION_TEXT_COLOR
        },

        CANCEL {
            override val orderNumber: Int
                get() = 2

            override val textColor: Int
                get() = CANCEL_ACTION_TEXT_COLOR
        }
    }

    private interface UIAlertDialogStyleAttrs {
        val textColor: Int

        val orderNumber: Int
    }

    class Builder(private val context: Context) {
        private var actions : ArrayList<UIAlertAction> = arrayListOf()
        private var title: String? = null
        private var message: String? = null
        private var cancelable: Boolean = true

        fun withTitle(title: String) = this.apply {
            this.title = title
        }

        fun withMessage(message: String) = this.apply {
            this.message = message
        }

        fun cancellable(cancelable: Boolean) = this.apply {
            this.cancelable = cancelable
        }

        fun addAction(action: UIAlertAction) = this.apply {
            this.actions.add(action)
        }

        fun withActions(action: List<UIAlertAction>) = this.apply {
            actions.clear()
            actions.addAll(action)
        }

        fun build() = UIAlertController(context, title, message, cancelable, actions)

        fun buildAsDialog() = build().dialog

        fun show() = build().show()
    }

    companion object {
        private val DEFAULT_ACTION_TEXT_COLOR = Color.parseColor("#007aff")
        private val CANCEL_ACTION_TEXT_COLOR = Color.parseColor("#007aff")
        private val DESTRUCTIVE_ACTION_TEXT_COLOR = Color.parseColor("#e72b42")
        private val DIVIDER_BG_COLOR = Color.parseColor("#cdced2")
    }

}
