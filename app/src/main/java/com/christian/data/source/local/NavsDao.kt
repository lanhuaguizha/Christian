/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.christian.data.source.local

import android.arch.persistence.room.*
import com.christian.data.Nav

/**
 * Data Access Object for the Navs table.
 */
@Dao
interface NavsDao {

    /**
     * Select all Navs from the Navs table.
     *
     * @return all Navs.
     */
    @Query("SELECT * FROM Navs")
    fun getNavs(): List<Nav>

    /**
     * Select a Nav by id.
     *
     * @param navId the Nav id.
     * @return the Nav with NavId.
     */
    @Query("SELECT * FROM Navs WHERE entryid = :taskId")
    fun getNavById(navId: String): Nav?

    /**
     * Insert a Nav in the database. If the Nav already exists, replace it.
     *
     * @param nav the Nav to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNav(nav: Nav)

    /**
     * Update a Nav.
     *
     * @param nav Nav to be updated
     * @return the number of Navs updated. This should always be 1.
     */
    @Update
    fun updateNav(nav: Nav): Int

    /**
     * Update the complete status of a Nav
     *
     * @param navId    id of the Nav
     * @param completed status to be updated
     */
    @Query("UPDATE Navs SET completed = :completed WHERE entryid = :taskId")
    fun updateCompleted(navId: String, completed: Boolean)

    /**
     * Delete a Nav by id.
     *
     * @return the number of Navs deleted. This should always be 1.
     */
    @Query("DELETE FROM Navs WHERE entryid = :taskId")
    fun deleteNavById(navId: String): Int

    /**
     * Delete all Navs.
     */
    @Query("DELETE FROM Navs")
    fun deleteNavs()

    /**
     * Delete all completed Navs from the table.
     *
     * @return the number of Navs deleted.
     */
    @Query("DELETE FROM Navs WHERE completed = 1")
    fun deleteCompletedNavs(): Int
}