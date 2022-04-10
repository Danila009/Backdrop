package com.example.backdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.backdrop.ui.theme.Teal200
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val cinemas = listOf(
        Cinema(
            title = "cinema #1",
            imgUri = "https://avatars.mds.yandex.net/i?id=d3869972f65340723bfd9d8aa4f17e60-5896834-images-thumbs&n=13",
            mapOne = 100.22,
            mapTwo = 77.1
        ),
        Cinema(
            title = "cinema #2",
            imgUri = "https://avatars.mds.yandex.net/i?id=0b137ac4258d39578c99871de58229db-5865724-images-thumbs&n=13",
            mapOne = 89.1,
            mapTwo = 12.1
        ),
        Cinema(
            title = "cinema #3",
            imgUri = "https://avatars.mds.yandex.net/i?id=2a0000017a0ddda85287468b475b2d90059d-4314299-images-thumbs&n=13",
            mapOne = 123.1,
            mapTwo = 90.1
        ),
        Cinema(
            title = "cinema #4",
            imgUri = "https://avatars.mds.yandex.net/i?id=67dfd59e74131f13f640d3e5c95fceaf-4666607-images-thumbs&n=13",
            mapOne = 32.12,
            mapTwo = 31.312
        ),
        Cinema(
            title = "cinema #5",
            imgUri = "https://avatars.mds.yandex.net/i?id=764f14c604a3eafca77f350349e61084-4034095-images-thumbs&n=13",
            mapOne = 79.72,
            mapTwo = 26.23
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            rememberSystemUiController().isStatusBarVisible = false

            val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
            val offset by backdropState.offset
            val halfHeightDp = LocalConfiguration.current.screenHeightDp / 3
            val halfHeightPx = with(LocalDensity.current) {
                halfHeightDp.dp.toPx()
            }

            val cameraPositionLatLng = remember { mutableStateOf(LatLng(cinemas[3].mapOne, cinemas[3].mapTwo)) }

            val cameraPositionState = CameraPositionState(
                CameraPosition(
                    cameraPositionLatLng.value,
                    80f,
                    1f,
                    1f
                )
            )

            LaunchedEffect(backdropState) {
                backdropState.reveal()
            }

            BackdropScaffold(
                scaffoldState = backdropState,
                headerHeight = halfHeightDp.dp,
                backLayerBackgroundColor = Teal200,
                frontLayerScrimColor = Color.Unspecified,
                peekHeight = 0.dp,
                appBar = {},
                backLayerContent = {
                    GoogleMap(
                        cameraPositionState = cameraPositionState,
                        content = {
                            repeat(cinemas.size){
                                Marker(
                                    position = LatLng(cinemas[it].mapOne,
                                        cinemas[it].mapTwo),
                                    title = cinemas[it].title
                                )
                            }
                        }
                    )
                }, frontLayerContent = {
                    val verticalListAlpha = ((halfHeightPx - offset) / halfHeightPx).coerceIn(0f..1f)
                    val horizontalAlpha = (offset / halfHeightPx).coerceIn(0f..1f)

                    LazyColumn(
                        modifier = Modifier
                            .alpha(verticalListAlpha),
                        content = {
                        items(cinemas){ item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = item.imgUri),
                                    modifier = Modifier.size(250.dp),
                                    contentDescription = null
                                )
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.W100,
                                    fontFamily = FontFamily.Cursive,
                                    color = Color.Red
                                )
                            }
                        }
                    })

                    LazyRow(
                        modifier = Modifier
                            .alpha(horizontalAlpha),
                        content = {
                        items(cinemas){ item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = item.imgUri),
                                    modifier = Modifier
                                        .size(250.dp)
                                        .clickable {
                                            cameraPositionLatLng.value = LatLng(
                                                item.mapOne, item.mapTwo
                                            )
                                        },
                                    contentDescription = null
                                )
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.W100,
                                    fontFamily = FontFamily.Cursive,
                                    color = Color.Red
                                )
                            }
                        }
                    })
                }
            )
        }
    }
}