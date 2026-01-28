/**
 * Demonstrates how Kotlin's lazy error message in require()
 * avoids unnecessary memory allocation and computation.
 */

/**
 * Custom implementation similar to Kotlin's stdlib require().
 *
 * Why lazyMessage: () -> Any ?
 *
 * - The message is provided as a lambda.
 * - The lambda is executed ONLY when the condition fails.
 * - No string or object is created when the condition passes.
 *
 * JVM Optimizations:
 * - inline -> no function call overhead
 * - no lambda object allocation
 *
 * Result:
 * A zero-cost abstraction when validation succeeds.
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
 * Non-lazy version (old-style approach).
 *
 * The message is evaluated BEFORE calling this function,
 * even when the condition is true.
 *
 * This causes unnecessary computation and object creation.
 */
fun requireEager(
    value: Boolean,
    message: Any
) {
    // FAST PATH
    if (value) return

    // SLOW PATH
    throw IllegalArgumentException(message.toString())
}

/**
 * Simulates an expensive message creation.
 */
fun expensiveMessage(): String {
    println("Computing error message...")
    return "Value must be positive"
}

/**
 * Kotlin program entry point.
 */
fun main() {

    val number = 10

    println("expensiveMessage() IS called")
    // expensiveMessage() is called even though number is positive
    requireEager(number > 0, "$number ${expensiveMessage()}")

    println()

    println("expensiveMessage() is NOT called")
    // expensiveMessage() is NOT called when number is positive
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
EXPECTED OUTPUT

expensiveMessage() IS called
Computing error message...

expensiveMessage() is NOT called
*/
