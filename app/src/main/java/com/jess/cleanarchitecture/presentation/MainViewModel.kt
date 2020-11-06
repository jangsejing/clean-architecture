package com.jess.cleanarchitecture.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.jess.cleanarchitecture.data.entity.ItemEntity

class MainViewModel @ViewModelInject constructor(

) : ViewModel() {


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
