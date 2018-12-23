package com.christian.data

class MeBean {
    var id: Int = 0
    var url: String? = null
    var name: String? = null
    var nickName: String? = null
    var address: Address? = null
    var settings: List<Settings> = arrayListOf()

    inner class Address {
        var street: String? = null
        var city: String? = null
        var country: String? = null
    }

    class Settings {
        var name: String? = null
        var url: String? = null
        var desc: String? = null
    }
}
