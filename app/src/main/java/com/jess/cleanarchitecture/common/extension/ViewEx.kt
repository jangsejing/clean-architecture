package com.jess.cleanarchitecture.common.extension

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(isVisible: Boolean) {
    this.isVisible = isVisible
}