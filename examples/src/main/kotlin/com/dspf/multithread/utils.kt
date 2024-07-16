package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFValue
import com.smeup.rpgparser.interpreter.BooleanValue
import com.smeup.rpgparser.interpreter.CharacterValue
import com.smeup.rpgparser.interpreter.ConcreteArrayValue
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.OccurableDataStructValue
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.TimeStampValue
import com.smeup.rpgparser.interpreter.UnlimitedStringValue
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.serialization.BigDecimalSerializer
import com.smeup.rpgparser.serialization.LocalDateTimeSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.net.Socket

// using 'use' will also close the socket; use it wisely

// saving readers and buffered is mandatory
// because function creates a new one each time

private const val EOB = "EOB"

fun receive(socket: Socket): String {
    println("receiving...")
    val bufferedReader = socket.getInputStream().bufferedReader()
    val string = StringBuilder()
    var line: String?

    // allows JSON to be collected and parsed correctly
    while (bufferedReader.readLine().also { line = it } != null) {
        if (line == EOB) {
            break
        }
        string.append(line)
    }

    println("received ${string.length} B")
    return string.toString()
}

fun send(socket: Socket, string: String) {
    println("sending...")
    val bufferedWriter = socket.getOutputStream().bufferedWriter()
    bufferedWriter.write(string)
    bufferedWriter.newLine()
    bufferedWriter.write(EOB)
    bufferedWriter.newLine()
    bufferedWriter.flush()
    println("sent ${string.length} B")
}

// serialization

private val module = SerializersModule {
    contextual(BigDecimalSerializer)
    contextual(LocalDateTimeSerializer)
    polymorphic(DSPFValue::class) {
        subclass(IntValue::class)
        subclass(DecimalValue::class)
        subclass(StringValue::class)
        subclass(BooleanValue::class)
        subclass(TimeStampValue::class)
        subclass(CharacterValue::class)
        subclass(ConcreteArrayValue::class)
        subclass(DataStructValue::class)
        subclass(OccurableDataStructValue::class)
        subclass(UnlimitedStringValue::class)
    }
    polymorphic(Value::class) {
        subclass(IntValue::class)
        subclass(DecimalValue::class)
        subclass(StringValue::class)
        subclass(BooleanValue::class)
        subclass(TimeStampValue::class)
        subclass(CharacterValue::class)
        subclass(ConcreteArrayValue::class)
        subclass(DataStructValue::class)
        subclass(OccurableDataStructValue::class)
        subclass(UnlimitedStringValue::class)
    }
}

val json = Json {
    prettyPrint = true
    serializersModule = module
    classDiscriminator = "json-type"
}

