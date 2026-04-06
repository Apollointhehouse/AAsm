package dev.apollointhehouse.asm

sealed interface Address {
    data class Raw(val value: Int) : Address
    data class Named(val name: String) : Address
}