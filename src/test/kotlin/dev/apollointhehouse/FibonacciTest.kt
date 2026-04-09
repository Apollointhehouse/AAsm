package dev.apollointhehouse

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class FibonacciTest {
    @Test
    fun testFibonacci() {
        val program = """
        START:  OUT   A
                LOAD  B
                STORE TEMP
                ADD   A
                STORE B
        
                LOAD  TEMP
                STORE A
        
                COMP  MAX
                JMPLT END
        
                JMP   START
        
        END:    HALT
        
                MAX:  .DATA 10
                TEMP: .DATA 0
                A:    .DATA 0
                B:    .DATA 1
        """

        val parser = Parser()
        val asm = parser.parseASM(program)
        val memory = Memory(Array(1024) { 0 })

        asm.instructions.forEachIndexed { ptr, (bin) ->
            memory.store(Address.Raw(ptr), bin)
        }

        // Let's check the memory after HALT
        val cu = ControlUnit(memory)
        try {
            var cycles = 0
            while (cycles < 1000) {
                cu.clock()
                cycles++
            }
        } catch (_: HaltException) {
        }
        
        val aAddr = asm.symbolTable["A"]!!
        val bAddr = asm.symbolTable["B"]!!

        assertEquals(21, memory.load(aAddr))
        assertEquals(34, memory.load(bAddr))
    }
}
