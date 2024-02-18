package uz.gita.customview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class Main3Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appRepos = AppRepository.getInstance()
        setContentView(R.layout.activity_main2)
        val qrCode = findViewById<QRCode>(R.id.qr_code)
        val x = findViewById<TextInputEditText>(R.id.x)
        val y = findViewById<TextInputEditText>(R.id.y)
        findViewById<Button>(R.id.make).setOnClickListener{
            qrCode.setMatrix(
                appRepos.newMatrix(
                    x.text.toString().toInt(),
                    y.text.toString().toInt()
                )
            )
        }
    }
}