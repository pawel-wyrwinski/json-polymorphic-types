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
