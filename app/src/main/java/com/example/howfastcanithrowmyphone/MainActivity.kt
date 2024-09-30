package com.example.howfastcanithrowmyphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.howfastcanithrowmyphone.ui.theme.HowFastCanIThrowMyPhoneTheme
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.core.content.getSystemService
import android.net.Uri
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.VideoView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HowFastCanIThrowMyPhoneTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    AppView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun AppView(
    modifier: Modifier
){
    var hasSeenIntroVideo by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        ThrowsTopAppBar(modifier = Modifier)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.Center){
                Text(stringResource(R.string.throwingInstructions))
            }

            Row(horizontalArrangement = Arrangement.Center){
                MaxVelocityView()

            }
            Row(horizontalArrangement = Arrangement.Center){
                AccelerometerView()
            }

        }


    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThrowsTopAppBar(modifier: Modifier){
    CenterAlignedTopAppBar(
        title = {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(
                    id = R.drawable.aperture_science_logo_png_transparent),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .requiredSize(45.dp)
                        .padding(8.dp))
                Text(stringResource(R.string.app_name).uppercase(), fontFamily = FontFamily.Monospace)
            }
        },
        modifier = Modifier,
        windowInsets = TopAppBarDefaults.windowInsets,
//        navigationIcon = {
//            IconButton(onClick = { /* Handle navigation icon click */ }) {
//                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
//            }
//        },
//        actions = {
//            IconButton(onClick = { /* Handle action icon click */ }) {
//                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
//            }

    )
}


@Composable
fun AccelerometerView() {
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var z by remember { mutableStateOf(0f) }

    val sensorManager = LocalContext.current.getSystemService<SensorManager>()
    val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    DisposableEffect(
        key1 = accelerometer,
    ) {
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    x = event.values[0]
                    y = event.values[1]
                    z = event.values[2]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager?.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        onDispose {
            sensorManager?.unregisterListener(sensorListener)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Text("X: $x")
            Text("Y: $y")
            Text("Z: $z")
        }
    }
}


//
//
//val Context.dataStore by preferencesDataStore(
//    name = "settings"
//)
//val HAS_SEEN_INTRO_VIDEO = booleanPreferencesKey("hasSeenIntroVideo")
//
//@Composable
//fun PlayVideoOnStartup() {
//    val context = LocalContext.current
//    Surface(modifier = Modifier.fillMaxSize()) {
//        AndroidView(
//            factory = {
//                VideoView(it).apply {
//
//                    setVideoURI(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.cavejonsonintrovideo))
//                    setOnPreparedListener { mp -> mp.start() }
//                }
//            }
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppView(
        modifier = Modifier
    )
}

//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            HowFastCanIThrowMyPhoneTheme {
//                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    HowFastCanIThrowMyPhoneTheme {
//        Greeting("Android")
//    }
//}