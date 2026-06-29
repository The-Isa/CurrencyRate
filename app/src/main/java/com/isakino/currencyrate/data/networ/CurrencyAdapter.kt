package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.R

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var ratesList: List<Pair<String, Double>> = emptyList()

    // Обычный клик
    var onItemClick: ((String, Double) -> Unit)? = null

    // Долгое нажатие
    var onItemLongClick: ((String, Double) -> Unit)? = null

    fun submitRates(newRates: Map<String, Double>) {
        ratesList = newRates.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val (code, rate) = ratesList[position]
        holder.bind(code, rate)
    }

    override fun getItemCount(): Int = ratesList.size

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvCode: TextView = itemView.findViewById(R.id.tvCurrencyCode)
        private val tvRate: TextView = itemView.findViewById(R.id.tvCurrencyRate)
        private val tvName: TextView = itemView.findViewById(R.id.tvCurrencyName)

        fun bind(code: String, rate: Double) {

            tvCode.text = code
            tvRate.text = String.format("%.4f", rate)

            // Название валюты
            tvName.text = CurrencyNames.getName(code)

            // Обычный клик
            itemView.setOnClickListener {
                onItemClick?.invoke(code, rate)
            }

            // Долгое нажатие
            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(code, rate)
                true
            }
        }
    }
}