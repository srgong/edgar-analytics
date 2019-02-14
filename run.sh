#!/bin/bash

javac ./src/Driver.java ./src/EdgarMetrics.java ./src/model/Session.java
java Driver input/log.csv output/sessionization.txt input/inactivity_period.txt
