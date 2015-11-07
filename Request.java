package concurrency.simulations.web;

/**
 * Created by anna on 07/11/15.
 */
public class Request {
    private final int serviceTime;

    public Request(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        return "[" + serviceTime + "]";
    }
}
