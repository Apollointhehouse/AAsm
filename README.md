# AAsm

A modified implementation of the textbook architecture introduced in CS 110 at the University of Auckland. This project provides a simple assembler and emulator for a 16-bit architecture.

## Architecture Overview

The architecture uses 16-bit instructions:
- **OpCode**: 4 bits
- **Address**: 12 bits (supporting 4096 addresses)

### Registers
- **Accumulator (R)**: Primary data register.
- **Logic Register (C)**: Stores comparison results for branching.
- **Program Counter (PC)**: Points to the next instruction in memory.

## Instruction Set

| Name  | OpCode | Description                                       |
|-------|--------|---------------------------------------------------|
| LOAD  | 0x0000 | Load memory value at address into R               |
| STORE | 0x1000 | Store R value into memory at address              |
| CLEAR | 0x2000 | Store 0 into memory at address                    |
| ADD   | 0x3000 | R = R + value at address                          |
| INC   | 0x4000 | Increment value in R and store at address         |
| SUB   | 0x5000 | R = R - value at address                          |
| DEC   | 0x6000 | Decrement value in R and store at  address        |
| COMP  | 0x7000 | Compare R with value at address (sets C register) |
| JMP   | 0x8000 | Jump to address                                   |
| JMPGT | 0x9000 | Jump if value >  R (from last COMP)               |
| JMPEQ | 0xA000 | Jump if value == R (from last COMP)               |
| JMPLT | 0xB000 | Jump if value <  R (from last COMP)               |
| JMPNE | 0xC000 | Jump if value != R (from last COMP)               |
| IN    | 0xD000 | Read integer from stdin into address              |
| OUT   | 0xE000 | Print value at address to stdout                  |
| HALT  | 0xF000 | Stop execution                                    |

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

## Usage

### Assembling an `.aaexe` file

To assemble one or more `.aasm` files into a single `.aaexe` executable:

```powershell
./gradlew run --args="ASSEMBLE <output_path> <aasm_file1> <aasm_file2> ..."
```

- **`<output_path>`**: The path to the output `.aaexe` file (the extension will be added automatically).
- **`<aasm_file>`**: The names of the source `.aasm` files, located in the same directory as the output path.

Example:
```powershell
./gradlew run --args="ASSEMBLE src/main/resources/output.aaexe fib.aasm link_fib_stack.aasm stack.aasm"
```

### Executing an `.aaexe` file

To run an assembled `.aaexe` file:

```powershell
./gradlew run --args="EXECUTE <aaexe_path>"
```

Example:
```powershell
./gradlew run --args="EXECUTE src/main/resources/output.aaexe"
```