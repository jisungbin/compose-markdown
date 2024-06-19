package land.sungbin.markdown.runtime

import okio.Buffer
import okio.Source
import okio.Timeout

public data object EmptySource : Source {
  override fun read(sink: Buffer, byteCount: Long): Long = -1L
  override fun timeout(): Timeout = Timeout.NONE
  override fun close() {}
}
