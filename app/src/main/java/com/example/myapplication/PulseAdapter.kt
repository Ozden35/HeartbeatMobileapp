package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PulseAdapter(private var pulseList: MutableList<PulseData>) : RecyclerView.Adapter<PulseAdapter.PulseViewHolder>() {

    // Inner class, ViewHolder'ı tanımlar
    inner class PulseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pulseRateTextView: TextView = itemView.findViewById(R.id.pulseRateTextView)
        val measurementTimeTextView: TextView = itemView.findViewById(R.id.measurementTimeTextView)
    }

    // Yeni bir ViewHolder oluşturulduğunda çağrılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PulseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pulse_item, parent, false)
        return PulseViewHolder(itemView)
    }

    // ViewHolder'ın içeriğini bağlama işlemi gerçekleştirilir
    override fun onBindViewHolder(holder: PulseViewHolder, position: Int) {
        val currentItem = pulseList[position]
        holder.pulseRateTextView.text = "Nabız Değeri: ${currentItem.pulse_rate}"
        holder.measurementTimeTextView.text = "Ölçüm Zamanı: ${currentItem.timestamp}"
    }

    // Veri kümesini güncelleyen fonksiyon
    fun setPulseData(data: List<PulseData>) {
        pulseList.clear()
        pulseList.addAll(data)
        notifyDataSetChanged()
    }

    // Toplam öğe sayısını döndüren fonksiyon
    override fun getItemCount() = pulseList.size
}