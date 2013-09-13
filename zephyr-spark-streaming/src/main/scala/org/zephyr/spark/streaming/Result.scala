package org.zephyr.spark.streaming

import org.apache.hadoop.io.Text

trait Result {
  def value: Text
  def outcome: Text

}
