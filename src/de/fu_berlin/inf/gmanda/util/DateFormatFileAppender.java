package de.fu_berlin.inf.gmanda.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

/**
 * DateFormatFileAppender uses a SimpleDateFormat pattern to set the name of the
 * log file.
 * 
 * Example:
 * 
 * 'logs/'yyyy/MM-MMM/dd-EEE/HH-mm-ss-S'.log'
 * 
 * will create a file like this:
 * 
 * logs/2004/04-Apr/13-Tue/09-45-15-937.log
 * 
 * This class was created based on a original design by James Stauffer but
 * little of the original implementation remains.
 * 
 * http://stauffer.james.googlepages.com/DateFormatFileAppender.java
 */
public class DateFormatFileAppender extends FileAppender {

    protected String fileBackup;

    @Override
    public synchronized void setFile(String fileName, boolean append,
        boolean bufferedIO, int bufferSize) throws IOException {

        /*
         * Make a back-up copy of the fileName, because it will be changed by us
         * and could not be reused.
         */
        if (this.fileBackup == null) {
            this.fileBackup = fileName;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fileBackup);
        String actualFileName = sdf.format(new Date());
        
        File parent = new File(actualFileName).getParentFile();

        if (parent != null){
            parent.mkdirs();
        }
        
        super.setFile(actualFileName, append, bufferedIO, bufferSize);
    }
}
