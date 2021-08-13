package com.nehaev.keepinmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.ui.viewmodels.MindViewModel
import com.nehaev.keepinmind.ui.viewmodels.MindViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_mind.*

class MindActivity : AppCompatActivity() {

    lateinit var viewModel: MindViewModel
    lateinit var mindRepository: MindRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind)
        // Create main repository
        mindRepository = MindRepository(
            db = MindDatabase(this)
        )
        // create view model
        val viewModelProviderFactory = MindViewModelProviderFactory(mindRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(MindViewModel::class.java)
        bottomNavigationView.setupWithNavController(mindNavHostFragment.findNavController())
    }
}