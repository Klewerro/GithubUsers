package com.klewerro.githubusers.core.presentation.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.klewerro.githubusers.ui.theme.GithubUsersTheme

@Composable
fun TextIcon(
    @DrawableRes resourceId: Int,
    text: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Row(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(resourceId),
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(
                with(LocalDensity.current) {
                    textStyle.fontSize.toDp()
                }
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = textStyle,
            color = contentColor
        )
    }
}

@Composable
fun TextIcon(
    imageVector: ImageVector,
    text: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Row(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(
                with(LocalDensity.current) {
                    textStyle.fontSize.toDp()
                }
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = textStyle, color = contentColor)
    }
}

@PreviewLightDark
@Composable
private fun TextIconPreview() {
    GithubUsersTheme {
        TextIcon(
            imageVector = Icons.Outlined.RemoveRedEye,
            text = 101.toString(),
            contentDescription = null
        )
    }
}
