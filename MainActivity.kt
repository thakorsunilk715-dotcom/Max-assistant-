package com.max.assistant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var speech: SpeechRecognizer? = null
    private lateinit var status: TextView
    private var torchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 10)
        }
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 48, 28, 24)
            setBackgroundColor(0xFFF0F2F8.toInt())
        }
        val title = TextView(this).apply {
            text = "MAX"
            textSize = 42f
            setTextColor(0xFF111827.toInt())
        }
        val subtitle = TextView(this).apply {
            text = "शुभ संध्या, सुनील"
            textSize = 20f
            setTextColor(0xFF4B5563.toInt())
        }
        val power = Button(this).apply {
            text = "⏻"
            textSize = 56f
            contentDescription = "Start MAX voice assistant"
            setOnClickListener { listen() }
        }
        status = TextView(this).apply {
            text = "मैक्स वॉइस असिस्टेंट"
            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(8, 24, 8, 24)
        }
        val listenButton = Button(this).apply {
            text = "शुरू करने के लिए टैप करें"
            setOnClickListener { listen() }
        }
        val settingsButton = Button(this).apply {
            text = "Accessibility / Quick Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
        val bottom = TextView(this).apply {
            text = "⌂ Home       ◷ History       ⚙ Settings       ⓘ Features"
            textSize = 17f
            gravity = Gravity.CENTER
            setPadding(0, 36, 0, 0)
        }
        listOf(title, subtitle, power, status, listenButton, settingsButton, bottom).forEach {
            root.addView(it, LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 16
            })
        }
        setContentView(root)
    }

    override fun onInit(result: Int) {
        if (result == TextToSpeech.SUCCESS) {
            val langResult = tts.setLanguage(Locale("hi", "IN"))
            tts.setSpeechRate(0.93f)
            if (langResult == TextToSpeech.LANG_MISSING_DATA ||
                langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                speak("Hindi voice installed नहीं है। कृपया फोन की Text to speech settings में Hindi voice डाउनलोड करें।")
            }
        }
    }

    private fun speak(message: String) {
        if (::tts.isInitialized) tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "MAX_REPLY")
        status.text = message
    }

    private fun listen() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            speak("इस फोन में speech recognition उपलब्ध नहीं है।")
            return
        }
        speech?.destroy()
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech?.setRecognitionListener(object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { status.text = "सुन रही हूँ, Boss…" }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { status.text = "समझ रही हूँ…" }
            override fun onError(error: Int) { status.text = "आवाज़ नहीं मिली। फिर से टैप करें।" }
            override fun onResults(results: Bundle?) {
                val words = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val command = words?.firstOrNull().orEmpty()
                status.text = command
                handleCommand(command.lowercase(Locale.ROOT))
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "बोलिए, Boss")
        }
        speech?.startListening(intent)
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("youtube") && (command.contains("search") || command.contains("गाना") || command.contains("वीडियो")) -> {
                val query = command.replace("youtube", "").replace("पर", "").replace("search", "")
                    .replace("गाना", "").replace("वीडियो", "").trim()
                val i = Intent(Intent.ACTION_SEARCH).apply {
                    setPackage("com.google.android.youtube")
                    putExtra("query", query.ifBlank { "trending music" })
                }
                try { startActivity(i) } catch (_: Exception) {
                    openUrl("https://www.youtube.com/results?search_query=${android.net.Uri.encode(query)}")
                }
                speak("YouTube खोल रही हूँ, Boss।")
            }
            command.contains("youtube") -> {
                launchPackage("com.google.android.youtube", "YouTube नहीं मिला।")
            }
            command.contains("play store") || command.contains("प्ले स्टोर") -> {
                launchPackage("com.android.vending", "Play Store नहीं मिला।")
            }
            command.contains("whatsapp") || command.contains("व्हाट्सएप") -> {
                launchPackage("com.whatsapp", "WhatsApp नहीं मिला।")
            }
            command.contains("flashlight on") || command.contains("torch on") ||
                command.contains("टॉर्च चालू") || command.contains("फ्लैशलाइट चालू") -> setTorch(true)
            command.contains("flashlight off") || command.contains("torch off") ||
                command.contains("टॉर्च बंद") || command.contains("फ्लैशलाइट बंद") -> setTorch(false)
            command.contains("scroll down") || command.contains("नीचे स्क्रॉल") -> {
                MaxAccessibilityService.instance?.scrollForward()
                speak("स्क्रीन नीचे स्क्रॉल कर रही हूँ, Boss।")
            }
            command.contains("wifi") || command.contains("वाईफाई") ||
                command.contains("bluetooth") || command.contains("एयरप्लेन") ||
                command.contains("mobile data") -> {
                speak("Boss, Android की Quick Settings खोल रही हूँ। वहाँ से इस स्विच को टैप करें।")
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            }
            command.contains("settings") || command.contains("सेटिंग") -> {
                startActivity(Intent(Settings.ACTION_SETTINGS)); speak("Settings खोल रही हूँ, Boss।")
            }
            else -> speak("Boss, यह कमांड अभी उपलब्ध नहीं है। YouTube, Play Store, WhatsApp, टॉर्च या स्क्रॉल बोलकर देखें।")
        }
    }

    private fun launchPackage(pkg: String, error: String) {
        val launch = packageManager.getLaunchIntentForPackage(pkg)
        if (launch != null) {
            startActivity(launch)
            speak("खोल रही हूँ, Boss।")
        } else speak(error)
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)))
    }

    private fun setTorch(enabled: Boolean) {
        try {
            val manager = getSystemService(CAMERA_SERVICE) as CameraManager
            val cameraId = manager.cameraIdList.firstOrNull { manager.getCameraCharacteristics(it)
                .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true }
            if (cameraId == null) { speak("इस फोन में flashlight उपलब्ध नहीं है।"); return }
            manager.setTorchMode(cameraId, enabled)
            torchOn = enabled
            speak(if (enabled) "टॉर्च चालू कर दी, Boss।" else "टॉर्च बंद कर दी, Boss।")
        } catch (_: Exception) {
            speak("टॉर्च नहीं बदल पाई। Camera permission और फोन की उपलब्धता जाँचें।")
        }
    }

    override fun onDestroy() {
        speech?.destroy()
        if (::tts.isInitialized) tts.shutdown()
        super.onDestroy()
    }
}
