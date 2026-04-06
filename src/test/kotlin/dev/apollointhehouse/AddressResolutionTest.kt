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
            
            IN N
            JMP START
            
    END:    OUT SUM
            HALT
            
            SUM: 0
            N: 0
            ZERO: 0
        """
        val parser = Parser()
        val asm = parser.parseASM(example)

        println("Symbol Table: ${asm.symbolTable}")
        
        // Instructions:
        // 0: IN N
        // 1: START: LOAD ZERO
        // 2: COMP N
        // 3: JMPLT END
        // 4: LOAD SUM
        // 5: ADD N
        // 6: STORE SUM
        // 7: IN N
        // 8: JMP START
        // 9: END: OUT SUM
        // 10: HALT
        
        // Data:
        // 11: SUM
        // 12: N
        // 13: ZERO

        assertEquals(1, asm.symbolTable["START"]?.value)
        assertEquals(9, asm.symbolTable["END"]?.value)
        assertEquals(11, asm.symbolTable["SUM"]?.value)
        assertEquals(12, asm.symbolTable["N"]?.value)
        assertEquals(13, asm.symbolTable["ZERO"]?.value)
        
        // Check resolved instructions
        assertEquals(12, asm.instructions[0].addr.value) // IN N
        assertEquals(13, asm.instructions[1].addr.value) // LOAD ZERO
        assertEquals(12, asm.instructions[2].addr.value) // COMP N
        assertEquals(9, asm.instructions[3].addr.value)  // JMPLT END
        assertEquals(11, asm.instructions[4].addr.value) // LOAD SUM
        assertEquals(12, asm.instructions[5].addr.value) // ADD N
        assertEquals(11, asm.instructions[6].addr.value) // STORE SUM
        assertEquals(12, asm.instructions[7].addr.value) // IN N
        assertEquals(1, asm.instructions[8].addr.value)  // JMP START
        assertEquals(11, asm.instructions[9].addr.value) // OUT SUM
        assertEquals(0, asm.instructions[10].addr.value) // HALT
    }
}
