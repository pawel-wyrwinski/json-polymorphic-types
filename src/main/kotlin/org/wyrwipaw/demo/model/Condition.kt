package org.wyrwipaw.demo.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "condition_type")
@JsonSubTypes(
    JsonSubTypes.Type(value = TimeBasedCondition::class, name = "time"),
    JsonSubTypes.Type(value = CustomerBasedCondition::class, name = "customer")
)
abstract class Condition(
    open val conditionType: String,
    open val priority: Int
) {
}