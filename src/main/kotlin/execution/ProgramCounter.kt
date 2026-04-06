package dev.apollointhehouse.execution

class ProgramCounter(val memory: Memory) {
    var addr: Int = 0

    fun clock() {
        addr++
    }

    fun load(): Int = memory.load(addr)
    fun set(address: Int) {
        addr = address - 1
    }
}