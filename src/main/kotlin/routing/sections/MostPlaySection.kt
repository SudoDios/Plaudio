package routing.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.SmoothImage
import core.CorePlayer
import core.db.models.ModelAudio
import theme.ColorBox
import utils.Global
import utils.Tools.formatToDuration

@Composable
fun ColumnScope.MostPlaySection(
    playingAudio: ModelAudio,
    active: Boolean,
) {

    val scrollState = rememberLazyListState()

    Column(
        Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp).width(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ColorBox.primaryDark2).weight(1f)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "You Most Played",
            color = ColorBox.text.copy(0.8f),
            fontSize = 16.sp
        )
        if (Global.Data.mostPlayedAudios.isEmpty()) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.padding(bottom = 16.dp).size(60.dp),
                    painter = painterResource("icons/music-note.svg"),
                    contentDescription = null,
                    tint = ColorBox.primary.copy(0.7f)
                )
                Text(
                    text = "No Item Found",
                    color = ColorBox.text.copy(0.7f),
                    fontSize = 13.sp
                )
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                ) {
                    items(Global.Data.mostPlayedAudios) {
                        MostPlayItem(playingAudio,active,it)
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState),
                    style = LocalScrollbarStyle.current.copy(
                        thickness = 6.dp,
                        hoverColor = ColorBox.text.copy(0.6f),
                        unhoverColor = ColorBox.text.copy(0.1f)
                    )
                )
            }

        }
    }
}

@Composable
private fun MostPlayItem(
    playingAudio: ModelAudio,
    active: Boolean,
    modelAudio: ModelAudio
) {

    val play = ((playingAudio.hash == modelAudio.hash) && active)
    val bgColor = animateColorAsState(if (play) ColorBox.primary.copy(0.1f) else Color.Transparent)

    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.padding(start = 8.dp,end = 8.dp),
            text = modelAudio.playCount.toString(),
            color = ColorBox.text.copy(0.8f),
            fontSize = 12.sp
        )
        Row(
            Modifier.padding(5.dp).fillMaxWidth().clip(RoundedCornerShape(50)).background(bgColor.value).clickable {
                CorePlayer.startPlay(modelAudio)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmoothImage(
                modifier = Modifier.padding(4.dp).size(44.dp).clip(RoundedCornerShape(50)),
                image = modelAudio.coverArt,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(start = 6.dp).weight(1f),
                text = modelAudio.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = ColorBox.text.copy(0.8f),
                fontSize = 13.sp
            )
            Text(
                modifier = Modifier.padding(start = 8.dp,end = 12.dp),
                text = modelAudio.duration.formatToDuration(),
                color = ColorBox.text.copy(0.7f),
                fontSize = 12.sp
            )
        }
    }
}