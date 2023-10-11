package routing.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CustomTextField
import components.MyIconButton
import core.db.models.ModelPlaylist
import ru.alexgladkov.odyssey.compose.controllers.ModalController
import theme.ColorBox

@Composable
fun CreatePlaylistDialog(
    modalController: ModalController,
    modelPlaylist: ModelPlaylist,
    callback : (ModelPlaylist) -> Unit
) {

    var name by remember { mutableStateOf(modelPlaylist.name) }

    Column(
        modifier = Modifier.width(400.dp).background(ColorBox.primaryDark2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).background(color = ColorBox.primaryDark.copy(0.5f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(3.dp))
            MyIconButton(
                contentPadding = 10.dp,
                colorFilter = ColorBox.text.copy(0.6f),
                icon = "icons/close.svg",
                onClick = {
                    modalController.popBackStack(null)
                }
            )
            Text(
                modifier = Modifier.padding(start = 12.dp).weight(1f),
                text = if (modelPlaylist.id == null) "Create playlist" else "Edit playlist",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier.padding(end = 8.dp).height(40.dp).clip(RoundedCornerShape(50))
                    .background(ColorBox.text.copy(0.1f))
                    .clickable {

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp).size(22.dp),
                    painter = painterResource("icons/check.svg"),
                    contentDescription = null,
                    tint = ColorBox.text
                )
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "Save",
                    fontSize = 12.sp,
                    color = ColorBox.text
                )
            }
        }
        CustomTextField(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            value = name,
            singleLine = true,
            label = {
                    Text("Playlist name")
            },
            onValueChange = {
                name = it
            }
        )
    }

}