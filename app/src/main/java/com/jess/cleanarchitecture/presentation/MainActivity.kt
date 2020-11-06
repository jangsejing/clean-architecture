package com.jess.cleanarchitecture.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jess.cleanarchitecture.R
import com.jess.cleanarchitecture.common.adapter.BaseListAdapter
import com.jess.cleanarchitecture.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy {
        BaseListAdapter(
            R.layout.main_item,
            viewModel.diffCallback
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        initView()
        initComponent()
    }

    private fun initView() {
        binding.rvMovie.adapter = adapter
    }

    private fun initComponent() {
        binding.viewModel = viewModel
        viewModel.run {
            search("mar")
        }
    }
}