package com.scanner.lite

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslateManager {

    /**
     * 翻译文本
     * @param sourceLang 源语言代码，如 TranslateLanguage.ENGLISH, TranslateLanguage.JAPANESE
     * @param targetLang 目标语言代码，默认中文 TranslateLanguage.CHINESE
     */
    fun translate(
        text: String,
        sourceLang: String = TranslateLanguage.ENGLISH,
        targetLang: String = TranslateLanguage.CHINESE,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (text.isBlank()) {
            onSuccess("")
            return
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        val translator = Translation.getClient(options)

        // 设置模型下载条件（可以根据需要限制仅 Wi-Fi 下载）
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        // 首次使用时会自动下载离线翻译模型（约 30MB）
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        onSuccess(translatedText)
                        translator.close()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                        translator.close()
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e)
                translator.close()
            }
    }
}