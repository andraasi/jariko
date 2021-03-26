package com.smeup.rpgparser.interpreter

data class FunctionParam(val name: String, val type: Type)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value
}

abstract class JvmFunction(val name: String = "<UNNAMED>", val params: List<FunctionParam>) :
    Function {
    override fun params() = params
}

class RpgFunction : Function {
    override fun params(): List<FunctionParam> {
        TODO("Not yet implemented")
    }

    override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value {
        TODO("Not yet implemented")
    }
}