package ru.practicum.android.diploma.util.extensions

import android.text.InputType
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import java.text.NumberFormat
import java.util.Locale

fun EditText.setupThousandSeparatorFormatter(
    onValueChange: (Int?) -> Unit
) {
    inputType = InputType.TYPE_CLASS_NUMBER

    var lastDigits = ""
    var selfChange = false

    addTextChangedListener(
        onTextChanged = { text, _, _, _ ->
            if (!selfChange) {
                val clean = text
                    ?.toString()
                    ?.replace("""\D""".toRegex(), "")
                    ?.toIntOrNull()
                onValueChange(clean)
            }
        },
        afterTextChanged = { editable ->
            if (!selfChange) {
                val digits = editable
                    ?.toString()
                    ?.replace("""\D""".toRegex(), "")
                    .orEmpty()
                if (digits != lastDigits) {
                    lastDigits = digits

                    val formatted = if (digits.isNotEmpty())
                        NumberFormat.getNumberInstance(Locale("ru"))
                            .format(digits.toLong())
                    else
                        ""

                    selfChange = true
                    setText(formatted)
                    setSelection(formatted.length)
                    selfChange = false
                }
            }
        }
    )
}
