package concurrency.simulations.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by anna on 07/11/15.
 */
public class ClientGenerator implements Runnable{
    private static final Logger LOGGER = LogManager.getLogger(ClientGenerator.class);

    private ClientLine clientLine;
    private Random random = new Random(33);

    public ClientGenerator(ClientLine clientLine) {
        this.clientLine = clientLine;
    }

    @Override
    public void run() {
        int requestCount = 0;

        try {
            while (!Thread.interrupted()) {
                requestCount = random.nextInt(7);

                if (requestCount == 0)
                    requestCount++;

                if (requestCount > 0) {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(250));
                    clientLine.put(new Client(requestCount));
                }
            }
        } catch (InterruptedException e) {
            LOGGER.warn("ClientGenerator interrupted");
        }

        LOGGER.info("ClientGenerator stopping");
    }
}
