package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.R

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    // Сюда мы сохраним список валют (Код, например, "USD" и его значение, например, 89.5)
    private var ratesList: List<Pair<String, Double>> = emptyList()

    // Метод длч обновления данных в списке, когда они скачаются из интернета
    fun submitRates(newRates: Map<String, Double>) {
        ratesList = newRates.toList()
        notifyDataSetChanged() // Принудительно обновляем экран
    }

    // Создаём внешний вид карточки ищ нашего файла item_currency.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    // Раскладываем данные конкретной валюты по элементам карточки
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val (currencyCode, rateValue) = ratesList[position]
        holder.bind(currencyCode, rateValue)
    }

    // Говорим списку, сколько всего элементов в нём отображать
    override fun getItemCount(): Int = ratesList.size

    // Класс-держатель элементов, который находит TextView внутри карточки
    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCode: TextView = itemView.findViewById(R.id.tvCurrencyCode)
        private val tvRate: TextView = itemView.findViewById(R.id.tvCurrencyRate)
        private val tvName: TextView = itemView.findViewById(R.id.tvCurrencyName)

        fun bind(code: String, rate: Double) {
            tvCode.text = code
            tvRate.text = String.format("%.4f", rate) // Округляем до 4 знаков после запятой
            tvName.text = "Валюта" // Пока ставим заглушку, позже привяжем полные названия
        }
    }
}
