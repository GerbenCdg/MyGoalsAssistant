package com.gmail.gerbencdg.mygoalsassistant.view.fragment

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.data.firebase.FirestoreManager
import com.gmail.gerbencdg.mygoalsassistant.databinding.FragmentGoalsListBinding
import com.gmail.gerbencdg.mygoalsassistant.model.adapter.GoalAdapter
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Quote
import com.gmail.gerbencdg.mygoalsassistant.utils.NetworkBroadcastReceiver
import com.gmail.gerbencdg.mygoalsassistant.utils.NetworkUtils
import com.gmail.gerbencdg.mygoalsassistant.view.dialog.GoalCreationDialogFragment
import com.gmail.gerbencdg.mygoalsassistant.viewmodel.GoalListVM
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_goals_list.*
import kotlinx.android.synthetic.main.item_quote.*


class GoalListFragment : Fragment(), GoalCreationDialogFragment.GoalCreationListener {

    private lateinit var viewModel: GoalListVM
    private lateinit var todayGoalsAdapter: GoalAdapter
    private lateinit var weekGoalsAdapter: GoalAdapter
    private lateinit var oneTimeGoalsAdapter: GoalAdapter

    private lateinit var rootView: View
    private var networkBroadcastReceiver: BroadcastReceiver? = null
    private var isNetworkBroadcastReceiverRegistered = false
    private lateinit var firestoreMgr: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupQuoteFetcherFromFirestore()
    }

    private fun setupQuoteFetcherFromFirestore() {
        firestoreMgr = FirestoreManager(FirebaseFirestore.getInstance())

        networkBroadcastReceiver = NetworkBroadcastReceiver(
            object : NetworkBroadcastReceiver.NetworkAvailableCallBack {
                override fun onNetworkAvailable() {
                    if (isNetworkBroadcastReceiverRegistered) fetchAndShowRandomQuote()
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(GoalListVM::class.java)

        val binding = FragmentGoalsListBinding.inflate(inflater)
        binding.goalListVM = viewModel
        binding.lifecycleOwner = this
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        setupGoalAdapters(navController)
        setupGoalCreationClickListeners()
        setQuoteRefreshButtonClickListener()

        viewModel.getGoals().observe(this, Observer {
            if (it != null) {
                todayGoalsAdapter.submitList(it)
                weekGoalsAdapter.submitList(it)
                oneTimeGoalsAdapter.submitList(it)
            }
        })

        if (quoteTextView.text.isEmpty()) {
            fetchAndShowRandomQuote()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is GoalCreationDialogFragment) {
            childFragment.goalCreationListener = this
        }
        super.onAttachFragment(childFragment)
    }

    private fun setupGoalAdapters(navController: NavController) {

        val navigateToGoalItemFragment: (Int) -> Unit = { goalId: Int ->
            val directions =
                GoalListFragmentDirections
                    .actionGoalListFragmentToGoalDetailFragment(goalId)

            navController.navigate(directions)
        }

        val updateGoalResultCallback = object : GoalAdapter.UpdateGoalResultCallback {
            override fun onGoalResultUpdate(goalResult: GoalResult) {
                viewModel.updateGoalResult(goalResult)
            }
        }

        todayGoalsAdapter = GoalAdapter(
            navigateToGoalItemFragment,
            updateGoalResultCallback,
            viewModel.todayGoalsPredicate
        )

        weekGoalsAdapter = GoalAdapter(
            navigateToGoalItemFragment,
            updateGoalResultCallback,
            viewModel.weekGoalsPredicate
        )

        oneTimeGoalsAdapter = GoalAdapter(
            navigateToGoalItemFragment,
            updateGoalResultCallback,
            viewModel.oneTimeGoalsPredicate
        )

        todayGoalsRecyclerView.adapter = todayGoalsAdapter
        weekGoalsRecyclerView.adapter = weekGoalsAdapter
        oneTimeGoalsRecyclerView.adapter = oneTimeGoalsAdapter
    }

    private fun setupGoalCreationClickListeners() {
        val showGoalCreationDialog = {
            val dialogFragment = GoalCreationDialogFragment.newInstance()
            dialogFragment.show(childFragmentManager, "new_goal")
        }
        fab.setOnClickListener { showGoalCreationDialog() }
        createFirstGoalButton.setOnClickListener { showGoalCreationDialog() }
    }


    private fun setQuoteRefreshButtonClickListener() {
        refreshQuoteImageButton.setOnClickListener {
            fetchAndShowRandomQuote()
        }
    }

    override fun onCreationCompleted(goal: Goal) {
        viewModel.insertGoal(goal)
        Snackbar.make(
            rootView,
            getString(com.gmail.gerbencdg.mygoalsassistant.R.string.goal_created),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCreationCancelled() {
        Snackbar.make(
            rootView,
            getString(com.gmail.gerbencdg.mygoalsassistant.R.string.goal_creation_cancelled),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun fetchAndShowRandomQuote() {

        val onFailure = {
            if (quoteTextView != null) {
                if (!NetworkUtils.isConnected(context)) {
                    quoteTextView.text = getString(R.string.no_internet_unable_to_receive_quote)
                    quoteAuthorTextView.text = ""
                } else {
                    quoteTextView.text = getString(R.string.unable_to_receive_quote)
                    quoteAuthorTextView.text = ""
                }
            }
        }

        val onSuccess: (quote: Quote) -> Unit = {
            if (quoteTextView != null) {
                quoteTextView.text = it.text
                quoteAuthorTextView.text = "― " + it.author
            }
        }

        firestoreMgr.fetchQuote(onSuccess, onFailure)
    }


    override fun onResume() {
        registerNetworkBroadcastReceiver()
        super.onResume()
    }

    override fun onPause() {
        unregisterNetworkBroadcastReceiver()
        super.onPause()
    }

    override fun onDestroy() {
        networkBroadcastReceiver = null
        super.onDestroy()
    }

    private fun registerNetworkBroadcastReceiver() {
        context?.registerReceiver(
            networkBroadcastReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
        isNetworkBroadcastReceiverRegistered = true
    }

    private fun unregisterNetworkBroadcastReceiver() {
        networkBroadcastReceiver?.let {
            requireContext().unregisterReceiver(it)
            isNetworkBroadcastReceiverRegistered = false
        }
    }

}