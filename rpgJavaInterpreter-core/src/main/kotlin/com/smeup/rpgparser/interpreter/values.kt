package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import java.lang.Exception
import java.lang.RuntimeException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.streams.toList

const val PAD_CHAR = '\u0000'
const val PAD_STRING = PAD_CHAR.toString()

abstract class Value {
    open fun asInt(): IntValue = throw UnsupportedOperationException("${this.javaClass.simpleName} cannot be seen as an Int")
    open fun asDecimal(): DecimalValue = throw UnsupportedOperationException("${this.javaClass.simpleName} cannot be seen as an Decimal")
    open fun asString(): StringValue = throw UnsupportedOperationException()
    open fun asBoolean(): BooleanValue = throw UnsupportedOperationException()
    open fun asTimeStamp(): TimeStampValue = throw UnsupportedOperationException()
    abstract fun assignableTo(expectedType: Type): Boolean
    open fun takeLast(n: Int): Value = TODO("takeLast not yet implemented for ${this.javaClass.simpleName}")
    open fun takeFirst(n: Int): Value = TODO("takeFirst not yet implemented for ${this.javaClass.simpleName}")
    open fun concatenate(other: Value): Value = TODO("concatenate not yet implemented for ${this.javaClass.simpleName}")
    open fun asArray(): ArrayValue = throw UnsupportedOperationException()
    open fun render(): String = "Nope"
    abstract fun copy(): Value
    fun toArray(nElements: Int, elementType: Type): Value {
        val elements = LinkedList<Value>()
        for (i in 1..nElements) {
            elements.add(coerce(this.copy(), elementType))
        }
        return ConcreteArrayValue(elements, elementType)
    }
}

interface NumberValue {
    fun negate(): Value
    val bigDecimal: BigDecimal
}

data class StringValue(val value: String, val varying: Boolean = false) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return when (expectedType) {
            is StringType -> expectedType.length >= value.length.toLong()
            is DataStructureType -> expectedType.elementSize == value.length // Check for >= ???
            else -> false
        }
    }

    override fun takeLast(n: Int): Value {
        return StringValue(value.takeLast(n))
    }

    override fun takeFirst(n: Int): Value {
        return StringValue(value.take(n))
    }

    override fun concatenate(other: Value): Value {
        require(other is StringValue)
        return StringValue(value + other.value)
    }

    val valueWithoutPadding: String
        get() = value.removeNullChars()

    companion object {
        fun blank(length: Int) = StringValue(PAD_STRING.repeat(length))
        fun padded(value: String, size: Int) = StringValue(value.padEnd(size, PAD_CHAR))
    }

    override fun equals(other: Any?): Boolean {
        return if (other is StringValue) {
            this.valueWithoutPadding == other.valueWithoutPadding
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return valueWithoutPadding.hashCode()
    }

    override fun toString(): String {
        return "StringValue[${value.length}]($valueWithoutPadding)"
    }

    override fun asString() = this
    fun isBlank(): Boolean {
        return this.valueWithoutPadding.isBlank()
    }

    override fun render(): String {
        return valueWithoutPadding
    }

    override fun copy(): StringValue = this

    fun length(varying : Boolean = this.varying) : Int {
        if( varying ) {
            var len = 0
            value.forEach {
                if( it != PAD_CHAR )
                    len++
            }
            return len
        }
        return value.length
    }
}

fun String.removeNullChars(): String {
    val firstNullChar = this.chars().toList().indexOfFirst { it == 0 }
    return if (firstNullChar == -1) {
        this
    } else {
        this.substring(0, firstNullChar)
    }
}

data class IntValue(val value: Long) : NumberValue, Value() {

    private val internalValue = BigDecimal(value)

    override val bigDecimal: BigDecimal
        get() = BigDecimal(value)

    override fun negate(): Value = IntValue(-value)

    override fun assignableTo(expectedType: Type): Boolean {
        // TODO check decimals
        when (expectedType) {
            is NumberType -> return expectedType is NumberType
            is ArrayType -> {
                return expectedType.element is NumberType
            }
        }
        return false
    }

    override fun asInt() = this
    // TODO Verify conversion
    override fun asDecimal(): DecimalValue = DecimalValue(internalValue)

    fun increment() = IntValue(value + 1)

    override fun takeLast(n: Int): Value {
        return IntValue(lastDigits(value, n))
    }

