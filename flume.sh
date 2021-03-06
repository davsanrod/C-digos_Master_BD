#!/usr/bin/env bash

CONF_FILE=$1
AGENT_NAME=$2

FLUME_HOME=apache-flume-1.9.0-bin

FLUME_BIN=${FLUME_HOME}/bin
FLUME_CONF=${FLUME_HOME}/conf

${FLUME_BIN}/flume-ng agent \ 
    --conf ${FLUME_CONF} \  
    --conf-file "$CONF_FILE" \
    --NAME "$AGENT_NAME" \
    -Dflume.root.logger=INFO,console  
