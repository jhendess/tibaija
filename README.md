# Tibaija - a TI-Basic Interpreter for Java

Tibaija is an interpreter for the TI-Basic language on graphical calculators from Texas Instruments. The main target platform will be programs for the TI-83+.

The long-term goal is to make most TI-Basic programs for the TI-83+ executable on an ordinary java-compatible platform. Future versions will also include support for drawing on a virtual calculator screen, setting a custom keyboard mapping.

The current state of the software is even less than pre-alpha. There exists a very basic interactive interpreter that can do some simple calculations without any control structures. So don't expect anything great from the current version - I warned you! ;)


## Building and running tibaija

To build tibaija you need to invoke a maven build and skip the tests (some tests are currently failing):
 
```
mvn clean package -DskipTests
```

After building you can start the interpreter - see the built-in help for more information:

```
java -jar target/tibaija-0.0.1-SNAPSHOT.jar
```

## Development

If you want to help developing tibaija, you should rebuild the generated code from Antlr every time you change the underlying grammar. You should also do this if you're trying to open the project in an IDE for the first time - otherwise the IDE will complain about missing files. To do this, run:

```
mvn antlr4:antlr4
```