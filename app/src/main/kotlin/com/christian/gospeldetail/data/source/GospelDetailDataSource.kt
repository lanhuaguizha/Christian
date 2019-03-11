package com.christian.gospeldetail.data.source

import com.christian.data.GospelDetailBean

interface GospelDetailDataSource {
    interface LoadGospelDetailsCallback {

        fun onGospelDetailsLoaded(gospelDetails: List<GospelDetailBean>)

        fun onDataNotAvailable()
    }

    interface GetGospelDetailCallback {

        fun onGospelDetailLoaded(gospelDetail: GospelDetailBean)

        fun onDataNotAvailable()
    }

    fun getGospelDetails(callback: LoadGospelDetailsCallback)

    fun getGospelDetail(gospelDetailId: String, callback: GetGospelDetailCallback)

//    fun saveTask(task: Task<*>)
//
//    fun completeTask(task: Task<*>)
//
//    fun completeTask(taskId: String)
//
//    fun activateTask(task: Task<*>)
//
//    fun activateTask(taskId: String)
//
//    fun clearCompletedTasks()
//
//    fun refreshTasks()
//
//    fun deleteAllTasks()
//
//    fun deleteTask(taskId: String)
}