package routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import components.CustomScaffold
import components.rememberCustomOCState
import kotlinx.coroutines.launch
import routing.dialogs.PlayerBottomSheet
import routing.sections.CompactPlayer
import routing.sections.Content
import routing.sections.Drawer
import theme.ColorBox

@Composable
fun ScreenHome() {

    val scope = rememberCoroutineScope()

    val drawerState = rememberCustomOCState()
    val sheetState = rememberCustomOCState()

    CustomScaffold(
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

}


