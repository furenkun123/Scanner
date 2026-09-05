package com.scanner.lite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAllHistory(): List<ScanRecord>

    @Insert
    suspend fun insert(record: ScanRecord)

    @Query("DELETE FROM scan_history")
    suspend fun clearHistory()
}