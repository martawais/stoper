package mw.stoper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by mstowska on 7/8/2016.
 */
public class MyService extends Service {


    final CountUpTimer nowy_timer = new Timer(1000);

    private LocalBroadcastManager broadcaster;

    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";



    private class Timer extends CountUpTimer {
        public Timer(long interval) {
            super(interval);
        }
        @Override
        public void onTick(long elapsedTime) {
            long time = elapsedTime/1000;
            sendResult(time+"");
        }
    }


    private void writeToLogs(String message) {
        Log.d("HelloServices", message);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        writeToLogs("Called onCreate() method.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeToLogs("Called onStartCommand() methond");
        nowy_timer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        writeToLogs("Called onDestroy() method");
        nowy_timer.stop();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void sendResult(String message) {
        Intent intent = new Intent(COPA_RESULT);
        if(message != null)
            intent.putExtra(COPA_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }

}
