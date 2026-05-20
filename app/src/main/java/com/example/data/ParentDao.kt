package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParentDao {
    @Query("SELECT * FROM parents ORDER BY studentName ASC")
    fun getAllParentsFlow(): Flow<List<ParentEntity>>

    @Query("SELECT * FROM parents ORDER BY studentName ASC")
    suspend fun getAllParents(): List<ParentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParents(parents: List<ParentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(parent: ParentEntity)

    @Delete
    suspend fun deleteParent(parent: ParentEntity)

    @Query("DELETE FROM parents WHERE id = :id")
    suspend fun deleteParentById(id: String)

    @Query("DELETE FROM parents")
    suspend fun clearAll()
}
