package concurrency.simulations.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by anna on 07/11/15.
 */
public class Server implements Runnable, Comparable<Server>{
    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    private static int count = 0;
    private final int id = count++;

    private boolean isUp = true;
    private int requestsProcessed = 0;

    private ClientLine clientLine;

    public Server(ClientLine clientLine) {
        this.clientLine = clientLine;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Request request;

                synchronized(clientLine) {
                    Client client = clientLine.take();
                    request = client.getRequest();

                    if (client.getRequestCount() > 0)
                        clientLine.offer(client);
                }

                TimeUnit.MILLISECONDS.sleep(request.getServiceTime());

                synchronized(this) {
                    requestsProcessed++;

                    while (!isUp)
                        wait();
                }
            }
        } catch (InterruptedException e) {
            LOGGER.warn(this + " interrupted");
        }

        LOGGER.info(this + " stopping");
    }

    public synchronized void processRequests() {
        assert !isUp : "already up " + this;

        isUp = true;
        notifyAll();
    }

    public synchronized void stopProcessing() {
        isUp = false;
        requestsProcessed = 0;
    }

    @Override
    public String toString() {
        return "[S #" + id + "]";
    }

    @Override
    public int compareTo(Server o) {
        return requestsProcessed < o.requestsProcessed ? -1 :
                requestsProcessed > o.requestsProcessed ? 1 : 0;
    }
}
