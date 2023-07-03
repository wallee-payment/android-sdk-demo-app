package com.wallee.samples.apps.shop.compose.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.compose.card
import com.wallee.samples.apps.shop.compose.utils.TextSnackbarContainer
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.viewmodels.ConfigViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfigScreen(
    configViewModel: ConfigViewModel,
    cacheSettings: (Settings) -> Unit,
) {
    val showSnackbar by configViewModel.showSnackbar.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(start = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(Modifier.fillMaxWidth()) {
            ShowHeader(viewModel = configViewModel)
            ShowUserId(viewModel = configViewModel)
            ShowSpaceId(viewModel = configViewModel)
            ShowApplicationKey(configViewModel, keyboardController)
            ShowSaveButton(viewModel = configViewModel, cacheSettings)

            if (showSnackbar) {
                TextSnackbarContainer(
                    snackbarText = stringResource(R.string.added_config),
                    showSnackbar = showSnackbar,
                    onDismissSnackbar = { configViewModel.dismissSnackbar() }
                ) {}
            }
        }
    }
}

@Composable
fun ShowHeader(viewModel: ConfigViewModel) {
    if (viewModel.getUserId().isEmpty() && viewModel.getSpaceId()
            .isEmpty() && viewModel.getAuthenticationKey().isEmpty()
    ) {
        TextHeader(stringResource(id = R.string.config_empty))
    } else {
        TextHeader(stringResource(id = R.string.config_present))
    }
}

@Composable
fun ShowSaveButton(
    viewModel: ConfigViewModel,
    cacheSettings: (Settings) -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = MaterialTheme.shapes.card,
        enabled = viewModel.isEnabledSavedButton.value,
        onClick = {
            viewModel.saveSettings()
            cacheSettings(
                Settings(
                    viewModel.getUserId(),
                    viewModel.getSpaceId(),
                    viewModel.getAuthenticationKey()
                )
            )
        }
    ) {
        Text(
            color = MaterialTheme.colors.onPrimary,
            text = stringResource(id = R.string.add_config)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ShowApplicationKey(
    viewModel: ConfigViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    val appKey by viewModel.appKeyState.collectAsState()

    Column {
        OutlinedTextField(
            value = appKey.property,
            label = { Text(text = stringResource(id = R.string.config_auth_key)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            onValueChange = {
                viewModel.setAuthenticationKey(it)
            },
            isError = appKey.isInvalid
        )
        Text(
            text = appKey.errorMessage,
            color = MaterialTheme.colors.onError
        )
    }
}

@Composable
private fun ShowSpaceId(viewModel: ConfigViewModel) {

    val space by viewModel.spaceState.collectAsState()

    Column {
        OutlinedTextField(
            value = space.property,
            label = { Text(text = stringResource(id = R.string.config_space_id)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                viewModel.setSpaceId(it)
            },
            isError = space.isInvalid
        )
        Text(
            text = space.errorMessage,
            color = MaterialTheme.colors.onError
        )
    }
}

@Composable
private fun ShowUserId(viewModel: ConfigViewModel) {

    val user by viewModel.userState.collectAsState()

    Column {
        OutlinedTextField(
            value = user.property,
            label = { Text(text = stringResource(id = R.string.config_user_id)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                viewModel.setUserId(it)
            },
            isError = user.isInvalid
        )
        Text(
            text = user.errorMessage,
            color = MaterialTheme.colors.onError
        )
    }
}

@Composable
private fun TextHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
    )
    Spacer(modifier = Modifier.size(2.dp))
}