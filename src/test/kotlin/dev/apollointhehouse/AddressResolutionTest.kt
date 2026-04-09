package dev.apollointhehouse

import dev.apollointhehouse.parsing.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class AddressResolutionTest {
    @Test
    fun testAddressResolution() {
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

        println("Symbol Table: ${asm.symbolTable}")
        
        // Addresses:
        // 0: N
        // 1: ZERO
        // 2: N
        // 3: END
        // 4: SUM
        // 5: N
        // 6: SUM
        // 7: N
        // 8: START
        // 9: SUM
        // 10: 0
        
        // Check resolved addresses
        assertEquals(12, asm.instructions[0].addr.value) // N
        assertEquals(13, asm.instructions[1].addr.value) // ZERO
        assertEquals(12, asm.instructions[2].addr.value) // N
        assertEquals(9,  asm.instructions[3].addr.value) // END
        assertEquals(11, asm.instructions[4].addr.value) // SUM
        assertEquals(12, asm.instructions[5].addr.value) // N
        assertEquals(11, asm.instructions[6].addr.value) // SUM
        assertEquals(12, asm.instructions[7].addr.value) // N
        assertEquals(1,  asm.instructions[8].addr.value) // START
        assertEquals(11, asm.instructions[9].addr.value) // SUM
        assertEquals(0, asm.instructions[10].addr.value) // 0
    }
}
