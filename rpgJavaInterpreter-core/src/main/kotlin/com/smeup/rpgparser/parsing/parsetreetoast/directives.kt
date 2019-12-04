package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.interpreter.BaseCompileTimeInterpreter
import com.smeup.rpgparser.parsing.ast.DeceditDirective
import com.smeup.rpgparser.parsing.ast.Directive
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.ast.StringLiteral
import com.strumenta.kolasu.mapping.toPosition

fun RpgParser.Hspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Directive {
    if (this.content is RpgParser.SetDeceditContext) {
        return (this.content as RpgParser.SetDeceditContext).hs_decedit_set().toAst(conf)
    } else {
        TODO("Unexpected ${this.content} in decedit directive")
    }
}

fun RpgParser.Hs_parmContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    when {
        this.hs_string() != null -> {
            val content = this.hs_string().content.map { it.text }.joinToString(separator = "")
            return StringLiteral(content, position = this.toPosition(conf.considerPosition))
        }
        else -> TODO("Invalid param in decedit directive")
    }
}

fun RpgParser.Hs_decedit_setContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Directive {
    val format = BaseCompileTimeInterpreter().evaluate(this.rContext(), this.hs_parm().toAst(conf)).asString().value
    return DeceditDirective(format, position = this.toPosition(conf.considerPosition))
}
