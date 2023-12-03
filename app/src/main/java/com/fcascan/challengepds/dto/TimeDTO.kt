package com.fcascan.challengepds.dto

data class TimeDTO(
    var id: String = "",        // "1"
    var currentDateTime: String = "",   // "2023-12-02T22:40-05:00"
    var utcOffset: String = "",     // "-05:00:00"
    var isDayLightSavingsTime: Boolean = false, // false
    var dayOfTheWeek: String = "",      // "Saturday"
    var timeZoneName: String = "",      // "Eastern Standard Time"
    var currentFileTime: Long = 0,      // 133460304037611347
    var ordinalDate: String = "",       // "2023-336"
    var serviceResponse: Any? = null,       // null
) {
    override fun toString(): String {
        return """TimeDTO{id: $id, currentDateTime: $currentDateTime, dayOfTheWeek: $dayOfTheWeek}"""
    }
}