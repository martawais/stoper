package mw.stoper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    private Button btnStartTime;
    private Button btnStopTime;
    private Button btnResetTime;
    public TextView mTextField;
    private BroadcastReceiver receiver;
    private String currentTime;

    public TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Display display = getWindowManager().getDefaultDisplay();

        mTextField = (TextView) findViewById(R.id.time);
        btnStartTime = (Button) findViewById(R.id.startTime);
        btnStopTime = (Button) findViewById(R.id.stopTime);
        btnResetTime = (Button) findViewById(R.id.resetTime);

        btnStartTime.setWidth(display.getWidth()/3-1);
        btnResetTime.setWidth(display.getWidth()/3-1);
        btnStopTime.setWidth(display.getWidth() / 3 - 1);


        mTextField.setText("00:00:00");



        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String secondsString = intent.getStringExtra(MyService.COPA_MESSAGE);
                int seconds = Integer.parseInt(secondsString);
                int minutes = 0;
                int hours = 0;
                if(seconds>59) {
                    minutes = seconds/60;
                    if(minutes>59) {
                        hours = minutes/60;
                        minutes = minutes%60;
                    }
                    seconds = seconds%60;
                }
                if(seconds<10) {
                    if(minutes<10) {

                        if(hours<10) {
                            currentTime = "0" + hours + ":" + "0" + minutes + ":" + "0" + seconds;
                            mTextField.setText(currentTime);
                        }
                        else{
                            currentTime = hours + ":" + "0" + minutes + ":" + "0" + seconds;
                            mTextField.setText(currentTime);
                        }
                    }
                    else {
                        if(hours<10) {
                            currentTime = "0" + hours + ":" + minutes + ":" + "0" + seconds;
                            mTextField.setText(currentTime);
                        }
                        else {
                            currentTime = hours + ":" + minutes + ":" + "0" + seconds;
                            mTextField.setText(currentTime);
                        }
                    }
                }
                else {
                    if(minutes<10) {
                        if(hours<10) {
                            currentTime = "0" + hours + ":" + "0" + minutes + ":" + seconds;
                            mTextField.setText(currentTime);
                        }
                        else {
                            currentTime = hours + ":" + "0" + minutes + ":" + seconds;
                            mTextField.setText(currentTime);
                        }
                    }
                    else {
                        if(hours<10) {
                            currentTime = "0" + hours + ":" + minutes + ":" + seconds;
                            mTextField.setText(currentTime);
                        }
                        else {
                            currentTime = hours + ":" + minutes + ":" + seconds;
                            mTextField.setText(currentTime);
                        }
                    }
                }

            }
        };




        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "btnStartTime", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startMyService();
                btnStartTime.setEnabled(false);
                btnResetTime.setEnabled(false);
                btnStopTime.setEnabled(true);

            }
        });

        btnResetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextField.setText("00:00:00");
                btnStartTime.setEnabled(true);
                btnResetTime.setEnabled(false);

            }
        });

        btnStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "btnStopTime", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                stopMyService();
                btnStopTime.setEnabled(false);
                btnResetTime.setEnabled(true);

            }
        });


    }

   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("currentTime", currentTime);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTime = savedInstanceState.getString("currentTime");
    }
*/
    private void startMyService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
    }

    private void stopMyService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(MyService.COPA_RESULT)
        );



    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }
}
