package routing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.onExternalDrag
import androidx.compose.ui.unit.dp
import components.CustomScaffold
import components.rememberCustomOCState
import kotlinx.coroutines.launch
import routing.dialogs.BaseDialog
import routing.dialogs.DropFilesDialog
import routing.dialogs.PlayerBottomSheet
import routing.sections.CompactPlayer
import routing.sections.Content
import routing.sections.Drawer
import theme.ColorBox
import utils.Global

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenHome() {

    val scope = rememberCoroutineScope()

    val drawerState = rememberCustomOCState()
    val sheetState = rememberCustomOCState()

    var dragState by remember { mutableStateOf(Global.DragState.DRAG_EXIT) }
    var dialogDragState by remember { mutableStateOf(Global.DragState.DRAG_EXIT) }

    val modifier = if (dragState == Global.DragState.DRAG_ENTER) {
        Modifier.fillMaxSize().background(ColorBox.primaryDark).blur(16.dp)
    } else {
        Modifier.fillMaxSize().background(ColorBox.primaryDark)
    }

    CustomScaffold(
        modifier = modifier
            .onExternalDrag(
                enabled = dialogDragState != Global.DragState.DROPPED,
                onDragStart = {
                    dragState = Global.DragState.DRAG_ENTER
                },
                onDragExit = {
                    dragState = Global.DragState.DRAG_EXIT
                },
                onDrop = {
                    if (dialogDragState == Global.DragState.DRAG_EXIT) {
                        dragState = Global.DragState.DRAG_EXIT
                    }
                }
            ),
        drawerState = drawerState,
        sheetState = sheetState,
        scope = scope,
        drawerBackgroundColor = ColorBox.primaryDark2,
        mainContent = {
            Content(
                drawerState,
                scope
            )
            CompactPlayer(
                onClicked = {
                    scope.launch {
                        sheetState.open()
                    }
                },
                onForceHide = {
                    if (sheetState.isOpen) {
                        scope.launch {
                            sheetState.close()
                        }
                    }
                }
            )
        },
        drawerContent = {
            Drawer {
                scope.launch {
                    drawerState.close()
                }
            }
        },
        sheetContent = {
            PlayerBottomSheet {
                scope.launch {
                    sheetState.close()
                }
            }
        }
    )

    BaseDialog(
        expanded = dragState == Global.DragState.DRAG_ENTER,
        backgroundColor = ColorBox.primaryDark
    ) {
        DropFilesDialog(
            onDragState = {
                dialogDragState = it
            },
            onClose = {
                dragState = Global.DragState.DRAG_EXIT
                dialogDragState = Global.DragState.DRAG_EXIT
            }
        )
    }

}


