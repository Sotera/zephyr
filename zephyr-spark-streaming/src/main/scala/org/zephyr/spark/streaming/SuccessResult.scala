package org.zephyr.spark.streaming

import org.apache.hadoop.io.Text

class SuccessResult(val value: Text) extends Result {
    override def outcome = new Text("success")
}
