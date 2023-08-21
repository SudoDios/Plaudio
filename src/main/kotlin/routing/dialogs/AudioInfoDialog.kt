package routing.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CircleDot
import components.MyIconButton
import components.SmoothImage
import core.db.models.ModelAudio
import org.jaudiotagger.audio.AudioFileIO
import theme.ColorBox
import utils.Tools.formatToDuration
import utils.Tools.formatToSizeFile
import java.awt.Desktop
import java.io.File

@Composable
fun AudioInfoDialog(
    modelAudio: ModelAudio,
    closeClicked: () -> Unit,
) {

    val audioFile = AudioFileIO.read(File(modelAudio.path))

    Column(
        modifier = Modifier.width(370.dp).background(color = ColorBox.primaryDark2),
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
                    closeClicked.invoke()
                }
            )
            Text(
                modifier = Modifier.padding(start = 12.dp).weight(1f),
                text = "Properties",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.padding(8.dp))
        SmoothImage(
            modifier = Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)),
            image = modelAudio.coverArt,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 12.dp, end = 12.dp),
            text = modelAudio.name,
            fontSize = 16.sp,
            color = ColorBox.text
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
            text = modelAudio.artist,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = ColorBox.text.copy(0.8f)
        )
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = modelAudio.duration.formatToDuration(),
                fontSize = 12.sp,
                color = ColorBox.text.copy(0.7f)
            )
            CircleDot(Modifier.padding(start = 6.dp, end = 6.dp).size(4.dp), color = ColorBox.text.copy(0.5f))
            Text(
                text = modelAudio.size.formatToSizeFile(),
                fontSize = 12.sp,
                color = ColorBox.text.copy(0.7f)
            )
            CircleDot(Modifier.padding(start = 6.dp, end = 6.dp).size(4.dp), color = ColorBox.text.copy(0.5f))
            Text(
                text = modelAudio.format.uppercase(),
                fontSize = 12.sp,
                color = ColorBox.text.copy(0.7f)
            )
            CircleDot(Modifier.padding(start = 6.dp, end = 6.dp).size(4.dp), color = ColorBox.text.copy(0.5f))
            Text(
                text = "${audioFile.audioHeader.bitRate} kbps",
                fontSize = 12.sp,
                color = ColorBox.text.copy(0.7f)
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ColorBox.text.copy(0.1f))
                .clickable {
                    Desktop.getDesktop().open(File(modelAudio.folder))
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp).size(22.dp),
                painter = painterResource("icons/folder.svg"),
                contentDescription = null,
                tint = ColorBox.text
            )
            Text(
                modifier = Modifier.padding(12.dp),
                text = modelAudio.path,
                fontSize = 11.sp,
                color = ColorBox.text
            )
        }
    }

}