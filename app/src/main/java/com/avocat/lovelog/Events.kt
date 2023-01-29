package com.avocat.lovelog

import androidx.compose.runtime.Stable


@Stable
class Events(
    vararg eventData: EventData
) {
    private val events = eventData.toMutableList()

    /**
     * Returns index of last completed date.
     * @param days days count
     */
    fun lastCompletedIndex(days: Int): Int {
        var i = 0
        while (i < events.size-2 && events[i+1].daysCount <= days) i++
        return i
    }

    /**
     * Returns last completed date.
     * @param days days count
     */
    fun lastDate(days: Int): EventData {
        return events[lastCompletedIndex(days)]
    }

    /**
     * Returns next date is available
     * @param days days count
     */
    fun nextDate(days: Int): EventData? {
        val i = lastCompletedIndex(days)
        return if (i < events.size-1)
            events[i+1]
            else
                null
    }

    fun list(): List<EventData> {
        return events.toList()
    }

    fun addEvent(event: EventData) {
        events.add(event)
    }
}