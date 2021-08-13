package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.QuestionsAdapter
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.*
import com.nehaev.keepinmind.util.QuestionsFragmentStates
import kotlinx.android.synthetic.main.fragment_questions.*

class QuestionsFragment : Fragment(R.layout.fragment_questions) {

    lateinit var viewModel: QuestionsViewModel
    lateinit var questionsAdapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setObserver()
        setFabAction()
    }

    private fun createViewModel() {
        val mindRepository =
            MindRepository(MindDatabase(activity as MindActivity))
        val theme = arguments?.getSerializable("Theme") as Theme

        val viewModelFactory = QuestionsViewModelProviderFactory(mindRepository, theme)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionsViewModel::class.java)
    }

    private fun setFabAction() {
        fabAddQuestion.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("Theme", viewModel.theme)
            findNavController().navigate(
                R.id.action_questionsFragment_to_questionCreateFragment,
                bundle
            )
        }
    }

    private fun setObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is QuestionsFragmentStates.Success -> {
                    questionsAdapter.differ.submitList(response.questionsList)
                    hideProgressBar()
                    showList()
                    hideText()
                }
                is QuestionsFragmentStates.EmptyList -> {
                    hideProgressBar()
                    hideList()
                    showText()
                }
                is QuestionsFragmentStates.Loading -> {
                    showProgressBar()
                    hideList()
                    hideText()
                }
            }
        })
    }

    private fun showProgressBar() {
        pbQuestions.visibility = View.VISIBLE
    }

    private fun showText() {
        tvEmptyQuestionsList.visibility = View.VISIBLE
    }

    private fun showList() {
        rvQuestions.visibility = View.VISIBLE
    }


    private fun hideList() {
        rvQuestions.visibility = View.GONE
    }

    private fun hideText() {
        tvEmptyQuestionsList.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbQuestions.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        questionsAdapter = QuestionsAdapter()
        questionsAdapter.setOnItemClickListener { question ->
            onListItemClick(question)
        }

        rvQuestions.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun onListItemClick(question: Question) {
        val bundle = Bundle()
        bundle.putSerializable("Question", question)
        bundle.putSerializable("Theme", viewModel.theme)
        findNavController().navigate(
            R.id.action_questionsFragment_to_questionCreateFragment,
            bundle
        )
    }
}