package com.example.myapplication.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Güncelleme butonunu bul ve tıklama dinleyicisi ekle
        val btnUpdateProfile: Button = view.findViewById(R.id.btnUpdateProfile)
        btnUpdateProfile.setOnClickListener {
            // Kullanıcının giriş yapmış olup olmadığını kontrol et
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Kullanıcı giriş yapmışsa, güncelleme sayfasına yönlendir
                findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
            } else {
                // Kullanıcı giriş yapmamışsa, uygun bir mesaj gösterilebilir
                Toast.makeText(requireContext(), "Lütfen önce giriş yapın.", Toast.LENGTH_SHORT).show()
            }
        }

        // Hesabı Sil butonunu bul ve tıklama dinleyicisi ekle
        val btnDeleteAccount: Button = view.findViewById(R.id.btnDeleteAccount)
        btnDeleteAccount.setOnClickListener {
            showDeleteAccountDialog()
        }

        // Çıkış Yap butonunu bul ve tıklama dinleyicisi ekle
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun showDeleteAccountDialog() {
        // AlertDialog oluştur
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hesabı Sil")
        builder.setMessage("Hesabınızı silmek istediğinizden emin misiniz?")
        builder.setPositiveButton("Evet") { dialog, which ->
            // Evet'e tıklanınca yapılacak işlemler
            deleteAccount()
        }
        builder.setNegativeButton("Hayır") { dialog, which ->
            // Hayır'a tıklanınca yapılacak işlemler (dialog kapatılır)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteAccount() {
        // Kullanıcıyı Firebase Authentication'dan ve veritabanından sil
        currentUser?.let { user ->
            user.delete()
                .addOnSuccessListener {
                    // Kullanıcı başarıyla silindiğinde yapılacak işlemler
                    // Veritabanından da kullanıcıyı sil
                    val userId = user.uid
                    val userRef = FirebaseDatabase.getInstance().getReference("user").child(userId)
                    userRef.removeValue()
                        .addOnSuccessListener {
                            // Kullanıcı veritabanından başarıyla silindiğinde yapılacak işlemler
                            Toast.makeText(requireContext(), "Hesabınız başarıyla silindi.", Toast.LENGTH_SHORT).show()
                            // Çıkış yap
                            logoutUser()
                        }
                        .addOnFailureListener { e ->
                            // Kullanıcıyı veritabanından silme işlemi başarısız olduğunda yapılacak işlemler
                            Toast.makeText(requireContext(), "Hesap silme işlemi başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    // Kullanıcı silme işlemi başarısız olduğunda yapılacak işlemler
                    Toast.makeText(requireContext(), "Hesap silme işlemi başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun logoutUser() {
        // Kullanıcıyı Firebase'den çıkış yap
        auth.signOut()

        // LoginActivity'e yönlendir ve diğer tüm aktiviteleri temizle
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
} 