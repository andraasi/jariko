package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice

class ProgramInterpreter(val systemInterface: SystemInterface) {

    private val dataWrapUpPolicy = HashMap<RpgProgram, DataWrapUpChoice>()
    private val interpreters = HashMap<RpgProgram, InternalInterpreter>()

    fun execute(rpgProgram: RpgProgram, initialValues: LinkedHashMap<String, Value>) {
        var firstCall = false
        val interpreter = interpreters.getOrPut(rpgProgram) {
            firstCall = true
            val ii = InternalInterpreter(systemInterface)
            ii.interpretationContext = object : InterpretationContext {
                override val currentProgramName: String
                    get() = rpgProgram.name
                override fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice) {
                    dataWrapUpPolicy[rpgProgram] = dataWrapUpChoice
                }
                override fun shouldReinitialize(): Boolean {
                    return if (rpgProgram in dataWrapUpPolicy) {
                        dataWrapUpPolicy[rpgProgram]!! == DataWrapUpChoice.LR
                    } else {
                        true
                    }
                }
                override val activationGroup: String?
                    get() {
                        TODO()
//                        val activationGroupType = rpgProgram.cu.directives.filterIsInstance<ActivationGroupDirective>().map {
//                            it as ActivationGroupDirective
//                        }[0]?.type
                    }
            }
            ii
        }
        interpreter.execute(rpgProgram.cu, initialValues, reinitialization = firstCall ||
            interpreter.interpretationContext.shouldReinitialize())
        initialValues.keys.forEach { initialValues[it] = interpreter[it] }
    }
}
