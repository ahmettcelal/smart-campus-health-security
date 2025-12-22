package ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ahmettcelal.smart_campus_health_security.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import model.UserRole
import ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    
    // Test kullanıcı bilgileri
    private val testUserEmail = "user@mail.com"
    private val testUserPassword = "user123"
    private val testAdminEmail = "admin@mail.com"
    private val testAdminPassword = "admin123"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)

        // Giriş Yap butonu
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Test için basit kontrol (gerçek uygulamada API çağrısı yapılacak)
            if ((email == testUserEmail && password == testUserPassword) ||
                (email == testAdminEmail && password == testAdminPassword)) {
                // Başarılı giriş mesajı
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                performLogin(email, password)
            } else {
                // Hatalı giriş mesajı
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }

        // Kayıt Ol butonu
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun performLogin(email: String, password: String) {
        // Kullanıcı rolünü belirle
        val role = if (email == testAdminEmail) UserRole.ADMIN else UserRole.USER
        
        // TODO: SharedPreferences veya DataStore'a kullanıcı bilgilerini kaydet
        // Şimdilik sadece MainActivity'ye geç
        
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("user_email", email)
            putExtra("user_role", role.name)
        }
        startActivity(intent)
        finish() // LoginActivity'yi kapat
    }
}