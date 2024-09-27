package com.shinlee.common.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.theme.AccentColor
import com.shinlee.common.theme.GrayColor
import com.shinlee.common.theme.PinkColor
import com.shinlee.common.theme.WhiteColor

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
   Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(AccentColor, PinkColor)),
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun ButtonIconComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
   Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor)
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        border = BorderStroke(1.dp, GrayColor),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(Color.White),
        enabled = isEnabled,

    ) {
       Box(
           modifier = Modifier
               .background(WhiteColor)
               .fillMaxWidth()
               .heightIn(48.dp),
           contentAlignment = Alignment.Center
       ) {

           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .heightIn(48.dp),
               contentAlignment = Alignment.CenterStart
           ) {
               Icon(
                   imageVector = Icons.Default.ArrowBack,
                   contentDescription = null,
                   tint = Color(0xFF039be5)
               )
           }

           Text(
               text = value,
               fontSize = 18.sp,
               color = Color.Black,
               fontWeight = FontWeight.Bold,
               textAlign = TextAlign.Center
           )
       }

//       Box(
//           modifier = Modifier
//               .fillMaxWidth()
//               .padding(start = 2.dp)
//           .background(
//               color = WhiteColor,
//       shape = RoundedCornerShape(15.dp)
//       ),
//           contentAlignment = Alignment.CenterStart)
//       {
//               Icon(
//                   imageVector = Icons.Default.ArrowBack,
//                   contentDescription = null,
//                   tint = Color(0xFF039be5)
//               )
//
//       }

    }
}
