package com.wallee.samples.apps.shop.compose.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.compose.card
import com.wallee.samples.apps.shop.compose.utils.TextSnackbarContainer
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.viewmodels.ConfigViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfigScreen(
    configViewModel: ConfigViewModel = hiltViewModel(),
    cacheSettings: (Settings) -> Unit,
) {
    val showSnackbar = configViewModel.showSnackbar.observeAsState().value
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

            if (showSnackbar != null) {
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

    Column {
        OutlinedTextField(
            value = viewModel.applicationKey.value,
            label = { Text(text = stringResource(id = R.string.config_auth_key)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            onValueChange = {
                viewModel.applicationKey.value = it
                viewModel.validateApplicationKey()
            },
            isError = viewModel.isApplicationKeyValid.value
        )
        Text(
            text = viewModel.applicationKeyErrMsg.value,
            color = MaterialTheme.colors.onError
        )
    }
}

@Composable
private fun ShowSpaceId(viewModel: ConfigViewModel) {

    Column {
        OutlinedTextField(
            value = viewModel.spaceId.value,
            label = { Text(text = stringResource(id = R.string.config_space_id)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                viewModel.spaceId.value = it
                viewModel.validateSpaceId()
            },
            isError = viewModel.isSpaceIdValid.value
        )
        Text(
            text = viewModel.spaceIdErrMsg.value,
            color = MaterialTheme.colors.onError
        )
    }
}

@Composable
private fun ShowUserId(viewModel: ConfigViewModel) {

    Column {
        OutlinedTextField(
            value = viewModel.userId.value,
            label = { Text(text = stringResource(id = R.string.config_user_id)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                viewModel.userId.value = it
                viewModel.validateUserId()
            },
            isError = viewModel.isUserIdValid.value
        )
        Text(
            text = viewModel.userIdErrMsg.value,
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