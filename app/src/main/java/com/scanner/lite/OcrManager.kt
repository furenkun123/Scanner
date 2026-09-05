package com.scanner.lite

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

class OcrManager {

    enum class LanguageType { CHINESE, JAPANESE, KOREAN }

    private val chineseRecognizer: TextRecognizer by lazy {
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
    }
    private val japaneseRecognizer: TextRecognizer by lazy {
        TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
    }
    private val koreanRecognizer: TextRecognizer by lazy {
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }

    /**
     * 识别 Bitmap 图片中的文字
     */
    fun processBitmap(
        bitmap: Bitmap,
        lang: LanguageType = LanguageType.CHINESE,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)
        getRecognizer(lang).process(image)
            .addOnSuccessListener { text -> onSuccess(text.text) }
            .addOnFailureListener(onFailure)
    }

    /**
     * 识别 CameraX 的 ImageProxy 帧数据
     */
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun processImageProxy(
        imageProxy: ImageProxy,
        lang: LanguageType = LanguageType.CHINESE,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            getRecognizer(lang).process(image)
                .addOnSuccessListener { text ->
                    onSuccess(text.text)
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun getRecognizer(lang: LanguageType): TextRecognizer {
        return when (lang) {
            LanguageType.CHINESE -> chineseRecognizer
            LanguageType.JAPANESE -> japaneseRecognizer
            LanguageType.KOREAN -> koreanRecognizer
        }
    }
}