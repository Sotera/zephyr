package org.zephyr.spark.streaming

import org.apache.hadoop.io.Text

class ParserFailResult(val value: Text) extends Result {
    override def outcome = new Text("parsefailure")
}
