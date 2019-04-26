package com.christian.data

import android.os.Parcel
import android.os.Parcelable

data class Gospels(
        var title: String = "",
        var subtitle: String = "",
        var gospelDetails: List<GospelDetails> = arrayListOf(),
        var date: String = "",
        var author: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            ArrayList<GospelDetails>().apply { source.readList(this, GospelDetails::class.java.classLoader) },
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(subtitle)
        writeList(gospelDetails)
        writeString(date)
        writeString(author)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Gospels> = object : Parcelable.Creator<Gospels> {
            override fun createFromParcel(source: Parcel): Gospels = Gospels(source)
            override fun newArray(size: Int): Array<Gospels?> = arrayOfNulls(size)
        }
    }
}

data class GospelDetails(
        val content: String,
        val image: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(content)
        writeString(image)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GospelDetails> = object : Parcelable.Creator<GospelDetails> {
            override fun createFromParcel(source: Parcel): GospelDetails = GospelDetails(source)
            override fun newArray(size: Int): Array<GospelDetails?> = arrayOfNulls(size)
        }
    }
}

data class Message(
        val id: String,
        var text: String = "",
        var name: String = "",
        var photoUrl: String = "",
        var imageUrl: String = "")

data class MeBean(
        val id: Int = 0,
        val url: String = "",
        val name: String = "",
        val nickName: String = "",
        val address: Address = Address(),
        val settings: List<Settings> = arrayListOf()
)

data class Settings(
        val name: String = "",
        val url: String = "",
        val desc: String = ""
)

// 头像点击开后的详情
data class Address(
        val street: String = "",
        val city: String = "",
        val country: String = ""
)