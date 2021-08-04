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
import kotlinx.android.synthetic.main.activity_mind.*
import kotlinx.android.synthetic.main.fragment_create_question.*
import kotlinx.android.synthetic.main.dialog_enter_name.*
import kotlin.math.min

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
        val mindRepository = MindRepository(MindDatabase(activity as MindActivity))
        val viewModelFactory = MindViewModelProviderFactory(mindRepository = mindRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionCreateViewModel::class.java)

        theme?.let {
            viewModel.themeId = it.id
        }
        viewModel.question = question
    }

}