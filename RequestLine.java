package concurrency.simulations.web;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by anna on 07/11/15.
 */
public class RequestLine extends ArrayBlockingQueue<Request> {
    public RequestLine(int capacity) {
        super(capacity);
    }
}
