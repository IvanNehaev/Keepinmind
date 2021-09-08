package com.nehaev.keepinmind.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nehaev.keepinmind.MindActivity
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import com.nehaev.keepinmind.ui.viewmodels.QuestionCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_question.*


class QuestionCreateFragment : Fragment(R.layout.fragment_create_question) {

    lateinit var viewModel: QuestionCreateViewModel

    var theme: Theme? = null
    var question: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme = arguments?.getSerializable("Theme") as Theme
        question = arguments?.getSerializable("Question") as Question?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel()
        setObserver()
        setupQuestionEditText()
        setupAnswerEditText()
        setupSaveButton()
        setupDeleteButton()
    }

    private fun setupDeleteButton() {
        question?.let {
            fragment_create_question_btn_delete_question.text = context?.resources?.getString(R.string.delete_text)
        }
        fragment_create_question_btn_delete_question.setOnClickListener {
            viewModel.onButtonDeleteClick()
        }
    }

    private fun setupSaveButton() {
        btnSaveQuestion.setOnClickListener {
            viewModel.onButtonSaveClick()
        }
    }

    private fun setupAnswerEditText() {
        etAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onAnswerTextChanged(etAnswer.text.toString())
            }
        })
        // if we edit existed question then set question text in edit text
        question?.let {
            etAnswer.setText(it.answer, TextView.BufferType.EDITABLE)
            viewModel.answerText = it.answer
        }
    }

    private fun setupQuestionEditText() {
        etQuestion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onQuestionTextChanged(etQuestion.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        question?.let {
            etQuestion.setText(it.question, TextView.BufferType.EDITABLE)
            viewModel.questionText = it.question
        }
    }

    private fun setObserver() {
        viewModel.liveData.observe( viewLifecycleOwner, Observer { state ->

            when(state) {
                // Invalid question
                is QuestionCreateViewModel.ViewStates.InvalidQuestion -> {
                    disableSaveButton()
                }
                // Valid question
                is QuestionCreateViewModel.ViewStates.ValidQuestion -> {
                    enableSaveButton()
                }
                // Save question
                is QuestionCreateViewModel.ViewStates.SaveQuestion -> {
                    val bundle = Bundle()
                    bundle.putSerializable("Theme", theme)
                    findNavController().navigate(R.id.questionsFragment, bundle)
                }
                // Cancel question
                is QuestionCreateViewModel.ViewStates.CancelQuestion -> {
                    val bundle = Bundle()
                    bundle.putSerializable("Theme", theme)
                    findNavController().navigate(R.id.questionsFragment, bundle)
                }
                // Delete question
                is QuestionCreateViewModel.ViewStates.DeleteQuestion -> {
                    val bundle = Bundle()
                    bundle.putSerializable("Theme", theme)
                    findNavController().navigate(R.id.questionsFragment, bundle)
                }
            }
        })
    }

    private fun enableSaveButton() {
        btnSaveQuestion.isEnabled = true
    }

    private fun disableSaveButton() {
        btnSaveQuestion.isEnabled = false
    }

    private fun createViewModel() {
        val mindRepository =
            MindRepository(MindDatabase(activity as MindActivity))
        val viewModelFactory = MindViewModelProviderFactory(mindRepository = mindRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionCreateViewModel::class.java)

        theme?.let {
            viewModel.themeId = it.id
        }
        viewModel.question = question
    }

}