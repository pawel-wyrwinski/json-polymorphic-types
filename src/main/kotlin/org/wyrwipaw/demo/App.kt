/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.wyrwipaw.demo

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.wyrwipaw.demo.model.Condition
import org.wyrwipaw.demo.model.CustomerBasedCondition
import org.wyrwipaw.demo.model.TimeBasedCondition
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class App {

    constructor() {
        val module = SimpleModule()

        // We want to support this format for all dates
        val formatterWrite = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val formatterRead = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        module.addSerializer<ZonedDateTime>(
            ZonedDateTime::class.java,
            object : JsonSerializer<ZonedDateTime>() {
                override fun serialize(
                    zonedDateTime: ZonedDateTime,
                    jsonGenerator: JsonGenerator,
                    serializerProvider: SerializerProvider
                ) {
                    jsonGenerator.writeString(formatterWrite.format(zonedDateTime))
                }
            })

        module.addDeserializer(
            ZonedDateTime::class.java,
            object : JsonDeserializer<ZonedDateTime>() {
                override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ZonedDateTime {
                    return ZonedDateTime.from(formatterRead.parse(p.text))
                }
            })

        mapper.registerModule(module)
        // This is needed to write dates as strings and not as unix timestamp (int)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        // Use camelCase->snake_case in object->json
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE

        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        mapper.registerModule(
            KotlinModule(
                nullToEmptyCollection = true,
                nullToEmptyMap = true,
                nullisSameAsDefault = true
            )
        )
    }

    private val mapper = ObjectMapper()

    /**
     * Demo - inflate collection of polymorphic objects
     */
    fun readJson(): List<Condition> {
        val type = mapper.typeFactory.constructCollectionType(List::class.java, Condition::class.java)
        val parsed: List<Condition> = mapper.readValue("""
            [ {
              "condition_type" : "time",
              "priority" : 1,
              "start_at" : "2020-08-12T18:16:53.102823",
              "end_at" : "2020-08-14T18:16:53.104542"
            }, {
              "condition_type" : "time",
              "priority" : 2,
              "start_at" : "2020-08-07T18:16:53.104625",
              "end_at" : "2020-08-09T18:16:53.104647"
            }, {
              "condition_type" : "customer",
              "priority" : 3,
              "condition_name" : "is new customer",
              "condition_expression" : "true"
            }, {
              "condition_type" : "time",
              "priority" : 4,
              "start_at" : "2020-07-23T18:16:53.105048",
              "end_at" : "2020-07-28T18:16:53.10508"
            }, {
              "condition_type" : "customer",
              "priority" : 5,
              "condition_name" : "#orders",
              "condition_expression" : "gte 5"
            }, {
              "condition_type" : "time",
              "priority" : 6,
              "start_at" : "2020-07-03T18:16:53.10511",
              "end_at" : "2020-07-13T18:16:53.105142"
            } ]
        """.trimIndent(), type
        )
        return parsed
    }

    /**
     * Demo - marshal a collection of polymorphic objects into JSON string
     */
    fun writeJson(conditions: List<Condition>) : String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(conditions)
    }
}

fun assembleTestData(): List<Condition> {
    return listOf(
        TimeBasedCondition(
            priority = 1,
            startAt = LocalDateTime.now().minus(20, ChronoUnit.DAYS),
            endAt = LocalDateTime.now().minus(18, ChronoUnit.DAYS)
        ),
        TimeBasedCondition(
            priority = 2,
            startAt = LocalDateTime.now().minus(25, ChronoUnit.DAYS),
            endAt = LocalDateTime.now().minus(23, ChronoUnit.DAYS)
        ),
        CustomerBasedCondition(
            priority = 3,
            conditionName = "is new customer",
            conditionExpression = "true"
        ),
        TimeBasedCondition(
            priority = 4,
            startAt = LocalDateTime.now().minus(40, ChronoUnit.DAYS),
            endAt = LocalDateTime.now().minus(35, ChronoUnit.DAYS)
        ),
        CustomerBasedCondition(
            priority = 5,
            conditionName = "#orders",
            conditionExpression = "gte 5" // "greater than or equal 5 (other values might be: "lt 5", "eq 5", "ne 5", ...)
        ),
        TimeBasedCondition(
            priority = 6,
            startAt = LocalDateTime.now().minus(60, ChronoUnit.DAYS),
            endAt = LocalDateTime.now().minus(50, ChronoUnit.DAYS)
        )
    )
}

fun main(args: Array<String>) {
    val app = App()

    val testData = assembleTestData()
    println("\nFrom object to JSON string:")
    println("===========================")
    println(app.writeJson(testData))

    println("\nFrom String to object:")
    println("===========================")
    println(app.readJson())


}
