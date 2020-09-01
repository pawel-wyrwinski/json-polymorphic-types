# json-polymorphic-types
(Kotlin + Jackson) Read and write Objects from the same inheritance hierarchy, Jackson recognizes the type based on "discriminator" property.

# The problem
`Animal` abstract class is extended by `Cat` and `Dog` classes.  
`Dog` and `Cat` instances are members of `List<Animal>`.
  
How to marshal and demarshal such collection with only minimal clutter in the code???

# Answer
Jackson has support for SubTypes.  
See documentation on [@JsonSubType](https://github.com/FasterXML/jackson-annotations/wiki/Jackson-Annotations#type-handling) annotation.

# What else was needed for this demo app to work
Some Jackson modules and config options have to be eneabled.
Nothing exotic, just the things that you'd want to have anyway:
 - module for Java JSR310 time (+ formatter to be used with it)
 - module for Kotlin

# What happens in demo?
1. builds a collection of polymorphic objects, then marshals it to the JSON string
2. demarshals a valid JSON (String) to the collection of polymorphic objects, then prints them to the console using `toString()`

# Sample output
```
From object to JSON string:
===========================
[ {
  "condition_type" : "time",
  "priority" : 1,
  "start_at" : "2020-08-12T19:01:18.912911",
  "end_at" : "2020-08-14T19:01:18.913937"
}, {
  "condition_type" : "time",
  "priority" : 2,
  "start_at" : "2020-08-07T19:01:18.913986",
  "end_at" : "2020-08-09T19:01:18.914"
}, {
  "condition_type" : "customer",
  "priority" : 3,
  "condition_name" : "is new customer",
  "condition_expression" : "true"
}, {
  "condition_type" : "time",
  "priority" : 4,
  "start_at" : "2020-07-23T19:01:18.914219",
  "end_at" : "2020-07-28T19:01:18.914237"
}, {
  "condition_type" : "customer",
  "priority" : 5,
  "condition_name" : "#orders",
  "condition_expression" : "gte 5"
}, {
  "condition_type" : "time",
  "priority" : 6,
  "start_at" : "2020-07-03T19:01:18.914257",
  "end_at" : "2020-07-13T19:01:18.914277"
} ]

From String to object:
===========================
[TimeBasedCondition(conditionType='time', priority=1, startAt=2020-08-12T18:16:53.102823, endAt=2020-08-14T18:16:53.104542), TimeBasedCondition(conditionType='time', priority=2, startAt=2020-08-07T18:16:53.104625, endAt=2020-08-09T18:16:53.104647), CustomerBasedCondition(conditionType='customer', priority=3, conditionName='is new customer', conditionExpression='true'), TimeBasedCondition(conditionType='time', priority=4, startAt=2020-07-23T18:16:53.105048, endAt=2020-07-28T18:16:53.105080), CustomerBasedCondition(conditionType='customer', priority=5, conditionName='#orders', conditionExpression='gte 5'), TimeBasedCondition(conditionType='time', priority=6, startAt=2020-07-03T18:16:53.105110, endAt=2020-07-13T18:16:53.105142)]
```
