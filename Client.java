package concurrency.simulations.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * Created by anna on 07/11/15.
 */
public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private Random random = new Random(33);
    private RequestLine requests;

    public Client(int requestCount) {
        try {
            requests = new RequestLine(requestCount);
        } catch (IllegalArgumentException e) {
            LOGGER.info("requestCount = " + requestCount);
            LOGGER.error(e);

            requests = new RequestLine(++requestCount);
        }

        for (int i = 0; i < requestCount; i++) {
            requests.add(new Request(random.nextInt(1000)));
        }
    }

    public Request getRequest() throws InterruptedException {
        return requests.take();
    }

    public int getRequestCount() {
        return requests.size();
    }

    @Override
    public String toString() {
        if (requests.isEmpty())
            return "{empty}";

        StringBuilder sb = new StringBuilder();

        for (Request request : requests)
            sb.append(request);

        return "{" + sb.toString() + "}";
    }
}
