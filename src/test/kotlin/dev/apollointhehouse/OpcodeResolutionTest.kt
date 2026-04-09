package dev.apollointhehouse

import dev.apollointhehouse.asm.OpCode
import dev.apollointhehouse.parsing.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class OpcodeResolutionTest {
    @Test
    fun testOpcodeResolution() {
        val example = """
            IN N
    
    START:  LOAD  ZERO
            COMP  N
            JMPLT END
            
            LOAD  SUM
            ADD   N
            STORE SUM
            
            IN    N
            JMP   START
            
    END:    OUT   SUM
            HALT
            
            SUM:  .DATA 0
            N:    .DATA 0
            ZERO: .DATA 0
        """
        val parser = Parser()
        val asm = parser.parseASM(example)
        
        // Op Codes:
        // 0:  IN
        // 1:  LOAD
        // 2:  COMP
        // 3:  JMPLT
        // 4:  LOAD
        // 5:  ADD
        // 6:  STORE
        // 7:  IN
        // 8:  JMP
        // 9:  OUT
        // 10: HALT
        
        // Check resolved op code's
        assertEquals(OpCode.IN,    asm.instructions[0].opCode) // IN
        assertEquals(OpCode.LOAD,  asm.instructions[1].opCode) // LOAD
        assertEquals(OpCode.COMP,  asm.instructions[2].opCode) // COMP
        assertEquals(OpCode.JMPLT, asm.instructions[3].opCode) // JMPLT
        assertEquals(OpCode.LOAD,  asm.instructions[4].opCode) // LOAD
        assertEquals(OpCode.ADD,   asm.instructions[5].opCode) // ADD
        assertEquals(OpCode.STORE, asm.instructions[6].opCode) // STORE
        assertEquals(OpCode.IN,    asm.instructions[7].opCode) // IN
        assertEquals(OpCode.JMP,   asm.instructions[8].opCode) // JMP
        assertEquals(OpCode.OUT,   asm.instructions[9].opCode) // OUT
        assertEquals(OpCode.HALT, asm.instructions[10].opCode) // HALT
    }
}
