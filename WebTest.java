package concurrency.simulations.web;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by anna on 07/11/15.
 */
public class WebTest {
    static final int OPTIMIZATION_PERIOD = 2000;
    static final int SERVER_COUNT = 5;

    public static void main(String[] args) throws IOException {
        ExecutorService exec = Executors.newCachedThreadPool();
        ClientLine clientLine = new ClientLine(SERVER_COUNT);

        exec.execute(new ClientGenerator(clientLine));
        exec.execute(new ServerManager(exec, clientLine, OPTIMIZATION_PERIOD));

        if (args.length == 0) {
            System.out.println("press enter to quit");
            System.in.read();
        }

        exec.shutdownNow();
    }
}
