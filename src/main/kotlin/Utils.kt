package dev.apollointhehouse

fun Int.toBinString(size: Int = 8): String = this
    .toString(2)
    .padStart(size, '0')