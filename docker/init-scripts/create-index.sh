#!/bin/bash

# Wait for Elasticsearch to start
until curl -s http://localhost:9200 -o /dev/null; do
    sleep 1
done

# Create the index
curl -X PUT "http://localhost:9200/transport-trade"
