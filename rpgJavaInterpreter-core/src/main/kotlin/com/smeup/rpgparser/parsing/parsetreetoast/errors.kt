/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.ErrorEventSource
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.adaptInFunctionOf
import com.smeup.rpgparser.parsing.facade.relative
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import org.antlr.v4.runtime.ParserRuleContext

open class ParseTreeToAstError(message: String, cause: Throwable? = null) : IllegalStateException(message, cause) {
    constructor(message: String) : this(message = message, cause = null)
}

class AstResolutionError(message: String, cause: Throwable? = null) : ParseTreeToAstError(message, cause)

internal fun Throwable.fireErrorEvent(position: Position?): Throwable {
    /* Ignoring duplicate messages. This could be caused by a retry of construction of one data definition, failed at the first time. */
    if (!getAstCreationErrors().any { it.message.equals(this.message) }) {
        getAstCreationErrors().add(this)
        val programNameToCopyBlocks = getProgramNameToCopyBlocks()
        val sourceReference = position?.relative(programNameToCopyBlocks.first, programNameToCopyBlocks.second)?.second
        val errorEvent = ErrorEvent(
            error = this,
            errorEventSource = ErrorEventSource.Parser,
            absoluteLine = position?.start?.line,
            sourceReference = sourceReference
        )
        MainExecutionContext.getConfiguration().jarikoCallback.onError(errorEvent)
    }

    return this
}

internal fun notImplementOperationException(message: String): IllegalStateException {
    return ParseTreeToAstError("An operation is not implemented: $message")
}

internal fun getAstCreationErrors() = MainExecutionContext.getAttributes()
    .getOrPut("com.smeup.rpgparser.parsing.parsetreetoast.getAstCreationErrors") { mutableListOf<Throwable>() } as MutableList<Throwable>

/**
 * This function is used to throw an error with a specific message and cause.
 * It is a natural replacement for the throw keyword in order to link the error to the ast node.
 *
 * @param message The error message to be used. It is nullable and defaults to null.
 * @param cause The Throwable that caused the error. It is nullable and defaults to null.
 * @return Nothing. This function always throws an exception.
 * @throws AstResolutionError if the cause is an instance of AstResolutionError.
 * @throws IllegalStateException with the error message and cause.
 */
internal fun Node.error(message: String? = null, cause: Throwable? = null): Nothing {
    val position = this.position?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    if (cause != null && cause is AstResolutionError) {
        cause
    } else {
        AstResolutionError(
            message?.let { "$message at: $position" } ?: "Error at: $position",
            cause?.let { cause }
        )
    }.let { error ->
        if (this is CompilationUnit) {
            throw error
        } else {
            throw error.fireErrorEvent(this.position)
        }
    }
}

/**
 * This function is used to throw a not implemented operation exception with a specific message.
 * It is a natural replacement for the TODO inline function in order to link the error to the ast node.
 *
 * @param message The error message to be used. It is nullable and defaults to null.
 * @return Nothing. This function always throws an exception.
 * @throws IllegalStateException with the error message and the position where the error occurred.
 */
internal fun Node.todo(message: String? = null): Nothing {
    val pref = message?.let {
        "$message at "
    } ?: "Error at "
    val position = this.position?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    notImplementOperationException("${pref}Position: $position").let { error ->
        if (this is CompilationUnit) {
            throw error
        } else {
            throw error.fireErrorEvent(this.position)
        }
    }
}

/**
 * This function is used to check if a certain condition is met and throw an error if it's not.
 * It is a natural replacement for the "require" kotlin function in order to link the error to the ast node.
 *
 * @param value The condition to be checked. It is a boolean value.
 * @param lazyMessage A lambda function that generates the error message. It is invoked only if the condition is not met.
 * @throws IllegalStateException with the error message generated by the lazyMessage function if the condition is not met.
 */
internal fun Node.require(value: Boolean, lazyMessage: () -> String) {
    if (!value) {
        this.error(message = lazyMessage.invoke())
    }
}

/**
 * This function is used to throw a not implemented operation exception with a specific message.
 * It is a natural replacement for the TODO inline function in order to link the error to the parse tree node.
 *
 * @param message The error message to be used. It is nullable and defaults to null.
 * param conf The configuration to be used to consider the position. It is a ToAstConfiguration.
 */
internal fun ParserRuleContext.todo(message: String? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at"
    } ?: "$text at"
    val position = toPosition(conf.considerPosition)?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    val myMessage = "$pref $position ${this.javaClass.name}"
    throw notImplementOperationException(myMessage).fireErrorEvent(toPosition(conf.considerPosition))
}
/**
 * This function is used to throw an error with a specific message and cause.
 * It is a natural replacement for the throw keyword in order to link the error to the parse tree node.
 * @param message The error message to be used. It is nullable and defaults to null.
 * @param cause The Throwable that caused the error. It is nullable and defaults to null.
 * @param conf The configuration to be used to consider the position. It is a ToAstConfiguration.
 */
internal fun ParserRuleContext.error(message: String? = null, cause: Throwable? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at: "
    } ?: "$text at: "
    val position = toPosition(conf.considerPosition)?.adaptInFunctionOf(getProgramNameToCopyBlocks().second)
    throw if (cause != null && cause is ParseTreeToAstError) {
        cause
    } else {
        ParseTreeToAstError(
            "$pref$position ${this.javaClass.name}",
            cause
        )
    }.fireErrorEvent(toPosition(conf.considerPosition))
}

/**
 * This function is used to check if a certain condition is met and throw an error if it's not.
 * It is a natural replacement for the "require" kotlin function in order to link the error to parse tree node.
 *
 * @param value The condition to be checked. It is a boolean value.
 * @param lazyMessage A lambda function that generates the error message. It is invoked only if the condition is not met.
 * @param conf The configuration to be used to consider the position. It is a ToAstConfiguration.
 */
internal fun ParserRuleContext.require(value: Boolean, lazyMessage: () -> String, conf: ToAstConfiguration) {
    if (!value) {
        this.error(message = lazyMessage.invoke(), conf = conf)
    }
}

internal fun checkAstCreationErrors(phase: AstHandlingPhase) {
    if (getAstCreationErrors().isNotEmpty()) {
        if (MainExecutionContext.getConfiguration().options?.toAstConfiguration?.afterPhaseErrorContinue?.invoke(phase) != true) {
            throw getAstCreationErrors()[0]
        }
    }
}
