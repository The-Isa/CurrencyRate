package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.R

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList: List<HistoryCurrencyEntity> = emptyList()

    // Метод для обновления данных в списке истории
    fun submitList(newList: List<HistoryCurrencyEntity>) {
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCode: TextView = itemView.findViewById(R.id.tvCurrencyCode)
        private val tvRate: TextView = itemView.findViewById(R.id.tvCurrencyRate)
        private val tvName: TextView = itemView.findViewById(R.id.tvCurrencyName)

        fun bind(item: HistoryCurrencyEntity) {
            tvCode.text = item.code
            tvRate.text = String.format("%.4f", item.rate)
            tvName.text = "Просмотрено недавно" // Показываем статус для наглядности
        }
    }
}
