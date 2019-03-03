package com.gmail.gerbencdg.mygoalsassistant.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gmail.gerbencdg.mygoalsassistant.data.GoalRepository
import com.gmail.gerbencdg.mygoalsassistant.databinding.FragmentGoalBinding
import com.gmail.gerbencdg.mygoalsassistant.model.adapter.GoalResultAdapter
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.utils.viewModelFactory
import com.gmail.gerbencdg.mygoalsassistant.viewmodel.GoalVM
import kotlinx.android.synthetic.main.fragment_goal.*

class GoalDetailFragment : Fragment() {

    private lateinit var viewModel: GoalVM
    private lateinit var adapter: GoalResultAdapter

    private var goalId: Int = -1
    private lateinit var goal: Goal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentArgs = arguments
        if (fragmentArgs != null) {
            val args =
                GoalDetailFragmentArgs.fromBundle(fragmentArgs)
            goalId = args.goalId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this,
            viewModelFactory { GoalVM(GoalRepository(), goalId) }).get(GoalVM::class.java)

        val binding = FragmentGoalBinding.inflate(inflater)
        binding.goalVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (goalId != -1) {
            viewModel.getGoalWithResults().observe(this, Observer {
                if (it != null) {
                    if (!::goal.isInitialized) {
                        goal = it.goal
                        setupRecyclerviewAdapter()
                    }
                    if (it.goalResults.isNotEmpty()) {
                        goal.results = it.goalResults.sorted()
                        adapter.submitList(goal.results)
                    }
                }
            })
        }

        addResultButton.setOnClickListener { viewModel.addStubGoalResult(goal.type) }
    }

    private fun setupRecyclerviewAdapter() {
        adapter = GoalResultAdapter(goal)
        goalResultsRecyclerView.adapter = adapter
    }


}