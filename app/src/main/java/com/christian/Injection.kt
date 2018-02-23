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
package com.christian

import android.content.Context
import com.christian.data.source.NavsRepository
//import com.christian.data.source.local.NavsLocalDataSource
//import com.christian.data.source.local.ToDoDatabase
import com.christian.data.source.remote.FakeNavsRemoteDataSource
import com.christian.util.AppExecutors

/**
 * Enables injection of mock implementations for
 * [NavsDataSource] at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
object Injection {
    fun provideNavsRepository(context: Context): NavsRepository {
//        val database = ToDoDatabase.getInstance(context)
//        return NavsRepository.getInstance(FakeNavsRemoteDataSource.getInstance(), NavsLocalDataSource.getInstance(AppExecutors(), database.NavDao()))
        return NavsRepository.getInstance(FakeNavsRemoteDataSource.getInstance())

    }
}