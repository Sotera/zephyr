Zephyr - ETL at Scale
========================

Current Status
-----------------------
Initial Release 0.1.0!
 - Storm and Spark Streaming are going to go in their own branches and not be included in this release - checkout branch storm-dev and (eventually) spark-streaming-dev for a peek at these!

Why?
------------------------
ETL - Extract, Transform, and Load - has been around for decades.  The concept of taking data in one form, 
extracting the important bits, transforming them into data you actually want to use, and loading it into some
destination is hardly virgin ground.  However, for the last few years, over innumerable projects, I've seen
so many one-off, rigid implementations of ETL - often laden with bugs, poor performance, and rarely scaleable.

The problem is a pure engineering problem.  What is the most efficient way to get data from source, to destination,
when we're dealing with gigabytes or terabytes of data?  Further, what if our pipeline changes - from batch, to 
streaming -- or streaming, to batch?

Rather than watch everyone recreate the wheel in a very specific context, and then recreate it again when 
business needs or platforms change - I wrote Zephyr, an API, an abstraction, for ETL at BigData scale.

Big Data Implementations
------------------------
Currently, Zephyr supports the following implementations (as in, your code, when written to the Zephyr API, 
will not need to be recompiled to work with):
* <a href="http://hadoop.apache.org">Hadoop MapReduce</a> (<a href="http://www.cloudera.com">CDH 4.3.0</a>)
* <a href="http://storm-project.net">Storm</a>

We also fully plan to support <a href="http://spark-project.org">Spark Streaming</a> - and much of our 
work for Storm (in  terms of initially loading the data into the platform, and writing the data to our 
destination) will help us implement this in a fairly short period of time.

General Flow
-------------------------
By and large, each of our targeted scaleable implementations has its own way (external to Zephyr) for best
getting data into their system.  Whether it is reading from some sort of InputFormat in MapReduce, or a
specific queue implementation (Kafka,JMS) -- or even just a socket connection.  These are all considered to be
outside the scope of Zephyr.  Far be it from us to tell you how your data gets to your BigData processing
implementation.

However, once it is available to MapReduce/Storm - Zephyr takes over.  It provides a series of contracts to 
be fulfilled by your implementations.  In order, they are:
* Preprocessing (optional)
* Parsing
* Schema Mapping (including naming, validating, and normalizing)
* Enrichment (0..N, in order, no control flow)

You may have noticed there is no output phase; that, like our input phase, is up to you.  For our MapReduce
implementation, we generally allow our Mapper to write the data out to HDFS (with no Reduce phase), and we
are done.  Storm or Spark Streaming, on the other hand, would have their own ways of persisting your 
extracted and transformed data (such as an HBase writer).

Most of our implementations provide helper methods for these Input and Output phases.  The important piece of
Zephyr - Zephyr-Core - is really the most vital part of the process.  We anticipate that, for some, the
MapReduce implementation may fall short; whether it's via assumption of data coming in as either Text or
BytesWritable, or that we want to write it to HDFS - and Zephyr has been written such that extension of
our classes - or superceding them entirely (and using only Zephyr-Core) is both expected and embraced.

Our goal for Zephyr and each of the BigData implementations is that it will accomodate 90% of ETL needs - 
or at least for structured data (free text or imagery/video ingestion are an entirely different beast - though 
either might still be well-served by utilizing Zephyr).

Using Zephyr in your Ingestion Product
--------------------------
Requirements:
* JDK 6+ (you can use Java, Scala, Groovy, Clojure)
* Gradle 1.5+

Zephyr will be built and published (as a jar) for our first release; at that point, you will merely need 
to include the gav org.zephyr:zephyr-<bigdata-implementation>:<version> - and that will include zephyr-core.

You may also want to include from contrib - helper classes such as a Secure Event (for real-time analytics with
visibility concerns), or a (extremely) simplistic HBase Outputter, will also be available for inclusion.

More importantly will be our Sample project, which will exist as source code and structure - this will offer 
you a great example for building and running a Zephyr ETL job.  We invite you to download this project 
<here>.  We utilize gradle for our build and distribution creating scripts, and have included some run files.

You are welcome to extend or enhance these - or supercede them entirely.  If you feel that your solution
is superior to the one bundled - please fork and submit a pull request!

Other Resources
--------------------------
- <a href="https://github.com/Sotera/zephyr-sample-project">Zephyr Sample Project(s)</a> - A sample project structure that shows off how you might use Zephyr
- <a href="https://github.com/Sotera/zephyr-contrib">Zephyr Contrib</a> - for projects that don't really belong in Zephyr, but can be used with Zephyr to add other capabilities (like Accumulo visibility controls)
