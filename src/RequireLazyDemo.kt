/**
 * Demonstrates how Kotlin's lazy error message in require()
 * avoids unnecessary memory allocation and computation.
 */

/**
 * Custom implementation similar to Kotlin's stdlib require().
 *
 * Why lazyMessage: () -> Any ?
 *
 * - Message is provided as a lambda
 * - Lambda executes ONLY when condition fails
 * - No string or object created when condition passes
 *
 * JVM Optimization:
 * - inline -> no function call overhead
 * - no lambda object allocation
 *
 * Result:
 * Zero-cost abstraction when validation succeeds.
 */
inline fun requireLazy(
    value: Boolean,
    lazyMessage: () -> Any
) {
    // FAST PATH
    if (value) return

    // SLOW PATH
    val message = lazyMessage()
    throw IllegalArgumentException(message.toString())
}

/**
 * Old way that does function call overhead
 */
fun requireLazy(
    value: Boolean,
    lazyMessage: Any
) {
    // FAST PATH
    if (value) return

    // SLOW PATH
    throw IllegalArgumentException(lazyMessage.toString())
}


/**
 * Simulates an expensive message creation.
 */
fun expensiveMessage(): String {
    println("Computing error message...")
    return "Value must be positive"
}

/**
 * Kotlin program entry point
 */
fun main() {

    val number = 10


    println("expensiveMessage() is called")
    // expensiveMessage() is called
    requireLazy(number > 0, "$number ${expensiveMessage()}") // expensiveMessage() is called eventhough number is positive



    println("expensiveMessage() is NOT called")
    // expensiveMessage() is NOT called
    requireLazy(number > 0) {
        "$number ${expensiveMessage()}"
    }


    // Uncomment to test failure case
    /*
    val invalid = -5
    requireLazy(invalid > 0) {
        expensiveMessage()
    }
    */
}

/*
OUTPUT when number = 10

Program continues normally

OUTPUT when invalid = -5

Computing error message...
Exception in thread "main" java.lang.IllegalArgumentException: Value must be positive
*/
