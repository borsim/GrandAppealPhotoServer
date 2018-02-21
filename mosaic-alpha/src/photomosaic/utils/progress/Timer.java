package photomosaic.utils.progress;

public class Timer {

    private long startTime;
    private Long stopTime = null;

    public Timer(long startTime) {
        this.startTime = startTime;
    }

    public static Timer start() {
        return new Timer(System.currentTimeMillis());
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    public long get() {
        return (stopTime != null? stopTime : System.currentTimeMillis() - stopTime);
    }

}
