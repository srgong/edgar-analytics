# Table of Contents
1. [Introduction](README.md#introduction)
2. [Approach](README.md#approach)
3. [To Run](README.md#torun)


# Introduction

Securities and Exchange Commission's Electronic Data Gathering, Analysis and Retrieval (EDGAR) system is used to retrieve financial documents. Web logs are generated from this activity showing which IP addresses accessed which documents. This project gathers session metrics: calculating how long a user spends on EDGAR and how many documents that user requests during their session.

# Approach

The project simulates a stream of web log data. For every record processed, the Edgar Metrics class drives the 
1) sessionization 
2) state management
3) expiration of sessions

During sessionization, the streams are converted to Session objects. If a session already exists, the two sessions are merged with updated running metrics. Lastly, the Edgar Metrics class checks for any expired sessions (duration <= inactivity period) and writes that to a metrics log. If end of the stream is reached, then all sessions are written to this log in the order they were processed. This order is preserved by a LinkedHashMap.
 
# To Run

A script is provided to compile the java classes. 3 arguments are required to run this, logs.csv, sessionization.txt, and inactivity_period.txt

`./run.sh`

 