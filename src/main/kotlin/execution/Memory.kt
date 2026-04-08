package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address

data class Memory(private val memory: Array<Int> = Array(256) { 0 }) {

    fun load(address: Address.Raw): Int = memory[address.value]
    fun store(address: Address.Raw, value: Int): Address.Raw {
        memory[address.value] = value
        return address
    }

    override fun toString(): String {
        return "Memory(memory=${memory.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memory

        return memory.contentEquals(other.memory)
    }

    override fun hashCode(): Int {
        return memory.contentHashCode()
    }
}