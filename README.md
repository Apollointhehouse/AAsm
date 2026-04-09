# AAsm

A modified implementation of the textbook architecture introduced in CS 110 at the University of Auckland. This project provides a simple assembler and emulator for a 16-bit architecture.

## Architecture Overview

The architecture uses 16-bit words:
- **OpCode**: 4 bits (high-order)
- **Address**: 12 bits (low-order, supporting up to 4096 addresses)

### Registers
- **Accumulator (R)**: Primary data register.
- **Control Register (C)**: Stores comparison results for branching.
- **Program Counter (PC)**: Points to the next instruction in memory.

## Instruction Set

| Mnemonic | OpCode | Description                                       |
|----------|--------|---------------------------------------------------|
| LOAD     | 0x0000 | Load memory value at address into AC              |
| STORE    | 0x1000 | Store R value into memory at address              |
| CLEAR    | 0x2000 | Store 0 into memory at address                    |
| ADD      | 0x3000 | R = R + value at address                          |
| INC      | 0x4000 | Increment value at address                        |
| SUB      | 0x5000 | R = R - value at address                          |
| DEC      | 0x6000 | Decrement value at address                        |
| COMP     | 0x7000 | Compare R with value at address (sets C register) |
| JMP      | 0x8000 | Jump to address                                   |
| JMPGT    | 0x9000 | Jump if R > value (from last COMP)                |
| JMPEQ    | 0xA000 | Jump if R == value (from last COMP)               |
| JMPLT    | 0xB000 | Jump if R < value (from last COMP)                |
| JMPNE    | 0xC000 | Jump if R != value (from last COMP)               |
| IN       | 0xD000 | Read integer from stdin into address              |
| OUT      | 0xE000 | Print value at address to stdout                  |
| HALT     | 0xF000 | Stop execution                                    |

## Example Program (Fibonacci)

```
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

MAX:    .DATA 0
TEMP:   .DATA 0
A:      .DATA 0
B:      .DATA 1
```

## Running the Project

1.  **Build**: `./gradlew build`
2.  **Run**: `./gradlew run`
3.  **Test**: `./gradlew test`
