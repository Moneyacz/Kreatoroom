package com.dicoding.kreatoroom.local.dao

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dicoding.kreatoroom.local.entity.Entity
import androidx.room.Query

interface KreatoProductDao {

    @Query("Select * FROM Product")
    suspend fun getAllProduct(): PagingSource<Int, Entity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg product: Entity)

    @Query("DELETE FROM Product")
    suspend fun deleteProduct()

}