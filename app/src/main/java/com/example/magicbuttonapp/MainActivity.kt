package com.example.magicbuttonapp //identify the code of app.
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LightSwitchApp()
        }
    }
}

@Composable
fun LightSwitchApp() {

    // False means OFF. True means ON.
    var isOn by remember {
        mutableStateOf(false)
    }

    // Gets the current Android screen context.
    val context = LocalContext.current

    // Loads the downloaded switch sound.
    val sound = remember(context) {
        MediaPlayer.create(context, R.raw.switch_click)
    }

    // Releases the sound when the screen is removed.
    DisposableEffect(sound) {
        onDispose {
            sound?.release()
        }
    }

    // Changes the background color smoothly.
    val backgroundColor by animateColorAsState(
        targetValue = if (isOn) Color(0xFFFFF8E7)
        else Color(0xFF121827),
        label = "Background"
    )

    // Animates the physical switch movement.
    val switchRotation by animateFloatAsState(
        targetValue = if (isOn) -18f else 18f,
        label = "Switch Movement"
    )

    // Changes the text color.
    val textColor = if (isOn) Color.DarkGray else Color.White

    // Main screen.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Shows ON or OFF.
            Text(
                text = if (isOn) "LIGHT ON" else "LIGHT OFF",
                color = textColor,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Switch outer frame.
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 170.dp)
                    .background(
                        Color(0xFF30343A),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {

                // Actual physical rocker switch.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationX = switchRotation
                            cameraDistance = 12 * density
                        }
                        .shadow(
                            6.dp,
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            if (isOn) Color.White
                            else Color(0xFFC5C8CB),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {

                            // Plays the downloaded clicking sound.
                            sound?.seekTo(0)
                            sound?.start()

                            // Changes ON to OFF or OFF to ON.
                            isOn = !isOn
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = if (isOn) "ON" else "OFF",
                        color = Color.DarkGray,
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Instruction.
            Text(
                text = "Tap the switch",
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}
