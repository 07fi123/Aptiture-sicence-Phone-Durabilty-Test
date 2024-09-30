package com.example.howfastcanithrowmyphone

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import kotlin.math.sqrt


@Composable
fun MaxVelocityView() {
    var maxVelocity by remember { mutableStateOf(0f) }
    var lastTimestamp by remember { mutableStateOf(0L) }
    var lastX by remember { mutableStateOf(0f) }
    var lastY by remember { mutableStateOf(0f) }
    var lastZ by remember { mutableStateOf(0f) }

    val sensorManager = LocalContext.current.getSystemService<SensorManager>()
    val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    DisposableEffect(
        key1 = accelerometer,
    ) {
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                    val currentTime = event.timestamp
                    if (lastTimestamp != 0L) {
                        val dt = (currentTime - lastTimestamp) * 1e-7f
                        val vx = (event.values[0] - lastX) / dt
                        val vy = (event.values[1] - lastY) / dt
                        val vz = (event.values[2] - lastZ) / dt
                        val velocity = sqrt(vx * vx + vy * vy + vz * vz)
                        if (velocity > maxVelocity) {
                            maxVelocity = velocity
                        }
                    }
                    lastTimestamp = currentTime
                    lastX = event.values[0]
                    lastY = event.values[1]
                    lastZ = event.values[2]
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


            StatusIndicator(maxVelocity)
           // Text("Test Max Velocity: $maxVelocity m/s")


}


@Composable
fun StatusIndicator(statusValue: Float) {
    val status = when {
        statusValue > 12.0f -> Status.DESTROYED
        statusValue > 9.8f -> Status.DAMAGED
        else -> Status.SAFE
    }

    Surface(shape = Shapes().extraLarge, modifier = Modifier.fillMaxHeight(0.5f).padding(10.dp).fillMaxWidth(0.9f), color = status.color) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(horizontalArrangement = Arrangement.Center){
                Text(text = "Phone Damage Status", color = Color.DarkGray, textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
                Text(text = "${status.name}", color = Color.DarkGray, textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge )

            }
        }
    }
}

enum class Status(val color: Color) {
    SAFE(Color.Green),
    DAMAGED(Color.Yellow),
    DESTROYED(Color.Red)
}

