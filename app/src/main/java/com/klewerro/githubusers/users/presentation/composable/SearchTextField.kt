package com.klewerro.githubusers.users.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.klewerro.githubusers.R
import com.klewerro.githubusers.ui.theme.GithubUsersTheme

@Composable
fun SearchTextField(
    searchQuery: String,
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        searchQuery,
        onValueChange = onValueChange,
        singleLine = true,
        trailingIcon = {
            FilledIconButton(
                enabled = isEnabled,
                onClick = {
                    onSearchClick()
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(
                        R.string.search_for_users_using_provided_username
                    ),
                    tint = MaterialTheme.colorScheme.onPrimary

                )
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
                keyboardController?.hide()
                defaultKeyboardAction(ImeAction.Search)
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.colors(),
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
private fun SearchTextFieldPreview() {
    GithubUsersTheme {
        SearchTextField(
            searchQuery = "",
            isEnabled = false,
            onValueChange = {},
            onSearchClick = {},
            modifier = Modifier
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchTextFieldEnabledPreview() {
    GithubUsersTheme {
        SearchTextField(
            searchQuery = "Text",
            isEnabled = true,
            onValueChange = {},
            onSearchClick = {},
            modifier = Modifier
        )
    }
}
