import java.io.*;
import java.util.List;

/**
 * Created by Sharon on 2/14/19.
 * This class simulates EDGAR streams and uses the EdgarMetrics class to sessionize and
 * write metrics upon session expiration.
 * If the stream ends, signaling no further requests, all sessions expire and metrics are written to log
 * in the order that they were received.
 */
class Driver {
    /**
     * checks inputs exist and is a file
     * @param f
     * @throws FileNotFoundException
     */
    public static void checkInputFiles(File f) throws FileNotFoundException {
        if(!f.exists()) throw new FileNotFoundException();
        if(f.isDirectory()) throw new FileNotFoundException();
    }

    /**
     * creates output file if it does not already exist
     * @param f
     * @throws IOException
     */
    public static void checkOutputFile(File f) throws IOException {
        f.createNewFile();
    }

    /**
     * Keeps track of cur clock time.
     * @param stream
     * @return
     */
    public static String getCurClockTime(String [] stream) {
        return stream[1] + " " + stream[2];
    }

    public static void main (String args[]) {
        if(args.length != 3) {
            System.err.println("Usage:\n    " +
                    "java Driver.java <streaming_file> <output_file> <inactivity_period_file>");
            System.exit(1);
        }

        String streamingFile= args[0];
        String metricsLogFile= args[1];
        String inactivityPeriodFile= args[2];

        try {
            checkInputFiles(new File(streamingFile));
            checkInputFiles(new File(inactivityPeriodFile));
            checkOutputFile(new File(metricsLogFile));


            BufferedReader br = new BufferedReader(new FileReader(streamingFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(metricsLogFile));

            BufferedReader inactivityPeriodReader = new BufferedReader(new FileReader(inactivityPeriodFile));
            int inactivityPeriodInt = Integer.parseInt(inactivityPeriodReader.readLine().trim());
            EdgarMetrics em = new EdgarMetrics(inactivityPeriodInt);

            // header
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String [] stream = line.split(",");
                em.sessionize(stream);
                List<String> partialMetricsLog = em.getPartialMetricsLog(getCurClockTime(stream));
                for(String log : partialMetricsLog) {
                    bw.write(log);
                    bw.newLine();
                }
                bw.flush();
            }
            List<String> metricsLog = em.getMetricsLog();
            for (String log : metricsLog) {
                bw.write(log);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}