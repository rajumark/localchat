package com.localchat.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

sealed interface ModelState {
    data object Checking : ModelState
    data object Unavailable : ModelState
    data object Downloadable : ModelState
    data object Downloading : ModelState
    data object Available : ModelState
    data class Error(val message: String) : ModelState
}

class ChatViewModel : ViewModel() {

    private val generativeModel: GenerativeModel = Generation.getClient()

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    private val _modelState = MutableStateFlow<ModelState>(ModelState.Checking)
    val modelState: StateFlow<ModelState> = _modelState

    private var modelAvailable = false

    init {
        checkModelAvailability()
    }

    private fun checkModelAvailability() {
        viewModelScope.launch {
            _modelState.value = ModelState.Checking
            try {
                val status = generativeModel.checkStatus()
                modelAvailable = status == FeatureStatus.AVAILABLE
                _modelState.value = when (status) {
                    FeatureStatus.AVAILABLE -> {
                        _messages.add(
                            ChatMessage(
                                "Hello! I'm LocalChat, powered by on-device Gemini Nano. " +
                                "Ask me anything — no internet needed.",
                                isUser = false
                            )
                        )
                        ModelState.Available
                    }
                    FeatureStatus.DOWNLOADABLE -> {
                        _messages.add(
                            ChatMessage(
                                "Gemini Nano needs to be downloaded first. " +
                                "Tap send to start the download.",
                                isUser = false
                            )
                        )
                        ModelState.Downloadable
                    }
                    FeatureStatus.DOWNLOADING -> ModelState.Downloading
                    FeatureStatus.UNAVAILABLE -> ModelState.Unavailable
                    else -> ModelState.Error("Unknown model status")
                }
            } catch (e: Exception) {
                _modelState.value = ModelState.Error(
                    "Failed to check model: ${e.message}"
                )
            }
        }
    }

    private fun downloadModel() {
        viewModelScope.launch {
            _modelState.value = ModelState.Downloading
            try {
                generativeModel.download().collect { status ->
                    when (status) {
                        is DownloadStatus.DownloadStarted -> {
                            _messages.add(
                                ChatMessage("Downloading Gemini Nano...", isUser = false)
                            )
                        }
                        is DownloadStatus.DownloadProgress -> {
                            val mb = status.totalBytesDownloaded / (1024 * 1024)
                            _messages.lastOrNull()?.let {
                                if (it.isUser == false) {
                                    _messages[_messages.lastIndex] = ChatMessage(
                                        "Downloading... $mb MB downloaded",
                                        isUser = false
                                    )
                                }
                            }
                        }
                        is DownloadStatus.DownloadCompleted -> {
                            modelAvailable = true
                            _modelState.value = ModelState.Available
                            _messages.add(
                                ChatMessage(
                                    "Download complete! Ask me anything.",
                                    isUser = false
                                )
                            )
                        }
                        is DownloadStatus.DownloadFailed -> {
                            _modelState.value = ModelState.Downloadable
                            _messages.add(
                                ChatMessage(
                                    "Download failed: ${status.e.message ?: "Unknown error"}",
                                    isUser = false
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _modelState.value = ModelState.Downloadable
                _messages.add(
                    ChatMessage("Download error: ${e.message}", isUser = false)
                )
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() || _isGenerating.value) return

        val currentState = _modelState.value
        if (currentState is ModelState.Downloadable) {
            downloadModel()
            _messages.add(ChatMessage(userText, isUser = true))
            return
        }

        if (currentState is ModelState.Downloading) {
            _messages.add(
                ChatMessage(
                    "Model is still downloading. Please wait...",
                    isUser = false
                )
            )
            return
        }

        if (!modelAvailable) {
            _messages.add(
                ChatMessage(
                    "Gemini Nano is not available on this device. " +
                    "AICore requires a supported device (Pixel 8/9, Galaxy S24/S25, etc.) " +
                    "with Android 14+.",
                    isUser = false
                )
            )
            return
        }

        _messages.add(ChatMessage(userText, isUser = true))
        _isGenerating.value = true

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(userText)
                val text = response.candidates.firstOrNull()?.text ?: "(empty response)"
                _messages.add(ChatMessage(text, isUser = false))
            } catch (e: Exception) {
                _messages.add(
                    ChatMessage(
                        "Error: ${e.message}",
                        isUser = false
                    )
                )
            } finally {
                _isGenerating.value = false
            }
        }
    }
}
