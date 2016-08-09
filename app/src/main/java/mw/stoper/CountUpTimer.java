package mw.stoper;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by mstowska on 7/8/2016.
 */
public abstract class CountUpTimer {

    private final long interval;
    private long base;

    public CountUpTimer(long interval) {
        this.interval = interval;
    }

    public void start() {
        base = SystemClock.elapsedRealtime();
        handler.sendMessage(handler.obtainMessage(MSG));

    }

    public void stop() {
        handler.removeMessages(MSG);

    }

    public void reset() {
        synchronized (this) {
            base = SystemClock.elapsedRealtime();
        }
    }

    abstract public void onTick(long elapsedTime);

    private static final int MSG = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountUpTimer.this) {
                long elapsedTime = SystemClock.elapsedRealtime() - base;
                onTick(elapsedTime);
                sendMessageDelayed(obtainMessage(MSG), interval);


            }
        }
    };
}
