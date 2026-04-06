package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address

data class Memory(private val memory: Array<Int> = Array(256) { 0 }) {

    fun load(address: Address.Raw): Int = memory[address.value]
    fun store(address: Address.Raw, value: Int): Address.Raw {
        memory[address.value] = value
        return address
    }

    private var stackPtr: Int = (memory.size - 1)
        set(value) {
            field = value % memory.size
        }

    fun push(value: Int): Address.Raw {
        store(Address.Raw(stackPtr), value)
        return Address.Raw(stackPtr--)
    }

    fun pop(): Address.Raw {
        stackPtr++
        return Address.Raw(stackPtr)
    }

    fun peek(): Address.Raw {
        return Address.Raw(stackPtr)
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