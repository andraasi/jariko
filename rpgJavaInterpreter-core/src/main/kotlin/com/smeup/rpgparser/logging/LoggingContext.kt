package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

class LoggingContext {
    private val timeUsageByStatement: HashMap<String, UsageMeasurement> = hashMapOf()
    private val symbolTableTimeUsage: EnumMap<SymbolTableAction, UsageMeasurement> = EnumMap(SymbolTableAction::class.java)
    private var renderingTimeMeasurement = UsageMeasurement.new()
    private var expressionTimeMeasurement = UsageMeasurement.new()

    private val initTimestamp = System.nanoTime()
    private val percentageFormatter = DecimalFormat("##%")

    private val totalTime
        get() = (System.nanoTime() - initTimestamp).nanoseconds

    enum class SymbolTableAction {
        INIT,
        LOAD,
        STORE
    }

    data class UsageMeasurement(val duration: Duration, val hit: Long) {
        companion object {
            fun new(): UsageMeasurement = UsageMeasurement(
                duration = Duration.ZERO,
                hit = 0
            )
        }
    }

    fun recordRenderingDuration(executionTime: Duration) {
        renderingTimeMeasurement = UsageMeasurement(
            duration = renderingTimeMeasurement.duration + executionTime,
            hit = renderingTimeMeasurement.hit + 1
        )
    }

    fun recordExpressionDuration(executionTime: Duration) {
        expressionTimeMeasurement = UsageMeasurement(
            duration = renderingTimeMeasurement.duration + executionTime,
            hit = renderingTimeMeasurement.hit + 1
        )
    }

    fun recordSymbolTableDuration(action: SymbolTableAction, executionTime: Duration) {
        val entry = symbolTableTimeUsage.getOrPut(action) { UsageMeasurement.new() }
        symbolTableTimeUsage[action] = UsageMeasurement(
            duration = entry.duration + executionTime,
            hit = entry.hit + 1
        )
    }

    fun recordStatementDuration(name: String, executionTime: Duration) {
        val entry = timeUsageByStatement.getOrPut(name) { UsageMeasurement.new() }
        timeUsageByStatement[name] = UsageMeasurement(
            duration = entry.duration + executionTime,
            hit = entry.hit + 1
        )
    }

    fun generateTimeUsageByStatementReportEntries(source: LogSourceData): List<LazyLogEntry> {
        return timeUsageByStatement.toList().map {
            val statementName = it.first
            val duration = it.second.duration
            val timePercentage = percentageFormatter.format(duration / totalTime)
            val hit = it.second.hit
            val average = (duration.inWholeNanoseconds / hit).nanoseconds

            val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "STMT TIME")
            LazyLogEntry(entry) { sep ->
                "$statementName$sep${duration.toString(DurationUnit.MICROSECONDS)}$sep$timePercentage${sep}$hit${sep}avg. ${average.toString(
                    DurationUnit.MICROSECONDS)}"
            }
        }
    }

    fun generateSymbolTableTimeUsageReportEntries(source: LogSourceData): List<LazyLogEntry> {
        return symbolTableTimeUsage.toList().map {
            val action = it.first
            val duration = it.second.duration
            val timePercentage = percentageFormatter.format(duration / totalTime)
            val hit = it.second.hit
            val average = (duration.inWholeNanoseconds / hit).nanoseconds

            val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "SYMTBL TIME")
            LazyLogEntry(entry) { sep ->
                "${action.name}$sep${duration.toString(DurationUnit.MICROSECONDS)}$sep$timePercentage${sep}$hit${sep}avg. ${average.toString(
                    DurationUnit.MICROSECONDS)}"
            }
        }
    }

    fun generateExpressionReportEntry(source: LogSourceData): LazyLogEntry {
        val duration = expressionTimeMeasurement.duration
        val timePercentage = percentageFormatter.format(duration / totalTime)
        val hit = expressionTimeMeasurement.hit
        val average = (duration.inWholeNanoseconds / hit).nanoseconds

        val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "EXPR TIME")
        return LazyLogEntry(entry) { sep ->
            "${duration.toString(DurationUnit.MICROSECONDS)}$sep$timePercentage$sep$hit${sep}avg. ${average.toString(
                DurationUnit.MICROSECONDS)}"
        }
    }

    fun generateLogTimeReportEntry(source: LogSourceData): LazyLogEntry {
        val duration = renderingTimeMeasurement.duration
        val timePercentage = percentageFormatter.format(duration / totalTime)
        val hit = renderingTimeMeasurement.hit
        val average = (duration.inWholeNanoseconds / hit).nanoseconds

        val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "LOG TIME")
        return LazyLogEntry(entry) { sep ->
            "${duration.toString(DurationUnit.MICROSECONDS)}$sep$timePercentage$sep$hit${sep}avg. ${average.toString(
                DurationUnit.MICROSECONDS)}"
        }
    }
}