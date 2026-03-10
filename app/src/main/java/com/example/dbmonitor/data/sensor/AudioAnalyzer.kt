package com.example.dbmonitor.data.sensor

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlin.math.log10
import kotlin.math.sqrt

class AudioAnalyzer {
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    fun getDecibelStream(): Flow<Double> = flow {
        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate, channelConfig, audioFormat, bufferSize
        )

        val buffer = ShortArray(bufferSize)

        try {
            audioRecord.startRecording()
            while (currentCoroutineContext().isActive) {
                val readResult = audioRecord.read(buffer, 0, bufferSize)
                if (readResult > 0) {
                    var sum = 0.0
                    for (i in 0 until readResult) {
                        sum += buffer[i] * buffer[i]
                    }
                    val rms = sqrt(sum / readResult)
                    val db = 20 * log10(rms / 32767.0) + 90
                    
                    emit(db.coerceAtLeast(0.0))
                }
                delay(100) // Delay to avoid overwhelming the flow with too many emissions
            }
        } finally {
            audioRecord.stop()
            audioRecord.release()
        }
    }.flowOn(Dispatchers.IO) // Coroutines IO for not blocking the main thread
}
