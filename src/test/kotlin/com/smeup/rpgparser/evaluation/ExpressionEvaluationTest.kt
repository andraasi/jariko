package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.strumenta.kolasu.model.ReferenceByName
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionEvaluationTest {

    @Test
    fun evaluateStringLiteral() {
        assertEquals(StringValue("hello"), interpret(StringLiteral("hello")))
    }

    @Test
    fun evaluateIntLiteral() {
        assertEquals(IntValue(0), interpret(IntLiteral(0)))
        assertEquals(IntValue(20), interpret(IntLiteral(20)))
    }

    @Test
    fun evaluateDataRefExpr() {
        val dataDefinition = DataDefinition("Foo", DataType.SINGLE, 1)
        assertEquals(IntValue(11), interpret(dataRefTo(dataDefinition), mapOf(dataDefinition to IntValue(11))))
    }

    @Test
    fun evaluateEqualityExprTrueCase() {
        val dataDefinition = DataDefinition("Foo", DataType.SINGLE, 1)
        assertEquals(
                BooleanValue(true),
                interpret(EqualityExpr(IntLiteral(11), dataRefTo(dataDefinition)),
                mapOf(dataDefinition to IntValue(11))))
    }

    @Test
    fun evaluateEqualityExprFalseCase() {
        val dataDefinition = DataDefinition("Foo", DataType.SINGLE, 1)
        assertEquals(
                BooleanValue(false),
                interpret(EqualityExpr(IntLiteral(10), dataRefTo(dataDefinition)),
                        mapOf(dataDefinition to IntValue(11))))
    }

    private fun interpret(expr: Expression, initializations : Map<AbstractDataDefinition, Value> = mapOf()) : Value {
        val systemInterface = DummySystemInterface()
        val interpreter = Interpreter(systemInterface)
        initializations.forEach {
            interpreter[it.key] = it.value
        }
        return interpreter.interpret(expr)
    }

}
