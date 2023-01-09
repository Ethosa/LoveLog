package com.avocat.lovelog.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.avocat.lovelog.R


@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap? = null
) {
    if (imageBitmap == null) {
        Image(
            ImageVector.vectorResource(R.drawable.ic_loveloglighticon),
            null,
            modifier
                .height(96.dp)
                .width(96.dp)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.background, CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            imageBitmap,
            null,
            modifier
                .height(96.dp)
                .width(96.dp)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.background, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}