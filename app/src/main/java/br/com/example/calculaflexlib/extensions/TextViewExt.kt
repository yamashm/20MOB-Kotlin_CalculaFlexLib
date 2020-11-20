package br.com.example.calculaflexlib.extensions

import android.os.Build
import android.text.Html
import android.widget.TextView

fun TextView.fromHtml(text: String?) {
    when {
        text == null -> this.text = ""
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> this.text =
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        else -> this.text = Html.fromHtml(text)
    }
}