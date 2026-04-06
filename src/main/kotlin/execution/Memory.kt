package dev.apollointhehouse.execution

import java.util.*

data class Memory(private val memory: Array<Int> = Array(256) { 0 }) : Stack<Int>() {

    fun load(address: Int): Int = memory[address]
    fun store(address: Int, value: Int) {
        memory[address] = value
    }

    private var stackPtr: Int = (memory.size - 1)
        set(value) {
            field = value % memory.size
        }

    override fun push(value: Int): Int {
        store(stackPtr, value)
        return stackPtr--
    }

    override fun pop(): Int {
        stackPtr++
        return stackPtr
    }

    override fun peek(): Int {
        return stackPtr
    }

    override fun toString(): String {
        return "Memory(memory=${memory.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memory

        if (stackPtr != other.stackPtr) return false
        if (!memory.contentEquals(other.memory)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stackPtr
        result = 31 * result + memory.contentHashCode()
        return result
    }
}