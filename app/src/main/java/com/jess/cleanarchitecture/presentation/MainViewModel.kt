package com.jess.cleanarchitecture.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.jess.cleanarchitecture.data.entity.ItemEntity
import com.jess.cleanarchitecture.data.entity.MovieEntity
import com.jess.cleanarchitecture.domain.usecase.SearchMoveUseCase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val useCase: SearchMoveUseCase
) : ViewModel() {

    private val _list = MutableLiveData<List<ItemEntity>>()
    val list: LiveData<List<ItemEntity>> get() = _list

    fun search(query: String?) {
        if (query.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch {
            useCase.invoke(query).apply {
                _list.postValue(this.items)
            }
        }
    }

    val diffCallback =
        object : DiffUtil.ItemCallback<ItemEntity>() {

            override fun areItemsTheSame(
                oldItem: ItemEntity,
                newItem: ItemEntity
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: ItemEntity,
                newItem: ItemEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
}
