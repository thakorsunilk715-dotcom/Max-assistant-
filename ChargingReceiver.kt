package com.max.assistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.Locale

class ChargingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> "Charging connect ho gaya, Boss."
            Intent.ACTION_POWER_DISCONNECTED -> "Charging disconnect ho gaya, Boss."
            else -> return
        }
        // A receiver is short-lived; this simple implementation uses Android's TTS engine.
        val tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsSafeSpeak(context, message)
            }
        }
    }
    private fun ttsSafeSpeak(context: Context, message: String) {
        val engine = TextToSpeech(context.applicationContext, null)
        engine.language = Locale("hi", "IN")
        engine.speak(message, TextToSpeech.QUEUE_FLUSH, null, "MAX_CHARGING")
    }
}
