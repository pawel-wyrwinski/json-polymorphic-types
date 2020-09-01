package org.wyrwipaw.demo.model

import java.time.LocalDateTime

class TimeBasedCondition(
    conditionType: String = "time",
    priority: Int,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
): Condition(conditionType = conditionType, priority = priority) {

    override fun toString(): String {
        return "TimeBasedCondition(conditionType='$conditionType', priority=$priority, startAt=$startAt, endAt=$endAt)"
    }
}