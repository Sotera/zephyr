package org.zephyr.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileAlreadyExistsException;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.mapreduce.security.TokenCache;

public class ZephyrOutputFormat extends MultipleTextOutputFormat<Text, Text> {

    protected String generateFileNameForKeyValue(Text key, Text value, String leaf) {
        return new Path(key.toString(), leaf).toString();
    }

    protected Text generateActualKey(Text key, Text value) {
        return null;
    }

    protected String getInputFileBasedOutputFileName(JobConf job, String name) {
        String interimName = super.getInputFileBasedOutputFileName(job, name);
        int lastIndex = interimName.lastIndexOf("/");
        StringBuilder builder = new StringBuilder();
        builder.append(interimName.substring(0, lastIndex + 1));
        builder.append(job.get("zephyr.job.uuid"));
        builder.append("-");
        builder.append(interimName.substring(lastIndex + 1));
        return builder.toString();
    }

    @Override
    public void checkOutputSpecs(FileSystem ignored, JobConf job) throws FileAlreadyExistsException, InvalidJobConfException, IOException {
        // Ensure that the output directory is set and not already there
        Path outDir = getOutputPath(job);
        if (outDir == null && job.getNumReduceTasks() != 0) {
            throw new InvalidJobConfException("Output directory not set in JobConf.");
        }
        if (outDir != null) {
            FileSystem fs = outDir.getFileSystem(job);
            // normalize the output directory
            outDir = fs.makeQualified(outDir);
            setOutputPath(job, outDir);

            // get delegation token for the outDir's file system
            TokenCache.obtainTokensForNamenodes(job.getCredentials(), new Path[]{outDir}, job);
            String jobUuid = job.get("zephyr.job.uuid");
            if (jobUuid == null)
                throw new InvalidJobConfException("This output format REQUIRES the value zephyr.job.uuid to be specified in the job configuration!");
            // // check its existence
            // if (fs.exists(outDir)) {
            // throw new FileAlreadyExistsException("Output directory " + outDir
            // + " already exists");
            // }
        }
    }

}
