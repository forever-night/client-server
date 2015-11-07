package concurrency.simulations.web;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by anna on 07/11/15.
 */
public class ClientLine extends ArrayBlockingQueue<Client> {
    public ClientLine(int capacity) {
        super(capacity);
    }

    public synchronized int getRequestsCount() {
        int result = 0;

        for (Client client : this) {
            assert client != null : "client == null";
            result += client.getRequestCount();
        }

        return result;
    }

    @Override
    public String toString() {
        if (this.isEmpty())
            return "[Empty]";

        StringBuilder sb = new StringBuilder();

        for (Client client : this) {
            sb.append(client);
            sb.append("\n");
        }

        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }
}
