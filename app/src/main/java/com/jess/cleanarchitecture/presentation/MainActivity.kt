package com.jess.cleanarchitecture.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jess.cleanarchitecture.R
import com.jess.cleanarchitecture.common.adapter.BaseListAdapter
import com.jess.cleanarchitecture.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        binding.etSearch.addTextChangedListener {
            viewModel.search(it.toString())
        }
    }

    private fun initComponent() {
        binding.viewModel = viewModel
        viewModel.run {
            list.observe(this@MainActivity, Observer {
                adapter.submitList(it)
            })

            isLoading.observe(this@MainActivity, Observer {
                Timber.d("isLoading $it")
            })
        }
    }
}