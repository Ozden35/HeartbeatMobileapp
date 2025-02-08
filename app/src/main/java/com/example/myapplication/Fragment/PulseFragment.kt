package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.PulseAdapter
import com.example.myapplication.PulseData
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*

class PulseFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pulseAdapter: PulseAdapter
    private lateinit var pulseList: MutableList<PulseData>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pulse, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pulseList = mutableListOf()
        pulseAdapter = PulseAdapter(pulseList)
        recyclerView.adapter = pulseAdapter

        // Firebase veritabanından son nabız verisini almak
        database = FirebaseDatabase.getInstance().reference.child("pulse_data")
        database.limitToLast(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pulseList.clear() // Mevcut listeyi temizle
                for (data in snapshot.children) {
                    val pulseRate = data.child("pulse_rate").getValue(Int::class.java)
                    val result = data.child("result").getValue(Int::class.java)
                    val timestamp = data.child("timestamp").getValue(String::class.java)

                    if (pulseRate != null && result != null && timestamp != null) {
                        val pulseData = PulseData(null, null, null, pulseRate, timestamp, result)
                        pulseList.add(pulseData)

                        // Kullanıcıya uygun mesajı göster ve renk ayarla
                        val message: String
                        val backgroundColor: Int
                        when (result) {
                            0 -> {
                                message = "Dikkat! Nabız değeriniz çok düşük. Lütfen dinlenin ve durumunuzu kontrol edin."
                                backgroundColor = R.color.blue // Düşük nabız için mavi renk
                            }
                            1 -> {
                                message = "Harika! Nabız değeriniz normal. Devam edin ve sağlıklı bir gün geçirin."
                                backgroundColor = R.color.green // Normal nabız için yeşil renk
                            }
                            2 -> {
                                message = "Dikkat! Nabız değeriniz yüksek. Lütfen durumunuzu kontrol edin ve gereken önlemleri alın."
                                backgroundColor = R.color.red // Yüksek nabız için kırmızı renk
                            }
                            else -> {
                                message = "Bilinmeyen nabız durumu."
                                backgroundColor = R.color.snackbar_background // Varsayılan arka plan rengi
                            }
                        }
                        showNotification(message, backgroundColor)
                    }
                }
                pulseAdapter.notifyDataSetChanged() // Adapteri güncelle
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PulseFragment", "Veri alınamadı: ${error.message}")
            }
        })

        return view
    }

    private fun showNotification(message: String, backgroundColor: Int) {
        // Kullanıcıya bildirim göstermek için uygun olan kodlar
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundColor))
        snackbar.setTextColor(ContextCompat.getColor(requireContext(), R.color.snackbar_text))
        snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.snackbar_action_text))

        // Snackbar'ı yukarı taşımak için margin ekledim
        val view = snackbar.view
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + 200) // 200px yukarı taşımak için
        view.layoutParams = params

        snackbar.show()
    }
}