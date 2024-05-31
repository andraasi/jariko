package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.DTKBS
import com.smeup.dspfparser.positionals.FieldType
import com.smeup.dspfparser.positionals.Reference
import com.smeup.dspfparser.positionals.Reserved
import com.smeup.dspfparser.positionals.TypeOfName
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFLine private constructor(
    val count: Int,
    val sequenceNumber: String,
    val a: Char,
    val conditions: DSPFConditionsGroup,
    val reserved: Reserved,
    val typeOfName: TypeOfName,
    val fieldName: String,
    val reference: Reference,
    val length: Int? = null,
    val dataTypeKeyboardShift: DTKBS,
    val decimalsPositions: Int? = null,
    val fieldType: FieldType,
    val y: Int? = null,
    val x: Int? = null,
    val keywords: DSPFKeywordsGroup,
) {
    companion object {
        fun from(lineSubstrings: DSPFLineSubstrings): DSPFLine {
            return DSPFLine(
                this.getCount(lineSubstrings),
                this.getSequenceNumber(lineSubstrings),
                this.getA(lineSubstrings),
                this.getCondition(lineSubstrings),
                this.getReserved(lineSubstrings),
                this.getTypeOfName(lineSubstrings),
                this.getFieldName(lineSubstrings),
                this.getReference(lineSubstrings),
                this.getLength(lineSubstrings),
                this.getDataTypeKeyboardShift(lineSubstrings),
                this.getDecimalsPositions(lineSubstrings),
                this.getFieldType(lineSubstrings),
                this.getY(lineSubstrings),
                this.getX(lineSubstrings),
                this.getKeywords(lineSubstrings),
            )
        }

        private fun getCount(lineSubstrings: DSPFLineSubstrings): Int {
            return lineSubstrings.count
        }

        private fun getSequenceNumber(lineSubstrings: DSPFLineSubstrings): String {
            return lineSubstrings.sequenceNumber.trim()
        }

        private fun getA(lineSubstrings: DSPFLineSubstrings): Char {
            return lineSubstrings.a[0]
        }

        private fun getCondition(lineSubstrings: DSPFLineSubstrings): DSPFConditionsGroup {
            // has singular name because in one line substrings there is only one condition
            return DSPFConditionsGroup.fromString(lineSubstrings.condition)
        }

        private fun getReserved(lineSubstrings: DSPFLineSubstrings): Reserved {
            return Reserved.values().first { it.value == lineSubstrings.reserved[0] }
        }

        private fun getTypeOfName(lineSubstrings: DSPFLineSubstrings): TypeOfName {
            return TypeOfName.entries.first { it.value == lineSubstrings.typeOfName[0] }
        }

        private fun getFieldName(lineSubstrings: DSPFLineSubstrings): String {
            return lineSubstrings.fieldName.trim()
        }

        private fun getReference(lineSubstrings: DSPFLineSubstrings): Reference {
            return Reference.entries.first { it.value == lineSubstrings.reference[0] }
        }

        private fun getLength(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.length.trim().isBlank()) return null
            return lineSubstrings.length.trim().toInt()
        }

        private fun getDataTypeKeyboardShift(lineSubstrings: DSPFLineSubstrings): DTKBS {
            return DTKBS.entries.first { it.value == lineSubstrings.dataTypeKeyboardShift[0] }
        }

        private fun getDecimalsPositions(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.decimalsPositions.trim().isBlank()) return null
            return lineSubstrings.decimalsPositions.trim().toInt()
        }

        private fun getFieldType(lineSubstrings: DSPFLineSubstrings): FieldType {
            return FieldType.entries.first { it.value == lineSubstrings.fieldType[0] }
        }

        private fun getY(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.y.trim().isBlank()) return null
            return lineSubstrings.y.trim().toInt()
        }

        private fun getX(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.x.trim().isBlank()) return null
            return lineSubstrings.x.trim().toInt()
        }

        private fun getKeywords(lineSubstrings: DSPFLineSubstrings): DSPFKeywordsGroup {
            return DSPFKeywordsGroup.fromString(lineSubstrings.keywords)
        }
    }

    fun isHelp(): Boolean {
        return this.typeOfName == TypeOfName.H && this.fieldName.isBlank()
    }

    fun isRecord(): Boolean {
        return this.typeOfName == TypeOfName.R && this.fieldName.isNotBlank()
    }

    fun isField(): Boolean {
        return this.typeOfName == TypeOfName.BLANK && this.fieldName.isNotBlank()
    }

    fun isNone(): Boolean {
        return !(this.isHelp() || this.isRecord() || this.isField())
    }

    fun isInput(): Boolean {
        return this.fieldType == FieldType.I
    }

    fun isOutput(): Boolean {
        return this.fieldType == FieldType.O || this.fieldType == FieldType.BLANK
    }

    fun isInputOutput(): Boolean {
        return this.fieldType == FieldType.B
    }
}
