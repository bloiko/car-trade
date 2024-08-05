package org.transport.trade.elastic;

public class ElasticSearchOperationFailedException extends RuntimeException {

    public ElasticSearchOperationFailedException(String message) {
        super(message);
    }

    public ElasticSearchOperationFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
