package land.sungbin.markdown.runtime

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
internal inline fun runtimeCheck(condition: Boolean, message: () -> String) {
  contract { returns() implies condition }
  if (!condition) runtimeError(message)
}

internal inline fun runtimeError(message: () -> String): Nothing =
  error(
    "Problem with compose-markdown runtime. Please check the GitHub issue for the same issue, " +
      "or register a new one. (https://github.com/jisungbin/compose-markdown/issues)\n" +
      message()
  )
