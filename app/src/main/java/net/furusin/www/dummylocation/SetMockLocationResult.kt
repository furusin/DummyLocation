package net.furusin.www.dummylocation

sealed interface SetMockLocationResult {
    object Success : SetMockLocationResult
    object Failure : SetMockLocationResult
}