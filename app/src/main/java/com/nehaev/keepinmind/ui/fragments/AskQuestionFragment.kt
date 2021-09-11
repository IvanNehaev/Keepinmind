package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.adapters.AskQuestionAdapter
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.AskQuestionViewModel
import com.nehaev.keepinmind.ui.viewmodels.MyViewModelProviderFactory
import com.nehaev.keepinmind.util.ViewModelFactoryContainer
import kotlinx.android.synthetic.main.fragment_ask_question.*

class AskQuestionFragment : Fragment(R.layout.fragment_ask_question) {
    private val TAG = AskQuestionFragment::class.simpleName

    private val mAdapter: AskQuestionAdapter by lazy {
        AskQuestionAdapter()
    }
    lateinit var  mViewModel: AskQuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setObserver()
    }

    private fun setObserver() {
        mViewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                // Loading state
                is AskQuestionViewModel.AskQuestionViewState.Loading -> {

                }
                // Questions loaded state
                is AskQuestionViewModel.AskQuestionViewState.QuestionsLoaded -> {
                    mAdapter.differ.submitList(state.questionsList)
                }
            }
        })
    }

    private fun createViewModel() {
        val mindRepository =
            MindRepository(MindDatabase(activity as MindActivity))
        val test = arguments?.getSerializable("Test") as Test
        val viewModelFactory =
            MyViewModelProviderFactory(
                mindRepository,
                ViewModelFactoryContainer(data = test)
            )
        mViewModel = ViewModelProvider(this, viewModelFactory).get(AskQuestionViewModel::class.java)
    }

    private fun setupViewPager() {
//        val questions = listOf<Question>(
//            Question(
//                id = "1",
//                themeId = "1",
//                question = "How are you?",
//                answer = "Fine"
//            ),
//            Question(
//                id = "2",
//                themeId = "2",
//                question = "Where are you?",
//                answer = "Right behind you!"
//            ),
//            Question(
//                id = "3",
//                themeId = "3",
//                question = "What are you doing?",
//                answer = "Killing you!"
//            )
//        )
        fragment_ask_question_viewPager.adapter = mAdapter
//        mAdapter.differ.submitList(questions)
    }
}