package com.smeup.rpgparser.interpreter

interface IMemorySliceStorage : AutoCloseable {

    /**
     * Open the storage
     * */
    fun open()

    /**
     * Load memory associated to memorySliceId
     * */
    fun load(memorySliceId: MemorySliceId): Map<String, Value>

    /**
     * Notify transaction start. Is called before all memory slices storing
     * */
    fun beginTrans()

    /**
     * Store map associated to memory slices
     * */
    fun store(memorySliceId: MemorySliceId, values: Map<String, Value>)

    /**
     * Called if all memory slices storing process is succesfully completed
     * */
    fun commitTrans()

    /**
     * Called in case of failure
     * */
    fun rollbackTrans()

    /**
     * Close the storage. If do not has been called commitTrans, it could be needed to implement rollback mechanisms
     * */
    override fun close()
}

/**
 * Memory slice identifier.
 * */
data class MemorySliceId(val activationGroup: String, val programName: String)

/**
 * Memory slice is that portion of program memory associated to the SymbolTable
 * */
data class MemorySlice(val memorySliceId: MemorySliceId, val symbolTable: ISymbolTable) {

    /**
     * Flag to indicate that memory slice has to be persisted.
     * This property is optional because, if for some reason is not set, the serializer will throw an exception as warranty
     * that something has been wrong, and it needs insights.
     * */
    var persist: Boolean? = null
}

/**
 * Create a memory slice manager
 * */
class MemorySliceMgr(private val storage: IMemorySliceStorage) {

    private var memorySlices = mutableMapOf<MemorySliceId, MemorySlice>()

    init {
        storage.open()
    }

    private fun getDataDefinition(name: String, symbolTable: ISymbolTable): AbstractDataDefinition? {
        return symbolTable.dataDefinitionByName(name)
    }

    private fun encodeDataDefinition(dataDefinition: AbstractDataDefinition): String {
        return dataDefinition.name
    }

    fun afterMainProgramInterpretation(ok: Boolean = true) {
        storage.use {
            if (ok) {
                store()
            }
        }
    }

    /**
     * Associate a symbol table to a memory slice.
     * At first time association, symbol table will be initialized by loading of persistent data through IMemorySliceStorage
     * */
    fun associate(memorySliceId: MemorySliceId, symbolTable: ISymbolTable): MemorySlice {
        return memorySlices.computeIfAbsent(memorySliceId) {
            storage.load(it).forEach() { nameToValue ->
                getDataDefinition(nameToValue.key, symbolTable).let { dataDef ->
                    symbolTable[dataDef!!] = nameToValue.value
                }
            }
            MemorySlice(memorySliceId = memorySliceId, symbolTable = symbolTable)
        }
    }

    /**
     * Store all memory slices.
     * @throws RuntimeException if something go wrong
     * */
    private fun store() {
        val slicesNotConfigured = memorySlices.values.filter {
            it.persist == null
        }
        if (slicesNotConfigured.isNotEmpty()) {
            throw RuntimeException("persist property not set for these slices: $slicesNotConfigured")
        }
        storage.beginTrans()
        memorySlices.values.forEach { slice ->
            val result = storage.runCatching {
                if (slice.persist!!) {
                    val values = slice.symbolTable.getValues().map {
                        encodeDataDefinition(it.key) to it.value
                    }.toMap()
                    storage.store(memorySliceId = slice.memorySliceId, values = values)
                }
            }
            if (result.isFailure) {
                storage.rollbackTrans()
                throw RuntimeException(result.exceptionOrNull())
            }
        }
        storage.commitTrans()
    }
}

class DummyMemorySliceStorage : IMemorySliceStorage {

    private val memory = mapOf<String, Value>()

    override fun open() {
    }

    override fun close() {
    }

    /**
     * Load memory associated to memorySliceId
     * */
    override fun load(memorySliceId: MemorySliceId): Map<String, Value> = memory

    override fun beginTrans() {
    }

    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
    }

    override fun commitTrans() {
    }

    override fun rollbackTrans() {
    }
}
