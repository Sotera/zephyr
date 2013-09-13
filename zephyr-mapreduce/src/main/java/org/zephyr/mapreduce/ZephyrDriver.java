package org.zephyr.mapreduce;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zephyr.util.UUIDHelper;

public class ZephyrDriver extends Configured implements Tool {

    public static final String DEFAULT_CACHE_FOLDER_NAME = "distributed-cache";
    private static HelpFormatter helpFormatter = new HelpFormatter();

    private ZephyrMRv1Configuration config;
    private String jobConfigFile;

    public ZephyrDriver(ZephyrMRv1Configuration config, String jobConfigFile) {
        this.config = config;
        this.jobConfigFile = jobConfigFile;
    }

    @Override
    public int run(String[] args) throws Exception {
        JobConf job = new JobConf(super.getConf(), ZephyrDriver.class);

        job.setJobName(config.getJobName());

        Path in = new Path(config.getInputPath());
        Path out = new Path(config.getOutputPath());

        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

        job.set("zephyr.job.uuid", UUIDHelper.generateUUID());

        job.setInputFormat(config.getInputFormat().getClass());
        job.setMapperClass(config.getMapper().getClass());

        job.setOutputFormat(ZephyrOutputFormat.class);

        for (Map.Entry<String, String> entry : config.getConfigMap().entrySet()) {
            job.set(entry.getKey(), entry.getValue());
        }

        job.set("zephyr.feed.xml", this.jobConfigFile);

        job.setNumReduceTasks(0);

        JobClient.runJob(job);

        return 0;
    }

    private static String[] buildArgsFromPath(File libPath) {
        String[] virtualArgs = new String[2];

        Collection<File> libs = FileUtils.listFiles(libPath, new String[]{"jar"}, true);

        if (libs.size() > 0) {
            virtualArgs[0] = "-libjars";
            virtualArgs[1] = buildCommaSeparatedPath(libs);
        }

        return virtualArgs;

    }

    private static String buildCommaSeparatedPath(Collection<File> paths) {
        StringBuilder pathList = new StringBuilder();
        for (File file : paths) {
            pathList.append(file.getAbsolutePath());
            pathList.append(",");
        }
        return pathList.substring(0, pathList.length() - 1);
    }

    public static void main(String args[]) throws Exception {
        Options options = buildCommandLineOptions();

        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.getArgs().length > options.getOptions().size()) {
            printHelpAndExit("Too many arguments passed in", options);
        }

        File currDir = null;
        if (cmd.hasOption("cache")) {
            currDir = resolveWorkingDirectory(cmd.getOptionValue("cache"));
        } else {
            currDir = resolveWorkingDirectory(System.getProperty("user.dir"));
        }
        currDir = currDir.getAbsoluteFile();

        // check to make sure the distributed-cache/libs and
        // distributed-cache/resources folder exists
        String libDir = currDir.getAbsolutePath() + File.separator + "lib";
        File libs = new File(libDir);

        if (!libs.exists() || !libs.isDirectory()) {
            printHelpAndExit("Could not find " + libDir, options);
        }

        if (!cmd.hasOption("job")) {
            printHelpAndExit("Configuration job file to use was not provided!", options);
        }

        String confFile = cmd.getOptionValue("job");

        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(confFile);
        ZephyrMRv1Configuration zephyrConfig = springContext.getBean("jobConfig", ZephyrMRv1Configuration.class);

        if (cmd.hasOption("ip")) {
            String inputPath = cmd.getOptionValue("ip");
            System.out.println("Overriding configuration file " + confFile + " configuration setting inputPath with " + inputPath);
            zephyrConfig.setInputPath(inputPath);
        }

        if (cmd.hasOption("op")) {
            String outputPath = cmd.getOptionValue("op");
            System.out.println("Overriding configuration file " + confFile + " configuration setting outputPath with " + outputPath);
            zephyrConfig.setOutputPath(outputPath);
        }

        Configuration config = new Configuration();
        int res = ToolRunner.run(config, new ZephyrDriver(zephyrConfig, confFile), buildArgsFromPath(libs));
        System.exit(res);
    }

    private static Options buildCommandLineOptions() {
        Options options = new Options();

        options.addOption("cache", true, "Path to folder containing distributed-cache and job config.  Default value is current directory.");
        options.addOption("job", true, "Name of xml file containing job configuration.");
        options.addOption("ip", true, "Input Path to run Zephyr ingest over (in hdfs).  Overrides job configuration setting.");
        options.addOption("op", true, "Output Path for Zephyr ingest to output to (in hdfs).  Overrides job configuration setting.");

        return options;
    }

    private static void printHelpAndExit(String errorMessage, Options options) {
        if (errorMessage != null && !errorMessage.trim().equals(""))
            System.err.println("Error: " + errorMessage);

        helpFormatter.printHelp("hadoop jar zephyr-mapreduce-<version>.jar org.zephyr.mapreduce.ZephyrDriver [option]...", options);
        System.exit(1);

    }

    private static File resolveWorkingDirectory(String path) {
        if (path.endsWith(File.separator)) {
            // remove trailing folder separator
            path = path.substring(0, path.length() - 1);
        }

        if (!path.endsWith(DEFAULT_CACHE_FOLDER_NAME)) {
            path += File.separator + DEFAULT_CACHE_FOLDER_NAME;
        }

        return new File(path).getAbsoluteFile();

    }

}
