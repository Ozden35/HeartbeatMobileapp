package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class HomeFragment : Fragment() {
    //Fragment oluşturulduğunda çağrılır. Bu metod, fragment_home layout dosyasını bu fragment için inflate eder ve görünüm olarak döner.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sağlık Durumunuzu İzleyin başlığı için onClickListener
        val titleTextViewHealthTracking = view.findViewById<TextView>(R.id.titleTextViewHealthTracking)
        val descriptionTextViewHealthTracking = view.findViewById<TextView>(R.id.descriptionTextViewHealthTracking)
        titleTextViewHealthTracking.setOnClickListener {
            toggleTextViewVisibility(descriptionTextViewHealthTracking)
        }

        // Nasıl Çalışır? başlığı için onClickListener
        val titleTextViewHowItWorks = view.findViewById<TextView>(R.id.titleTextViewHowItWorks)
        val descriptionTextViewHowItWorks = view.findViewById<TextView>(R.id.descriptionTextViewHowItWorks)
        titleTextViewHowItWorks.setOnClickListener {
            toggleTextViewVisibility(descriptionTextViewHowItWorks)
        }
// Sağlık Takibi başlığı için onClickListener
        val titleTextViewHealthTracking2 = view.findViewById<TextView>(R.id.titleTextViewHealthTracking2)
        val descriptionTextViewHealthTracking2 = view.findViewById<TextView>(R.id.descriptionTextViewHealthTracking2)
        titleTextViewHealthTracking2.setOnClickListener {
            toggleTextViewVisibility(descriptionTextViewHealthTracking2)
        }

// Güvenlik ve Gizlilik başlığı için onClickListener
        val titleTextViewSecurityPrivacy = view.findViewById<TextView>(R.id.titleTextViewSecurityPrivacy)
        val descriptionTextViewSecurityPrivacy = view.findViewById<TextView>(R.id.descriptionTextViewSecurityPrivacy)
        titleTextViewSecurityPrivacy.setOnClickListener {
            toggleTextViewVisibility(descriptionTextViewSecurityPrivacy)
        }

// Sürekli Gelişim başlığı için onClickListener
        val titleTextViewContinuousImprovement = view.findViewById<TextView>(R.id.titleTextViewContinuousImprovement)
        val descriptionTextViewContinuousImprovement = view.findViewById<TextView>(R.id.descriptionTextViewContinuousImprovement)
        titleTextViewContinuousImprovement.setOnClickListener {
            toggleTextViewVisibility(descriptionTextViewContinuousImprovement)
        }

    }
// TextView bileşeninin görünürlüğünü değiştirmek için kullanılır.
    private fun toggleTextViewVisibility(textView: TextView) {
        if (textView.visibility == View.VISIBLE) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
        }
    }
}