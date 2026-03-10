package com.example.dbmonitor.ui.composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dbmonitor.ui.viewmodel.MonitorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonitorScreen(viewModel: MonitorViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val statusColor by animateColorAsState(
        targetValue = when {
            uiState.currentDb > 85 -> Color(0xFFE53935)
            uiState.currentDb > 60 -> Color(0xFFFFB300)
            else -> Color(0xFF43A047)
        },
        label = "ColorAnimation"
    )

    val permissionState = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleMonitoring()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 120.dp),
            text = if (uiState.isMonitoring) "MONITORANDO" else "PAUSADO",
            style = MaterialTheme.typography.labelLarge,
            color = statusColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text(
                text = "%.1f".format(uiState.currentDb),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 85.sp,
                    fontWeight = FontWeight.Black
                ),
                color = statusColor
            )
            Text(
                text = "decibéis (dB)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
        LiveLineChart(
            history = uiState.history,
            lineColor = statusColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 8.dp)
        )

        Button(
            onClick = {
                permissionState.launch(android.Manifest.permission.RECORD_AUDIO)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isMonitoring) 
                    Color.Red.copy(alpha = 0.05f) 
                else 
                    MaterialTheme.colorScheme.primaryContainer,
                contentColor = if (uiState.isMonitoring) 
                    Color.Red 
                else 
                    MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = if (uiState.isMonitoring) BorderStroke(2.dp, Color.Red) else null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (uiState.isMonitoring) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (uiState.isMonitoring) "PARAR MONITORAMENTO" else "INICIAR MONITORAMENTO",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
