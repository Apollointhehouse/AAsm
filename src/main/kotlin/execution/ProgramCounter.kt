package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address

class ProgramCounter(val memory: Memory) {
    private var addr: Int = 0

    fun clock() {
        addr++
    }

    fun load(): Int = memory.load(Address.Raw(addr))
    fun set(address: Address.Raw) {
        addr = address.value - 1
    }
}