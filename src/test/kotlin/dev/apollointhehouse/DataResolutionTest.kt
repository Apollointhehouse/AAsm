package dev.apollointhehouse

import dev.apollointhehouse.parsing.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class DataResolutionTest {
    @Test
    fun testDataResolution() {
        val example = """
                IN    MAX
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
        
                MAX:  .DATA 0
                TEMP: .DATA 0
                A:    .DATA 0
                B:    .DATA 1
        """
        val parser = Parser()
        val asm = parser.parseASM(example)
        
        // Data:
        // 12: .DATA 0
        // 13: .DATA 0
        // 14: .DATA 0
        // 15: .DATA 1

        // Check resolved data
        assertEquals(0, asm.instructions[12].bin) // 0
        assertEquals(0, asm.instructions[13].bin) // 0
        assertEquals(0, asm.instructions[14].bin) // 0
        assertEquals(1, asm.instructions[15].bin) // 1
    }
}
