package com.scanner.lite

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 绑定清空按钮点击事件（弹窗二次确认）
        findViewById<ImageButton>(R.id.btnClearHistory).setOnClickListener {
            showClearHistoryDialog()
        }

        loadHistoryData()
    }

    private fun loadHistoryData() {
        lifecycleScope.launch {
            val historyList = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).scanDao().getAllHistory()
            }
            recyclerView.adapter = ScanHistoryAdapter(historyList)
        }
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_clear_title)
            .setMessage(R.string.dialog_clear_msg)
            .setPositiveButton(R.string.action_confirm) { _, _ ->
                clearHistory()
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    private fun clearHistory() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).scanDao().clearHistory()
            }
            Toast.makeText(this@ScanHistoryActivity, getString(R.string.msg_history_cleared), Toast.LENGTH_SHORT).show()
            loadHistoryData() // 清空后刷新列表
        }
    }
}