package org.zephyr.spark.streaming

import org.apache.hadoop.io.Text

class MapperFailResult(val value: Text) extends Result {
    override def outcome = new Text("mapperfailure")
}
