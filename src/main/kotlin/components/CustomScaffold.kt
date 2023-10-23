package components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import theme.ColorBox

@Composable
fun CustomScaffold(
    modifier: Modifier,
    drawerState: CustomOcState,
    sheetState: CustomOcState,
    scope: CoroutineScope,
    drawerBackgroundColor: Color,
    mainContent: @Composable @UiComposable BoxWithConstraintsScope.() -> Unit,
    drawerContent: @Composable BoxScope.() -> Unit,
    sheetContent: @Composable BoxScope.() -> Unit
) {

    BoxWithConstraints(modifier = modifier) {
        BoxWithConstraints(Modifier.fillMaxSize(), content = mainContent)
        DrawerLayout(scope, drawerState, drawerBackgroundColor, drawerContent)
        SheetLayout(scope, sheetState, ColorBox.primaryDark2, sheetContent)
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
            enter = slideInHorizontally(initialOffsetX = { -300 }),
            exit = slideOutHorizontally(targetOffsetX = { -300 })
        ) {
            Box(
                Modifier.width(300.dp).fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(drawerBackgroundColor), content = drawerContent
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.SheetLayout(
    scope: CoroutineScope,
    sheetState: CustomOcState,
    backgroundColor: Color,
    content: @Composable BoxScope.() -> Unit
) {

    val slideAnim = animateFloatAsState(if (sheetState.isOpen) 1f else 0f)

    DimView(Color.Black.copy(0.8f), slideAnim.value) {
        scope.launch {
            sheetState.close()
        }
    }
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = sheetState.isOpen,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .background(backgroundColor), content = content
        )
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