package com.isakino.currencyrate.data.networ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isakino.currencyrate.R
import de.hdodenhof.circleimageview.CircleImageView

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var ratesList: List<Pair<String, Double>> = emptyList()

    var onItemClick: ((String, Double) -> Unit)? = null
    var onItemLongClick: ((String, Double) -> Unit)? = null

    private var lastPosition = -1

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

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = ratesList.size

    private fun setAnimation(view: View, position: Int) {

        if (position > lastPosition) {

            val animation = AnimationUtils.loadAnimation(
                view.context,
                R.anim.item_animation
            )

            view.startAnimation(animation)

            lastPosition = position
        }
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivFlag: CircleImageView =
            itemView.findViewById(R.id.ivFlag)

        private val tvCode: TextView =
            itemView.findViewById(R.id.tvCurrencyCode)

        private val tvRate: TextView =
            itemView.findViewById(R.id.tvCurrencyRate)

        private val tvName: TextView =
            itemView.findViewById(R.id.tvCurrencyName)

        fun bind(code: String, rate: Double) {

            tvCode.text = code
            tvRate.text = String.format("%.4f", rate)
            tvName.text = CurrencyNames.getName(code)

            val countryCode = CurrencyFlags.get(code)

            if (countryCode != null) {

                Glide.with(itemView.context)
                    .load("https://flagcdn.com/w80/$countryCode.png")
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivFlag)

            } else {

                ivFlag.setImageResource(R.drawable.ic_launcher_foreground)

            }

            itemView.setOnClickListener {
                onItemClick?.invoke(code, rate)
            }

            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(code, rate)
                true
            }
        }
    }
}