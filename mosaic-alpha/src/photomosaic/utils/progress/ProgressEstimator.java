package photomosaic.utils.progress;

public class ProgressEstimator {

    private Timer timer;

    public ProgressEstimator(Timer timer) {
        if (timer == null) {
            throw new NullPointerException();
        }
        this.timer = timer;
    }

    public long estimateRemainingTime(double progress) {
        if (progress < 1.0E-06) {
            return Long.MAX_VALUE;
        }

        progress = Math.min(Math.max(0.0, progress), 1.0);
        long spentTime = timer.get();
        double speed = progress / spentTime;
        double remainingProgress = 1.0 - progress;

        return Math.round(remainingProgress / speed);
    }

}
