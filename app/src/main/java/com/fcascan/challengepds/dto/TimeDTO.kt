package com.fcascan.challengepds.dto

data class TimeDTO(
    var id: String = "",        // "1"
    var year: Int = 0,          // 2023
    var month: Int = 0,         // 12
    var day: Int = 0,           // 11
    var hour: Int = 0,          // 15
    var minute: Int = 0,        // 37
    var seconds: Int = 0,       // 0
    var milliSeconds: Int = 0,  // 107
    var dateTime: String = "",  // "2023-12-11T15:37:00.1070951"
    var date: String = "",      // "12/11/2023"
    var time: String = "",      // "15:37"
    var timeZone: String = "",  // "America/Argentina/Buenos_Aires"
    var dayOfWeek: String = "", // "Monday",
    var dstActive: Boolean      // false
) {
    override fun toString(): String {
        return """TimeDTO{id: $id, dayOfWeek: $dayOfWeek, dateTime: $dateTime}"""
    }
}