    private fun lastDigits(n: Long, digits: Int): Long {
        return (n % Math.pow(10.0, digits.toDouble())).toLong()
    }

    private fun firstDigits(n: Long, digits: Int): Long {
        var localNr = n
        if (n < 0) {
            localNr = n * -1
        }
        val div = Math.pow(10.0, digits.toDouble()).toInt()
        while (localNr / div > 0) {
            localNr /= 10
        }
        return localNr * java.lang.Long.signum(n)
    }

    override fun takeFirst(n: Int): Value {
        return IntValue(firstDigits(value, n))
    }

    override fun concatenate(other: Value): Value {
        require(other is IntValue)
        return IntValue((value.toString() + other.value.toString()).toLong())
    }

    companion object {
        val ZERO = IntValue(0)

        fun sequenceOfNines(length: Int): IntValue {
            require(length >= 1)
            val ed = "9".repeat(length)
            return IntValue("$ed".toLong())
        }
    }

    override fun render(): String {
        return value.toString()
    }

    override fun copy(): IntValue = this
}

data class DecimalValue(val value: BigDecimal) : NumberValue, Value() {

    override val bigDecimal: BigDecimal
        get() = value

    override fun negate(): Value = DecimalValue(-value)

    override fun asInt(): IntValue {

        return IntValue(value.toLong())
    }

    override fun asDecimal(): DecimalValue = this

    override fun assignableTo(expectedType: Type): Boolean {
        // TODO check decimals
        when (expectedType) {
            is NumberType -> return true
            is ArrayType -> {
                return expectedType.element is NumberType
            }
        }
        return false
    }

    fun isPositive(): Boolean {
        return value.signum() >= 0
    }

    companion object {
        val ZERO = DecimalValue(BigDecimal.ZERO)
    }

    override fun render(): String {
        return value.toString()
    }

    override fun copy(): DecimalValue = this
}

data class BooleanValue(val value: Boolean) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is BooleanType
    }

    override fun asBoolean() = this

    override fun asString() = StringValue(if (value) "1" else "0")

    companion object {
        val FALSE = BooleanValue(false)
        val TRUE = BooleanValue(true)
    }
    override fun render(): String {
        return value.toString()
    }

    override fun copy(): BooleanValue = this
}

data class CharacterValue(val value: Array<Char>) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is CharacterType
    }

    override fun copy(): CharacterValue = this
}

data class TimeStampValue(val value: Date) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is TimeStampType
    }

    override fun asTimeStamp() = this

    companion object {
        val LOVAL = TimeStampValue(GregorianCalendar(0, Calendar.JANUARY, 0).time)
    }

    override fun copy(): TimeStampValue = this
}

abstract class ArrayValue : Value() {
    abstract fun arrayLength(): Int
    abstract fun elementSize(): Int
    fun totalSize() = elementSize() * arrayLength()
    abstract fun setElement(index: Int, value: Value)
    abstract fun getElement(index: Int): Value
    fun elements(): List<Value> {
        val elements = LinkedList<Value>()
        for (i in 0 until (arrayLength())) {
            elements.add(getElement(i + 1))
        }
        return elements
    }

    override fun asString(): StringValue {
        return StringValue(elements().map { it.asString() }.joinToString(""))
    }

    override fun assignableTo(expectedType: Type): Boolean {
        if (expectedType is DataStructureType) {
            // FIXME
            return true
        }
        if (expectedType is ArrayType) {
            return elements().all {
                it.assignableTo(expectedType.element)
            }
        }
        if (expectedType is StringType) {
            return expectedType.length >= arrayLength() * elementSize()
        }
        return false
    }
    override fun render(): String {
        return "Array(${elements().size})"
    }
    override fun asArray() = this

    fun areEquivalent(other: ArrayValue): Boolean {
        if (this.arrayLength() != other.arrayLength()) {
            return false
        }
        for (i in 1..this.arrayLength()) {
            if (this.getElement(i) != other.getElement(i)) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var res = this.arrayLength()
        if (this.arrayLength() > 0) {
            res *= 7 * this.getElement(1).hashCode()
            res *= 3 * this.getElement(this.arrayLength()).hashCode()
        }
        return res
    }

    abstract val elementType: Type

    override fun copy(): ArrayValue {
        return ConcreteArrayValue(this.elements().map { it.copy() }.toMutableList(), this.elementType)
    }
}

data class ConcreteArrayValue(val elements: MutableList<Value>, override val elementType: Type) : ArrayValue() {
    override fun elementSize() = elementType.size.toInt()

