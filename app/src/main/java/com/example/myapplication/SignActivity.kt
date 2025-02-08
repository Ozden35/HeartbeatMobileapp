package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySignBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding
    private lateinit var userName: String
    private lateinit var userMail: String
    private lateinit var userPassword: String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.ZatenHesapvarbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnHesap.setOnClickListener {
            userName = binding.editTextText.text.toString().trim()
            userMail = binding.editTextTextEmailAddress2.text.toString().trim()
            userPassword = binding.editTextTextPassword2.text.toString().trim()

            if (TextUtils.isEmpty(userName)) {
                binding.editTextText.error = "Kullanıcı adı boş olamaz"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(userMail)) {
                binding.editTextTextEmailAddress2.error = "E-posta boş olamaz"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(userPassword)) {
                binding.editTextTextPassword2.error = "Şifre boş olamaz"
                return@setOnClickListener
            } else {
                createAccount(userMail, userPassword)
            }
        }
    }

    private fun createAccount(userMail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userMail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this@SignActivity, "Kayıt başarılı", Toast.LENGTH_LONG).show()
                    saveUserData()
                    startActivity(Intent(this@SignActivity, MainActivity::class.java))
                }?.addOnFailureListener {
                    Toast.makeText(this, "Hata: ${it.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@SignActivity, "Hata: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserData() {
        userName = binding.editTextText.text.toString().trim()
        userMail = binding.editTextTextEmailAddress2.text.toString().trim()
        userPassword = binding.editTextTextPassword2.text.toString().trim()

        val userId = UUID.randomUUID().toString()
        val user = Users(userId, userName, userMail, userPassword)

        database.child("user").child(userId).setValue(user)
    }
}