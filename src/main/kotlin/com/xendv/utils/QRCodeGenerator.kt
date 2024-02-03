package com.xendv.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class QRCodeGenerator {
    private val folderPath = "src/main/resources/demo"

    private fun generateSingleQRCode(content: String, filePath: String) {
        val width = 300
        val height = 300

        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L

        val bitMatrix: BitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

        saveQRCodeImage(bitMatrix, filePath)
    }

    fun generateMultipleQRCodes(contents: List<String>) {
        contents.forEach { content ->
            val filePath = "$folderPath/qr_code_${content}.png"
            generateSingleQRCode(content, filePath)
        }
    }

    private fun saveQRCodeImage(bitMatrix: BitMatrix, filePath: String) {
        val width = bitMatrix.width
        val height = bitMatrix.height

        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        image.setRGB(0, 0, width, height, pixels, 0, width)

        ImageIO.write(image, "png", File(filePath))
    }
}

fun main() {
    val generator = QRCodeGenerator()
    generator.generateMultipleQRCodes(listOf("5XSzzCjAeD", "tronpY117R", "38dDd7Q0gA"))
}