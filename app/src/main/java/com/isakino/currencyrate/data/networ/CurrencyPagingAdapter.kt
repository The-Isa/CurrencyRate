package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.R

// Поменяли CurrencyEntity на FavoriteCurrencyEntity по ТЗ
class CurrencyPagingAdapter : PagingDataAdapter<FavoriteCurrencyEntity, CurrencyPagingAdapter.PagingViewHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return PagingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class PagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCode: TextView = itemView.findViewById(R.id.tvCurrencyCode)
        private val tvRate: TextView = itemView.findViewById(R.id.tvCurrencyRate)
        private val tvName: TextView = itemView.findViewById(R.id.tvCurrencyName)

        fun bind(entity: FavoriteCurrencyEntity) {
            tvCode.text = entity.code
            tvRate.text = String.format("%.4f", entity.rate)
            tvName.text = "Избранная валюта" // Раз она в этой таблице, она точно в избранном
        }
    }

    class CurrencyDiffCallback : DiffUtil.ItemCallback<FavoriteCurrencyEntity>() {
        override fun areItemsTheSame(oldItem: FavoriteCurrencyEntity, newItem: FavoriteCurrencyEntity): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: FavoriteCurrencyEntity, newItem: FavoriteCurrencyEntity): Boolean {
            return oldItem == newItem
        }
    }
}
