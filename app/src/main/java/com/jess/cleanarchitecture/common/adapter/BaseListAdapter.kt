package com.jess.cleanarchitecture.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jess.cleanarchitecture.BR

class BaseListAdapter<T : Any>(
    @LayoutRes private val layoutId: Int,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, ViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<T> {
        require(layoutId > 0) { "Empty Layout Resource" }
        val dataBinding = createViewDataBinding(parent)
        return createViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.onBind(getItem(position))
    }

    private fun createViewDataBinding(parent: ViewGroup): ViewDataBinding {
        return DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    private fun createViewHolder(dataBinding: ViewDataBinding): ViewHolder<T> {
        return ViewHolder(
            dataBinding
        )
    }
}

class ViewHolder<T : Any?>(
    private val viewDataBinding: ViewDataBinding
) : RecyclerView.ViewHolder(viewDataBinding.root) {

    fun onBind(item: T?) {
        viewDataBinding.run {
            setVariable(BR.item, item)
            executePendingBindings()
        }
    }
}