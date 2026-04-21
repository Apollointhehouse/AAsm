package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address

class ProgramCounter(val memory: Memory) {
    private var addr: Short = 0

    fun clock() {
        addr++
    }

    fun load(): Short = memory.load(Address.Raw(addr))
    fun set(address: Address.Raw) {
        addr = (address.value - 1).toShort()
    }
}