    override fun arrayLength() = elements.size

    override fun setElement(index: Int, value: Value) {
        require(index >= 1)
        require(index <= arrayLength())
        require(value.assignableTo(elementType))
        elements[index - 1] = value
    }

    override fun getElement(index: Int): Value {
        require(index >= 1) { "Indexes should be >=1. Index asked: $index" }
        require(index <= arrayLength())
        return elements[index - 1]
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ArrayValue) {
            this.areEquivalent(other)
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

object BlanksValue : Value() {
    override fun toString(): String {
        return "BlanksValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }

    override fun copy(): BlanksValue = this
}

object HiValValue : Value() {
    override fun toString(): String {
        return "HiValValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }

    override fun copy(): HiValValue = this
}
object LowValValue : Value() {
    override fun toString(): String {
        return "LowValValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }

    override fun copy(): LowValValue = this
}

/**
 * The container should always be a DS value
 */
class ProjectedArrayValue(
    val container: DataStructValue,
    val field: FieldDefinition,
    val startOffset: Int,
    val step: Long,
    val arrayLength: Int
) : ArrayValue() {
    override val elementType: Type
        get() = (this.field.type as ArrayType).element

    companion object {
        fun forData(containerValue: DataStructValue, data: FieldDefinition): ProjectedArrayValue {
            val stepSize = data.stepSize
            val arrayLength = data.declaredArrayInLine!!
            return ProjectedArrayValue(containerValue, data, data.startOffset, stepSize, arrayLength)
        }
    }

    override fun elementSize(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun arrayLength() = arrayLength

    override fun setElement(index: Int, value: Value) {
        require(index >= 1)
        require(index <= arrayLength())
        require(value.assignableTo((field.type as ArrayType).element)) { "Assigning to field $field incompatible value $value" }
        val startIndex = (this.startOffset + this.step * (index - 1)).toInt()
        val endIndex = (startIndex + this.field.elementSize()).toInt()
        container.setSubstring(startIndex, endIndex, coerce(value, StringType(this.field.elementSize())) as StringValue)
    }

    override fun getElement(index: Int): Value {
        require(index >= 1) { "Indexes should be >=1. Index asked: $index" }
        require(index <= arrayLength())

        val startIndex = (this.startOffset + this.step * (index - 1)).toInt()
        val endIndex = (startIndex + this.field.elementSize()).toInt()
        val substringValue = container.getSubstring(startIndex, endIndex)

        return coerce(substringValue, (this.field.type as ArrayType).element)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ArrayValue) {
            this.areEquivalent(other)
        } else {
            false
        }
    }
}

fun createArrayValue(elementType: Type, n: Int, creator: (Int) -> Value) = ConcreteArrayValue(Array(n, creator).toMutableList(), elementType)

fun blankString(length: Int) = StringValue(PAD_STRING.repeat(length))

fun Long.asValue() = IntValue(this)

fun String.asValue() = StringValue(this)

private const val FORMAT_DATE_ISO = "yyyy-MM-dd-HH.mm.ss.SSS"

fun String.asIsoDate(): Date {
    return SimpleDateFormat(FORMAT_DATE_ISO).parse(this.take(FORMAT_DATE_ISO.length))
}

fun Type.blank(dataDefinition: DataDefinition): Value {
    return when (this) {
        is ArrayType -> createArrayValue(this.element, this.nElements) {
            this.element.blank()
        }
        is DataStructureType -> {
            val ds = DataStructValue.blank(this.size.toInt())
            dataDefinition.fields.forEach {
                when (it.type) {
                    is NumberType -> when {
                        it.type.rpgType == RpgType.ZONED.rpgType || it.type.rpgType == RpgType.PACKED.rpgType -> {
                            var rnd = if (dataDefinition.inz)  BigDecimal.ZERO else BigDecimal.ONE.unaryMinus()
                            ds.set(it, DecimalValue(rnd))
                        }
                        it.type.rpgType == RpgType.BINARY.rpgType || it.type.rpgType == RpgType.INTEGER.rpgType || it.type.rpgType == RpgType.UNSIGNED.rpgType -> {
                            var rnd = if (dataDefinition.inz)  0 else (1..9).random()
                            ds.set(it, IntValue(rnd.toLong()))
                        }
                    }
                }
            }
            ds
        }
        is StringType -> StringValue.blank(this.size.toInt())
        is NumberType -> IntValue(0)
        is BooleanType -> BooleanValue(false)
        is TimeStampType -> TimeStampValue.LOVAL
        is KListType -> throw UnsupportedOperationException("Blank value not supported for KList")
        is CharacterType -> CharacterValue(Array(this.nChars) { ' ' })
        is HiValType -> TODO()
    }
}

// Deprecated?
fun Type.blank(): Value {
    return when (this) {
        is ArrayType -> createArrayValue(this.element, this.nElements) {
            this.element.blank()
        }
        is DataStructureType -> DataStructValue.blank(this.size.toInt())
        is StringType -> StringValue.blank(this.size.toInt())
        is NumberType -> IntValue(0)
        is BooleanType -> BooleanValue(false)
        is TimeStampType -> TimeStampValue.LOVAL
        is KListType -> throw UnsupportedOperationException("Blank value not supported for KList")
        is CharacterType -> CharacterValue(Array(this.nChars) { ' ' })
        is HiValType -> TODO()
    }
}

/**
 * StringValue wrapper
 */

data class DataStructValue(var value: String) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return when (expectedType) {
            // Check if the size of the value mathches the expected size within the DS
            is DataStructureType -> expectedType.elementSize == value.length
            is StringType -> expectedType.size == this.value.length.toLong()
            else -> false
        }
    }

    override fun copy(): DataStructValue = DataStructValue(value)

    /**
     * A DataStructure could also be an array of data structures. In that case the field is seen as
     * an array itself, so setting the field value requires an array value. In cases in which we
     * want to set a field of a single instance of the data structure we can use this method.
     */
    fun setSingleField(field: FieldDefinition, value: Value) {
        try {
            val v = (field.type as ArrayType).element.toDataStructureValue(value)
            val startIndex = field.startOffset
            val endIndex = field.startOffset + field.elementSize().toInt()
            try {
                this.setSubstring(startIndex, endIndex, v)
            } catch (e: Exception) {
                throw RuntimeException("Issue arose while setting field ${field.name}. Indexes: $startIndex to $endIndex. Field size: ${field.size}. Value: $value", e)
            }
        } catch (e: Throwable) {
            throw RuntimeException("Issue arose while setting field ${field.name}. Value: $value", e)
        }
    }

    fun set(field: FieldDefinition, value: Value) {
        val v = field.toDataStructureValue(value)
        val startIndex = field.startOffset
        val endIndex = field.startOffset + field.size.toInt()
        try {
            this.setSubstring(startIndex, endIndex, v)
        } catch (e: Exception) {
            throw RuntimeException("Issue arose while setting field ${field.name}. Indexes: $startIndex to $endIndex. Field size: ${field.size}. Value: $value", e)
        }
    }

    operator fun get(data: FieldDefinition): Value {
        return if (data.declaredArrayInLine != null) {
            ProjectedArrayValue.forData(this, data)
        } else {
            coerce(this.getSubstring(data.startOffset, data.endOffset), data.type)
        }
    }

    /**
     * See setSingleField
     */
    fun getSingleField(data: FieldDefinition): Value {
        require(data.type is ArrayType)
        return coerce(this.getSubstring(data.startOffset, data.endOffset), data.type.element)
    }

    val valueWithoutPadding: String
        get() = value.removeNullChars()

    fun setSubstring(startOffset: Int, endOffset: Int, substringValue: StringValue) {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        require(endOffset - startOffset == substringValue.value.length) { "Setting value $substringValue, with length ${substringValue.value.length}, into field of length ${endOffset - startOffset}" }
        val newValue = value.substring(0, startOffset) + substringValue.value + value.substring(endOffset)
        value = newValue // .replace('\u0000', ' ')
    }

    fun getSubstring(startOffset: Int, endOffset: Int): StringValue {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        val s = value.substring(startOffset, endOffset)
        return StringValue(s)
    }

    companion object {
        fun blank(length: Int) = DataStructValue(PAD_STRING.repeat(length))
    }

    override fun toString(): String {
        return "DataStructureValue[${value.length}]($valueWithoutPadding)"
    }

    override fun asString() = StringValue(this.value)

    fun isBlank(): Boolean {
        return this.valueWithoutPadding.isBlank()
    }
}
