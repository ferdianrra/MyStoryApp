package com.dicoding.mystoryapp.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.dicoding.mystoryapp.R
import com.google.android.material.textfield.TextInputEditText

class MyEmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pattern = android.util.Patterns.EMAIL_ADDRESS
                if (!pattern.matcher(s.toString()).matches()) {
                    setError(resources.getString(R.string.format_email_error), null)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}