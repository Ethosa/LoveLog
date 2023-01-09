package com.avocat.lovelog.ui.screen

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.avocat.lovelog.R
import com.avocat.lovelog.Utils
import com.avocat.lovelog.ui.composable.Avatar
import com.avocat.lovelog.ui.theme.LAccent


@Composable
fun SettingsScreen(navController: NavController, preferences: SharedPreferences) {

    // Names
    val newLeftName = remember { mutableStateOf(
        TextFieldValue(preferences.getString(Utils.LEFT_PARTNER, "")!!
    )) }
    val newRightName = remember { mutableStateOf(
        TextFieldValue(preferences.getString(Utils.RIGHT_PARTNER, "")!!
    )) }

    // Avatars
    val newLeftPhoto = remember { mutableStateOf(
        preferences.getString(Utils.LEFT_AVATAR, "")!!
    ) }
    val newRightPhoto = remember { mutableStateOf(
        preferences.getString(Utils.RIGHT_AVATAR, "")!!
    ) }

    // old password
    val pass = remember { mutableStateOf(
        preferences.getString(Utils.PASSWORD, "")!!
    ) }

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.large
    ) {
        // Actions
        Box(Modifier.padding(8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back arrow
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.Outlined.ArrowBack,
                        "back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                // Save settings and exit
                IconButton(onClick = {
                    preferences.edit()
                        .putString(Utils.LEFT_PARTNER, newLeftName.value.text)
                        .putString(Utils.RIGHT_PARTNER, newRightName.value.text)
                        .putString(Utils.RIGHT_AVATAR, newRightPhoto.value)
                        .putString(Utils.LEFT_AVATAR, newLeftPhoto.value)
                        .apply()
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.Outlined.Done,
                        "done",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        // Main content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                LocalContext.current.getString(R.string.settings),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(2.dp, 4.dp)
            )
            Divider(Modifier.width(128.dp))
            // Names and avatars
            Spacer(Modifier.height(8.dp))
            Text(LocalContext.current.getString(R.string.main_title))
            Divider(Modifier.width(256.dp))
            Spacer(Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarChanger(nameToChange = newLeftName, avatarToChange = newLeftPhoto)
                Icon(
                    Icons.Outlined.Favorite,
                    "heart",
                    tint = LAccent,
                    modifier = Modifier.scale(2f)
                )
                AvatarChanger(nameToChange = newRightName, avatarToChange = newRightPhoto)
            }
            Spacer(Modifier.height(24.dp))
            // Security
            Text(LocalContext.current.getString(R.string.security_title))
            Divider(Modifier.width(256.dp))
            Spacer(Modifier.height(24.dp))
            Button(onClick = {
                navController.navigate("passwordSetScreen")
            }) {
                Text(LocalContext.current.getString(R.string.set_pass))
            }
            if (pass.value.isNotEmpty())
                Button(onClick = {
                    navController.navigate("passwordClearScreen")
                }) {
                    Text(LocalContext.current.getString(R.string.clear_pass))
                }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarChanger(
    modifier: Modifier = Modifier,
    nameToChange: MutableState<TextFieldValue>,
    avatarToChange: MutableState<String>,
) {
    val ctx = LocalContext.current.applicationContext
    val pickPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        it?.let {
            avatarToChange.value = it.toString()
            ctx.contentResolver.takePersistableUriPermission(
                it, (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            )
        }
    }

    Column(
        modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Load avatar image
        val img: ImageBitmap? = Utils.uriToImageBitmap(
            Uri.parse(avatarToChange.value), LocalContext.current.applicationContext.contentResolver
        )
        Avatar(
            Modifier.size(64.dp).clickable {
                pickPicture.launch(arrayOf("image/*"))
            },
            img
        )
        TextField(
            modifier = Modifier.wrapContentSize(),
            value = nameToChange.value,
            onValueChange = {
                nameToChange.value = it
            },
            placeholder = {
                Text(
                    LocalContext.current.getString(R.string.name),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            label = {
                Text(
                    LocalContext.current.getString(R.string.name),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
}
