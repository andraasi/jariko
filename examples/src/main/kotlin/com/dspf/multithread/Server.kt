package com.dspf.multithread

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

private fun onExfmt(fields: List<DSPFField>, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
    while (true) {
        try {
            return OnExfmtResponse(runtimeInterpreterSnapshot, emptyMap())
        } catch (e: Exception) {
            continue
        }
    }
}

fun main(args: Array<String>) {
    val (program, configuration) = setup(args, ::onExfmt)
//    program.singleCall(emptyList(), configuration)
}