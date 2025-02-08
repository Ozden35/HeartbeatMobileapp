package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnGiris.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                binding.editTextTextEmailAddress.error = "E-posta boş olamaz"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.editTextTextPassword.error = "Şifre boş olamaz"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.Hesapyokmubutton.setOnClickListener {
            // Kayıt ol ekranına yönlendir
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

        // Şifremi Unuttum metnine tıklanabilirlik özelliği ekle
        binding.textView10.setOnClickListener {
            // Şifreyi sıfırlama işlemi
            resetPassword()
        }
    }

    // Şifreyi sıfırlama işlemi için metot
    private fun resetPassword() {
        val email = binding.editTextTextEmailAddress.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            binding.editTextTextEmailAddress.error = "E-posta boş olamaz"
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "Şifre sıfırlama e-postası gönderildi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        baseContext, "Şifre sıfırlama e-postası gönderilemedi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılı, ana sayfaya yönlendir
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // Giriş başarısız, hata mesajı göster
                    Toast.makeText(
                        baseContext, "Giriş başarısız: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}