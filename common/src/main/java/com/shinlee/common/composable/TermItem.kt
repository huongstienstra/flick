package com.shinlee.common.composable

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.shinlee.common.theme.PinkColor
import com.shinlee.common.theme.WhiteColor

@Composable
fun TermItem(
    modifier: Modifier,
    value: String,
    valueClick: String,
    isChecked: Boolean,
    onTextSelected: (String) -> Unit,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange.invoke()
            },
            colors = CheckboxDefaults.colors(
                checkedColor = PinkColor,
                uncheckedColor = PinkColor,
                checkmarkColor = WhiteColor,
            )
        )


        ClickableTextComponent(value, valueClick, onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(
    firstValue: String,
    valueClick: String,
    onTextSelected: (String) -> Unit
) {

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black, fontSize = 20.sp)) {
            append(firstValue)
        }

        withStyle(style = SpanStyle(color = PinkColor, fontSize = 20.sp)) {
            pushStringAnnotation(tag = valueClick, annotation = valueClick)
            append(valueClick)
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->

        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if (span.item == valueClick) {
                    onTextSelected(span.item)
                }
            }

    })
}