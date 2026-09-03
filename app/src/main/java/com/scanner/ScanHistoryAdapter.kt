package com.scanner.lite

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanHistoryAdapter(private val historyList: List<ScanRecord>) :
    RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        val context = holder.itemView.context
        val timeStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(item.timestamp))

        // 使用资源占位符替代直接字符串拼接
        holder.tvText.text = context.getString(R.string.history_item_format, item.content, timeStr)

        holder.itemView.setOnClickListener {
            copyToClipboard(context, item.content)
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("ScanResult", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.msg_copied), Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int = historyList.size
}