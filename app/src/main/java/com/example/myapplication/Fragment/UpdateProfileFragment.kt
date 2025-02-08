package com.example.myapplication.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.FragmentUpdateProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UpdateProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentUpdateProfileBinding
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdate.setOnClickListener {
            val newUsername = binding.editTextUsername.text.toString().trim()
            val currentPassword = binding.editTextCurrentPassword.text.toString().trim()
            val newPassword = binding.editTextNewPassword.text.toString().trim()
            val confirmNewPassword = binding.editTextConfirmNewPassword.text.toString().trim()

            // Gerekli doğrulamaları yapın
            if (newUsername.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                Toast.makeText(requireContext(), "Yeni şifreler uyuşmuyor.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateProfile(newUsername, currentPassword, newPassword)
        }
    }

    private fun updateProfile(newUsername: String, currentPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential)
                .addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        // Kullanıcı yeniden doğrulandıysa, Firebase Authentication ile kullanıcı şifresini güncelle.
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updatePasswordTask ->
                                if (updatePasswordTask.isSuccessful) {
                                    // Firebase Authentication'daki şifre güncelleme işlemi başarılı oldu.
                                    // Şimdi Realtime Database'deki kullanıcı bilgilerini güncelleyebiliriz.
                                    val userId = user.uid
                                    val userReference = FirebaseDatabase.getInstance().getReference("user").child(userId)

                                    val updates = hashMapOf<String, Any>(
                                        "username" to newUsername,
                                        "password" to newPassword

                                    )

                                    userReference.updateChildren(updates)
                                        .addOnSuccessListener {
                                            // Realtime Database'deki veri güncelleme işlemi başarılı oldu.
                                            Toast.makeText(requireContext(), "Profil başarıyla güncellendi.", Toast.LENGTH_SHORT).show()
                                            // Ana ekrana geri dön
                                            requireActivity().supportFragmentManager.popBackStack()
                                        }
                                        .addOnFailureListener { e ->
                                            // Realtime Database'deki veri güncelleme işlemi başarısız oldu.
                                            Toast.makeText(requireContext(), "Veri güncelleme başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    // Firebase Authentication'daki şifre güncelleme işlemi başarısız oldu.
                                    Toast.makeText(requireContext(), "Şifre güncelleme başarısız: ${updatePasswordTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // Kullanıcı yeniden doğrulanamadı.
                        Toast.makeText(requireContext(), "Kullanıcı yeniden doğrulanamadı: ${reAuthTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Kullanıcı mevcut değil.
            Toast.makeText(requireContext(), "Kullanıcı mevcut değil.", Toast.LENGTH_SHORT).show()
        }
    }
}