package com.jess.cleanarchitecture.common.extension

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * HTML
 *
 * @param source
 * @return
 */
@BindingAdapter("fromHtml")
fun TextView.fromHtml(source: String?) {
    if (!source.isNullOrEmpty()) {
        this.text = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(source)
        } else {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        }
    }
}