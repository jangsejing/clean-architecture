package com.jess.cleanarchitecture.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import com.jess.cleanarchitecture.common.coroutine.StateCoroutine
import com.jess.cleanarchitecture.common.coroutine.StateCoroutineImpl
import com.jess.cleanarchitecture.common.coroutine.State
import com.jess.cleanarchitecture.data.entity.ItemEntity
import com.jess.cleanarchitecture.domain.usecase.SearchMoveUseCase

class MainViewModel @ViewModelInject constructor(
    private val useCase: SearchMoveUseCase
) : ViewModel(), StateCoroutine by StateCoroutineImpl() {

    private val _list = MutableLiveData<List<ItemEntity>>()
    val list: LiveData<List<ItemEntity>> get() = _list

    val isLoading = MediatorLiveData<Boolean>().apply {
        addSource(coroutineState) {
            value = when (it) {
                is State.CoroutinesStarted -> true
                is State.CoroutinesFinished -> false
            }
        }
    }

    fun search(query: String?) {

        if (query.isNullOrEmpty()) {
            return
        }

        viewModelScope.jessLaunch {
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
