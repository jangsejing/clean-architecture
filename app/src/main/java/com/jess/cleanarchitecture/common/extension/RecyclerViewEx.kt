package com.jess.cleanarchitecture.common.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jess.cleanarchitecture.common.adapter.BaseListAdapter

@Suppress("UNCHECKED_CAST")
@BindingAdapter("submitList")
fun RecyclerView.submitList(
    items: List<Any>?
) {
    (this.adapter as? BaseListAdapter<Any>)?.run {
        if (!items.isNullOrEmpty()) {
            submitList(items)
        }
    }
}