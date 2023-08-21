package components

import CenterState
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import theme.ColorBox

@Composable
fun CustomScaffold(
    drawerState: CustomOcState,
    searchState: CustomOcState,
    scope: CoroutineScope,
    drawerBackgroundColor: Color,
    appbarBackgroundColor: Color,

    onSearchContent : (String) -> Unit,
    appbarContent: @Composable RowScope.() -> Unit,
    mainContent: @Composable @UiComposable BoxWithConstraintsScope.() -> Unit,
    drawerContent: @Composable BoxScope.() -> Unit
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(ColorBox.primaryDark)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth().background(appbarBackgroundColor)) {
                CustomToolbar(drawerState, searchState,onSearchContent, scope, appbarContent)
            }
            Box {
                BoxWithConstraints(Modifier.fillMaxSize(), content = mainContent)
                DrawerLayout(scope, drawerState, drawerBackgroundColor, drawerContent)
            }
        }
    }

}

@Composable
private fun CustomToolbar(
    drawerState: CustomOcState,
    searchState: CustomOcState,
    onSearchContent: (String) -> Unit,
    scope: CoroutineScope,
    appbarContent: @Composable RowScope.() -> Unit
) {

    Row(Modifier.height(56.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.padding(start = 4.dp))
        val menuIconState = if (searchState.isOpen) true else if (drawerState.isOpen) true else false
        HamburgerMenu(modifier = Modifier.size(48.dp), size = 20.dp, isOpen = menuIconState) {
            if (searchState.isOpen) {
                scope.launch {
                    searchState.close()
                }
            } else {
                if (drawerState.isOpen) {
                    scope.launch {
                        drawerState.close()
                    }
                } else {
                    scope.launch {
                        drawerState.open()
                    }
                }
            }
        }
        this@Row.AnimatedVisibility(
            visible = searchState.isOpen,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Box(Modifier.padding(start = 12.dp, end = 8.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                searchTextField(searchState) {
                    onSearchContent.invoke(it)
                }
            }
        }
        this@Row.AnimatedVisibility(
            visible = !searchState.isOpen,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, content = appbarContent)
        }
    }
}


@Composable
private fun searchTextField(
    searchState: CustomOcState,
    onValueChange: (String) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }

    var value by remember { mutableStateOf(TextFieldValue()) }
    if (!searchState.isOpen) {
        value = TextFieldValue("")
        onValueChange.invoke("")
    }

    if (value.text.trim().isEmpty()) {
        Text(modifier = Modifier.fillMaxWidth(), text = "Search audios", fontSize = 15.sp, color = ColorBox.text.copy(0.3f))
    }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            modifier = Modifier.weight(1f).focusRequester(focusRequester).onFocusChanged { CenterState.hasAnyTextFieldFocus = it.hasFocus },
            value = value,
            textStyle = TextStyle(color = ColorBox.text.copy(0.8f), fontSize = 15.sp, fontWeight = FontWeight.Medium),
            cursorBrush = SolidColor(ColorBox.text.copy(0.9f)),
            singleLine = true,
            onValueChange = {
                value = it
                onValueChange.invoke(it.text)
            }
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun DrawerLayout(
    scope: CoroutineScope,
    drawerState: CustomOcState,
    drawerBackgroundColor: Color,
    drawerContent: @Composable BoxScope.() -> Unit
) {
    val slideAnim = animateFloatAsState(if (drawerState.isOpen) 1f else 0f)

    Box(modifier = Modifier.fillMaxSize()) {
        DimView(Color.Black.copy(0.8f), slideAnim.value) {
            scope.launch {
                drawerState.close()
            }
        }
        AnimatedVisibility(
            visible = drawerState.isOpen,
            enter = slideInHorizontally(initialOffsetX = { -250 }),
            exit = slideOutHorizontally(targetOffsetX = { -250 })
        ) {
            Box(
                Modifier.width(250.dp).fillMaxHeight().clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(drawerBackgroundColor), content = drawerContent
            )
        }
    }
}

class CustomOcState {
    private var state = mutableStateOf(false)
    val isOpen: Boolean
        get() = state.value

    fun open() {
        state.value = true
    }

    fun close() {
        state.value = false
    }
}

@Composable
fun rememberCustomOCState(): CustomOcState {
    return rememberSaveable {
        CustomOcState()
    }
}


@Composable
internal fun DimView(color: Color, fraction: Float, onDimClicked: () -> Unit) {
    if (fraction == 1f) {
        Canvas(
            modifier = Modifier.fillMaxSize()
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    onDimClicked.invoke()
                }) {
            drawRect(color, alpha = fraction)
        }
    } else {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color, alpha = fraction)
        }
    }
}