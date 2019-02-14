
import model.Session;
import java.util.*;

/**
 * Created by Sharon on 2/14/19.
 * This class creates sessions from streaming input and delivers metrics when sessions end.
 */
public class EdgarMetrics {
    Map<String, Session> hm = new LinkedHashMap<>();
    int inactivity_period;

    EdgarMetrics(int inactivity_period) {
        this.inactivity_period = inactivity_period;
    }

    /**
     * Creates session object from stream.
     * If a session already exists, merge metrics. (Incr docs requested if different, update end time.)
     * @param stream
     */
    public void sessionize(String[] stream) {
        Session s = new Session(stream);
        if(hm.containsKey(s.getIp())) {
            Session existingSession = hm.get(s.getIp());
            if(existingSession.isActive(s.getEnd(), inactivity_period)) {
                existingSession.add(s);
            }
        } else {
            hm.put(s.getIp(), s);
        }
    }

    /**
     * Checks for any expired sessions.
     * Returns expired sessions to write to log.
     * @return expired sessions
     */
    public List<String> getPartialMetricsLog(String curClockTime) {
        List<String> expiredList = new ArrayList<>();
        List<String> paritalMetricsLog = new ArrayList<>();
        for(Map.Entry<String, Session> e : hm.entrySet()) {
            if(!e.getValue().isActive(curClockTime, inactivity_period)) {
                expiredList.add(e.getKey());
            }
        }
        for(String expired : expiredList) {
            Session removed = hm.remove(expired);
            paritalMetricsLog.add(removed.toString());
        }
        return paritalMetricsLog;
    }

    /**
     * If there are any sessions remaining when the stream ends, expire all of them and write to log.
     * Order of expiration is queue-like, order of input is preserved.
     * @return forced expired sessions
     */
    public List<String> getMetricsLog() {
        List<String> expiredList = new ArrayList<>();
        for (Session s: hm.values()) {
            expiredList.add(s.toString());
        }
        return expiredList;
    }

}
