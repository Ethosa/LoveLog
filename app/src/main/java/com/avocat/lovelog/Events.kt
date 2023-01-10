package com.avocat.lovelog


class Events(
    private vararg val eventData: EventData
) {
    /**
     * Returns index of last completed date.
     * @param days days count
     */
    fun lastCompletedIndex(days: Int): Int {
        var i = 0
        while (i < eventData.size-2 && eventData[i+1].daysCount <= days) i++
        return i
    }

    /**
     * Returns last completed date.
     * @param days days count
     */
    fun lastDate(days: Int): EventData {
        return eventData[lastCompletedIndex(days)]
    }

    /**
     * Returns next date is available
     * @param days days count
     */
    fun nextDate(days: Int): EventData? {
        val i = lastCompletedIndex(days)
        return if (i < eventData.size-1)
                eventData[i+1]
            else
                null
    }

    val list = eventData.toList()
}