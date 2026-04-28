package dev.apollointhehouse

fun Int.toBinString(size: Int = 8): String = this
    .toString(2)
    .padStart(size, '0')


fun <T> Iterator<T>.nextOr(block: () -> T): T {
    if (!hasNext()) return block()
    return next()
}