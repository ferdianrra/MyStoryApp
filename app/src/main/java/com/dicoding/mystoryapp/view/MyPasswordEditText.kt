package com.dicoding.mystoryapp.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dicoding.mystoryapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    private var errorTV: TextView? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parentLayout = parent as? ViewGroup
                parentLayout?.let {
                    val textInputLayout = it.parent as? TextInputLayout
                    errorTV = textInputLayout?.findViewById(R.id.tvErrorPassword)
                    if (s.toString().length < 8) {
                        (parent.parent as TextInputLayout).apply {
                            errorTV?.visibility = View.VISIBLE
                            errorTV?.text = context.getString(R.string.error_password)
                        }
                    } else {
                        errorTV?.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }



}