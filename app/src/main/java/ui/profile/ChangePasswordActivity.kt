package ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ahmettcelal.akillikampusaglikguvenlikuygulamasi.R

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val btnSave = findViewById<MaterialButton>(R.id.btnSavePassword)

        btnSave.setOnClickListener {
            Toast.makeText(this, "Şifre değiştirme ekranı hazır (demo)", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
