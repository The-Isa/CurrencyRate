package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isakino.currencyrate.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import de.hdodenhof.circleimageview.CircleImageView

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList: List<HistoryCurrencyEntity> = emptyList()

    fun submitList(newList: List<HistoryCurrencyEntity>) {
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)

        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivFlag: CircleImageView =
            itemView.findViewById(R.id.ivFlag)

        private val tvCode: TextView =
            itemView.findViewById(R.id.tvCurrencyCode)

        private val tvRate: TextView =
            itemView.findViewById(R.id.tvCurrencyRate)

        private val tvName: TextView =
            itemView.findViewById(R.id.tvCurrencyName)

        fun bind(item: HistoryCurrencyEntity) {

            tvCode.text = item.code
            tvRate.text = String.format("%.4f", item.rate)

            val now = System.currentTimeMillis()
            val diff = now - item.timestamp

            tvName.text = when {

                diff < 60_000 ->
                    "Только что"

                diff < 60 * 60_000 ->
                    "${diff / 60_000} мин назад"

                diff < 24 * 60 * 60_000 ->
                    SimpleDateFormat("Сегодня HH:mm", Locale.getDefault())
                        .format(Date(item.timestamp))

                diff < 48 * 60 * 60_000 ->
                    SimpleDateFormat("'Вчера' HH:mm", Locale.getDefault())
                        .format(Date(item.timestamp))

                else ->
                    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(Date(item.timestamp))
            }

            val countryCode = CurrencyFlags.get(item.code)

            if (countryCode != null) {

                Glide.with(itemView.context)
                    .load("https://flagcdn.com/w80/$countryCode.png")
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivFlag)

            } else {

                ivFlag.setImageResource(R.drawable.ic_launcher_foreground)

            }
        }
    }
}