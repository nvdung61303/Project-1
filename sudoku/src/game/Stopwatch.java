package game;

public class Stopwatch {
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
            displayTime();
        }
    }

    public void stop() {
        if (isRunning) {
            elapsedTime += System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void reset() {
        startTime = 0;
        elapsedTime = 0;
        isRunning = false;
    }

    public long getElapsedTime() {
        if (isRunning) {
            return elapsedTime + System.currentTimeMillis() - startTime;
        } else {
            return elapsedTime;
        }
    }

    public String getFormattedElapsedTime() {
        long seconds = getElapsedTime() / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    public String displayTime() {
        while (isRunning) {
            try {
                Thread.sleep(1000); // Update every second
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        
            return getFormattedElapsedTime();
        }
        
        return getFormattedElapsedTime();
    }

    public boolean isRunning() {
        return isRunning;
    }

}