package org.zephyr.spark.streaming

import org.zephyr.schema.Schema
import org.zephyr.output.formatter.OutputFormatter
import org.zephyr.parser.{Parser, ParserFactory}
import spark.streaming.{Duration, StreamingContext}
import java.io.ByteArrayInputStream
import org.zephyr.data.{Record, ProcessingResult}
import org.apache.hadoop.io.Text
import org.zephyr.data.Pair
import org.zephyr.enricher.Enricher
import scala.collection.JavaConversions._


class ZephyrStreamer(val clusterUrl: String = "local", val username: String, val password: String, val duration: Long = 5000, val parser: ParserFactory,
                     val schema: Schema, val formatter: OutputFormatter, val enrichers: java.util.List[Enricher]) {
  def run() {
    val streamingContext = new StreamingContext(clusterUrl, "Zephyr Streaming", new Duration(duration))
    streamingContext
      .twitterStream(username, password)
      .map(_.getText.getBytes)
      .map(new ByteArrayInputStream(_))
      .map(parser.newParser(_))
      .map(execute(_, schema, formatter, enrichers))
      .saveAsTextFiles("twitter")
  }

  private def execute(parser: Parser, schema: Schema, formatter: OutputFormatter, enrichers: java.util.List[Enricher]) = {
    val results = Iterator.continually(parser.parse()).takeWhile(_ != null)
    results.foreach( result =>
      if (result.wasProcessedSuccessfully)
        processResult(result, schema, formatter, enrichers)
      else
        parseFailureResult(result)
    )
  }

  private def processResult(result: ProcessingResult[java.util.List[Pair], Array[Byte]], schema: Schema, formatter: OutputFormatter, enrichers: java.util.List[Enricher]) : Result = {
    val mappedRecord = schema.map(result.getProcessedData())
    if (mappedRecord.wasProcessedSuccessfully)
      successResult(mappedRecord, schema, formatter, enrichers: java.util.List[Enricher])
    else
      mappedFailureResult(mappedRecord, schema, formatter)
  }

  private def successResult(mappedRecord: ProcessingResult[Record, java.util.List[Pair]], schema: Schema, formatter: OutputFormatter, enrichers: java.util.List[Enricher]): Result = {
    val output = new Text()
    val record = mappedRecord.getProcessedData()

    for (enricher <- enrichers) {
      enricher.enrich(record)
    }
    output.set(formatter.formatRecord(record))

    new SuccessResult(output)
  }

  private def mappedFailureResult(mappedRecord: ProcessingResult[Record, java.util.List[Pair]], schema: Schema, formatter: OutputFormatter): Result = {
    val output = new Text()
    output.set(getPairsAsBytes(mappedRecord.getRawData()))

    new MapperFailResult(output)
  }

  private def getPairsAsBytes(pairs: java.util.List[Pair]): String = {
     val builder = new StringBuilder()
     for (pair <- pairs) {
       builder.append(pair.getKey())
       builder.append(":")
       builder.append(pair.getValue())
       builder.append(",")
     }

    builder.toString()
  }

  private def parseFailureResult(result: ProcessingResult[java.util.List[Pair], Array[Byte]]): Result = {
    val output = new Text()
    output.set(result.getRawData())

    new ParserFailResult(output)
  }
}