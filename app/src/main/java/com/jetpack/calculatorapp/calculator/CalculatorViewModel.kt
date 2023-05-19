package com.jetpack.calculatorapp.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            CalculatorAction.Decimal -> enterDecimal()
            CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            CalculatorAction.Calculate -> performCalculation()
            CalculatorAction.Delete -> performDeletion()
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(number2 = state.number2.dropLast(1))
            state.operation != null -> state = state.copy(operation = null)
            state.number1.isNotBlank() -> state = state.copy(number1 = state.number1.dropLast(1))
        }
    }

    private fun performCalculation() {
        if (state.number2.isNotBlank()) {
            val firstNumber = state.number1.toDouble()
            val secondNumber = state.number2.toDouble()
            val result = when (state.operation) {
                CalculatorOperation.Add -> firstNumber + secondNumber
                CalculatorOperation.Divide -> firstNumber / secondNumber
                CalculatorOperation.Multiply -> firstNumber * secondNumber
                CalculatorOperation.Subtract -> firstNumber - secondNumber
                null -> 0.0
            }

            state = CalculatorState(number1 = "$result")
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        when {
            state.number2.isNotBlank() && !state.number2.contains(".") -> state =
                state.copy(number2 = "${state.number2}.")
            state.number1.isNotBlank() && !state.number2.contains(".") && state.operation != null -> state =
                state.copy(number1 = "${state.number1}.")
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation != null && state.number2.length < MAX_NUM_LENGTH) {
            state = state.copy(number2 = state.number2 + number)
        } else if (state.operation == null && state.number1.length < MAX_NUM_LENGTH) {
            state = state.copy(number1 = state.number1 + number)
        }
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}