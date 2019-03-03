package com.gmail.gerbencdg.mygoalsassistant.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.DialogFragment
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.view.GoalCreationPage
import com.gmail.gerbencdg.mygoalsassistant.view.GoalCreationPage.PageName

class GoalCreationDialogFragment : DialogFragment() {

    companion object {
        private const val LAST_PAGE = 4
        private const val FIRST_PAGE = 1

        fun newInstance(): GoalCreationDialogFragment {
            return GoalCreationDialogFragment().apply {
                retainInstance = true
            }
        }
    }

    private var currentPage = FIRST_PAGE
    private var goal = Goal()
    private lateinit var goalCreationPage: GoalCreationPage
    private lateinit var dialog: AlertDialog

    lateinit var goalCreationListener: GoalCreationListener
    private lateinit var rootView: View
    private lateinit var contentHostView: LinearLayout

    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var pageIndicatorTextView: TextView

    private lateinit var nextImageButton: ImageButton
    private lateinit var previousImageButton: ImageButton

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (!::goalCreationListener.isInitialized) {
            throw IllegalArgumentException("Please set the goalCreationPage variable in onAttachFragment(childFragment)")
        }

        return activity.let {
            val builder = AlertDialog.Builder(it).apply {
                setView(getCurrentView())
            }
            dialog = builder.create()
            dialog.setOnCancelListener { goalCreationListener.onCreationCancelled() }
            dialog.setOnKeyListener(backPressedListener)
            return@let dialog
        }
    }

    override fun onDestroyView() {
        val dialog = getDialog()
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    private fun getCurrentView(): View {
        initializeView()
        setContent()
        return rootView
    }

    private fun setContent() {

        goalCreationPage = GoalViewFactory.create(requireContext(), getPageFromNumber(currentPage), goal)

        pageIndicatorTextView.text = "$currentPage / $LAST_PAGE"
        titleTextView.text = goalCreationPage.title
        subtitleTextView.text = goalCreationPage.subTitle

        if (contentHostView.childCount != 0) {
            contentHostView.removeAllViews()
        }
        contentHostView.addView(goalCreationPage.contentView)

        if (currentPage == FIRST_PAGE) {
            previousImageButton.visibility = View.GONE
        } else {
            previousImageButton.visibility = View.VISIBLE
        }

        if (currentPage == LAST_PAGE) {
            nextImageButton.setImageDrawable(
                getDrawable(requireContext(), R.drawable.baseline_check_black_24)
            )
        } else {
            nextImageButton.setImageDrawable(
                getDrawable(requireContext(), R.drawable.baseline_navigate_next_black_24)
            )
        }

    }

    private fun initializeView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_new_goal, null)
        contentHostView = rootView.findViewById(R.id.contentHostLinearLayout)

        titleTextView = rootView.findViewById(R.id.titleTextView)
        subtitleTextView = rootView.findViewById(R.id.subtitleTextView)
        pageIndicatorTextView = rootView.findViewById(R.id.pageIndicatorTextView)

        previousImageButton = rootView.findViewById(R.id.previousButton)
        previousImageButton.setOnClickListener {
            previousPage()
        }

        nextImageButton = rootView.findViewById(R.id.nextButton)
        nextImageButton.setOnClickListener {
            if (currentPage == LAST_PAGE) {
                dialog.dismiss()
                goalCreationListener.onCreationCompleted(goal)
            } else {
                nextPage()
            }
        }
    }

    private fun previousPage() {
        if (currentPage <= FIRST_PAGE) return
        currentPage--
        setContent()
    }

    private fun nextPage() {
        if (currentPage >= LAST_PAGE) return
        if (!goalCreationPage.validateInfo()) return
        goalCreationPage.updateGoalInfo()

        currentPage++
        setContent()
    }

    private val backPressedListener = DialogInterface.OnKeyListener {
            _: DialogInterface?, keyCode: Int, event: KeyEvent? ->

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event?.action != KeyEvent.ACTION_DOWN) {
                return@OnKeyListener true
            }
            dialog.dismiss()
            goalCreationListener.onCreationCancelled()
            return@OnKeyListener true
        }
        false
    }

    override fun onDismiss(dialog: DialogInterface) {
        retainInstance = false
        super.onDismiss(dialog)
    }

    private fun getPageFromNumber(pageNumber: Int) = PageName.values()[pageNumber - 1]

    interface GoalCreationListener {
        fun onCreationCompleted(goal: Goal)
        fun onCreationCancelled()
    }
}
