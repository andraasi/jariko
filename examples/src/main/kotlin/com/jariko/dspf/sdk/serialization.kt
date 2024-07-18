package com.jariko.dspf.sdk

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