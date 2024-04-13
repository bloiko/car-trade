package org.transport.trade.service.elastic;

public class ElasticSearchOperationFailedException extends RuntimeException {

    public ElasticSearchOperationFailedException(String message) {
        super(message);
    }

    public ElasticSearchOperationFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
