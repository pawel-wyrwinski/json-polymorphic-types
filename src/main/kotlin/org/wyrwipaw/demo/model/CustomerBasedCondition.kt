package org.wyrwipaw.demo.model

class CustomerBasedCondition(
    conditionType: String = "customer",
    priority: Int,
    val conditionName: String,
    val conditionExpression: String
) : Condition(conditionType = conditionType, priority = priority) {

    override fun toString(): String {
        return "CustomerBasedCondition(conditionType='$conditionType', priority=$priority, conditionName='$conditionName', conditionExpression='$conditionExpression')"
    }
}