package com.example.qrmemo

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageViewQR: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editTextContent)
        buttonSave = findViewById(R.id.buttonSave)
        imageViewQR = findViewById(R.id.imageViewQR)

        val savedContent = getSavedContent()
        if (savedContent.isNotEmpty()) {
            editText.setText(savedContent)
            generateQR(savedContent)
        }

        buttonSave.setOnClickListener {
            val content = editText.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveContent(content)
            generateQR(content)
        }
    }

    private fun getSavedContent(): String {
        val prefs = getSharedPreferences("qrmemo", MODE_PRIVATE)
        return prefs.getString("content", "") ?: ""
    }

    private fun saveContent(content: String) {
        val prefs = getSharedPreferences("qrmemo", MODE_PRIVATE)
        prefs.edit().putString("content", content).apply()
    }

    private fun generateQR(text: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                text, BarcodeFormat.QR_CODE, 600, 600
            )
            imageViewQR.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, "生成二维码失败", Toast.LENGTH_SHORT).show()
        }
    }
}
