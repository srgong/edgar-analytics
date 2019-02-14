package model;

import java.text.SimpleDateFormat;

/**
 * Created by Sharon on 2/14/19.
 * This class models a session. It converts raw EDGAR streams into
 * a meaningful class with metrics defined by the business.
 */
public class Session {
    String ip;
    String date;
    String start;
    String end;
    Long duration;
    int requests;



    boolean isActive;

    public Session(String [] stream) {
        ip = stream[0];
        date = stream[1];
        String time = stream[2];
        String cik = stream[4];
        String accession = stream[5];
        String extention = stream[6];

        start = date + " " + time;
        end = date + " " + time;
        duration = 1L;
        requests = 1;

        isActive=true;
        String requestId = cik+accession+extention;
    }

    /**
     * Combines sessions if ip matches and the stream is still active.
     * @param s
     */
    public void add(Session s) {
        end = s.start;
        duration = updateDuration();
        requests++;
    }

    public Long updateDuration() {
        Long duration=this.duration;
        try {
            Long start = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.start).getTime() / 1000;
            Long end = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.end).getTime() / 1000;
            duration = Math.abs(end-start)+1;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return duration;
    }
    /**
     * Checks to see if the session is still active.
     * A session is active if its duration is <= user specified inactivity time.
     * A session duration is calculated from latest clock time - last activity time.
     * @param inactivityPeriod
     * @return
     */
    public boolean isActive(String curClockTime, int inactivityPeriod) {
        try {
            Long now = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(curClockTime).getTime() / 1000;
            Long end = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.end).getTime() / 1000;
            Long sessionDuration = now-end;
            isActive = sessionDuration <= inactivityPeriod;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isActive;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * A session is defined by the same key, or ip.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return ip.equals(session.ip);
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public String toString() {
        return ip + "," + start + "," + end + "," + duration + "," + requests;

    }
}
