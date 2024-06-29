/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.unit

import androidx.compose.runtime.Immutable
import kotlin.math.roundToInt

@Immutable
public sealed interface Size {
  public val width: String
  public val height: String

  public operator fun component1(): String = width
  public operator fun component2(): String = height
}

public class FixedSize(width: Int, height: Int) : Size {
  public override val width: String = width.toString()
  public override val height: String = height.toString()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as FixedSize

    if (width != other.width) return false
    if (height != other.height) return false

    return true
  }

  override fun hashCode(): Int {
    var result = width.hashCode()
    result = 31 * result + height.hashCode()
    return result
  }

  override fun toString(): String = "FixedSize(width='$width', height='$height')"
}

public class Percentage(width: Float, height: Float) : Size {
  public override val width: String = "${width.roundToInt()}%"
  public override val height: String = "${height.roundToInt()}%"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Percentage

    if (width != other.width) return false
    if (height != other.height) return false

    return true
  }

  override fun hashCode(): Int {
    var result = width.hashCode()
    result = 31 * result + height.hashCode()
    return result
  }

  override fun toString(): String = "PercentSize(width='$width', height='$height')"
}
