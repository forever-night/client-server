package concurrency.simulations.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by anna on 07/11/15.
 */
public class ServerManager implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ServerManager.class);

    private int optimizationPeriod;
    private int optimizationCount = 0;
    private ExecutorService exec;
    private ClientLine clientLine;

    private PriorityQueue<Server> serversUp = new PriorityQueue<Server>();
    private LinkedList<Server> serversDown = new LinkedList<Server>();

    public ServerManager(ExecutorService exec, ClientLine clientLine, int optimizationPeriod) {
        this.exec = exec;
        this.clientLine = clientLine;
        this.optimizationPeriod = optimizationPeriod;

        Server server = new Server(clientLine);
        exec.execute(server);
        serversUp.add(server);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(optimizationPeriod);

                optimize();


                LOGGER.info("### " + optimizationCount + " ###");
                LOGGER.info(clientLine);

                StringBuilder sb = new StringBuilder();

                for (Server server : serversUp) {
                    sb.append(server);
                }

                LOGGER.info(sb.toString());
                LOGGER.info("#########\n");
            }
        } catch (InterruptedException e) {
            LOGGER.warn(this + " interrupted");
        }

        LOGGER.info(this + " stopping");
    }

    private void optimize() {
        optimizationCount++;

        if (clientLine.isEmpty()) {
            while (serversUp.size() > 1)
                stopOneServer();

            return;
        }

        if (clientLine.getRequestsCount() / serversUp.size() > 3) {
            if (serversDown.size() > 0) {
                Server server = serversDown.remove();
                server.processRequests();
                serversUp.offer(server);
                return;
            }

            Server server = new Server(clientLine);
            exec.execute(server);
            serversUp.add(server);
            return;
        }

        if (serversUp.size() > 1 &&
                clientLine.getRequestsCount() / serversUp.size() < 3) {
            stopOneServer();
            return;
        }
    }

    private void stopOneServer() {
        Server server = serversUp.remove();
        server.stopProcessing();
        serversDown.add(server);
    }

    @Override
    public String toString() {
        return "ServerManager";
    }
}
