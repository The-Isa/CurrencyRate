package com.isakino.currencyrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.NotificationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notificationList: List<NotificationEntity> = emptyList()

    var onDeleteClick: ((NotificationEntity) -> Unit)? = null

    fun submitList(list: List<NotificationEntity>) {
        notificationList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)

        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int = notificationList.size

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        holder.bind(notificationList[position])
    }

    inner class NotificationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvTitle =
            itemView.findViewById<TextView>(R.id.tvTitle)

        private val tvMessage =
            itemView.findViewById<TextView>(R.id.tvMessage)

        private val tvTime =
            itemView.findViewById<TextView>(R.id.tvTime)

        private val btnDelete =
            itemView.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(notification: NotificationEntity) {

            val formatter = SimpleDateFormat(
                "dd.MM.yyyy HH:mm",
                Locale.getDefault()
            )

            tvTitle.text = notification.currencyCode
            tvMessage.text = notification.message
            tvTime.text = "📅 ${formatter.format(Date(notification.triggerTime))}"

            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(notification)
            }
        }
    }
}