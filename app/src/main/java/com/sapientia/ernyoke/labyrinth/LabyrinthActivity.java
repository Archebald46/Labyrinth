package com.sapientia.ernyoke.labyrinth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class LabyrinthActivity extends Activity implements SensorEventListener, RecognitionListener {

    /*private LabyrinthView labView;*/
    private LabyrinthModel labModel;
    SharedPreferences sharedPreferences;
    public View start;
    private static final int TIME = 170;
    private static final double G = 9.81;
    private double sensitivity;

    private long lastDetection;
    private long currentDetection;

    public enum CONTROL {ACCELEROMETER, TOUCH, GRAVITY, SPEECH, MI};

    private CONTROL inputControl;
    private SensorManager sensorManager;
    private Sensor activeSensor;
    private List<Sensor> sensorList;
    private boolean isEnd = false;
    private MainMenu.DIFFICULTY currentDiff;
    private SpeechRecognizer sr;
    private Intent listenIntent;
    public Display mDisplay;
    public WindowManager mWindowManager;
    public float mX,mY;
    public View labView;
    public View other;
    public RelativeLayout STR;


    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        currentDiff = (MainMenu.DIFFICULTY)bundle.get(Constants.DIFF_ID);
        labView = findViewById(R.id.labView);
        start =  findViewById(R.id.start);
        STR =  (RelativeLayout)  findViewById(R.id.start);
        other =  findViewById(R.id.Other);
        switch (currentDiff) {
            case EASY: {
                readLabyrinth(R.array.labyrinthEasy);
                break;
            }
            case MEDIUM: {
                readLabyrinth(R.array.labyrinthMedium);
                break;
            }
            case HARD: {
                readLabyrinth(R.array.labyrinthDifficult);
                break;
            }

        }
        this.getAvailableSensors();
        /*setContentView(R.layout.activity_main);*/
        labView = new LabyrinthView(this, labModel);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        startTime = System.currentTimeMillis();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int width = size.x;
        ViewGroup.LayoutParams params = start.getLayoutParams();
            params.height = height;
            params.width = height;
        labView.setLayoutParams(params);
        STR.addView(labView,params);
        other.setLayoutParams(new RelativeLayout.LayoutParams(width,width - height));



    }

    private void readLabyrinth(int resId) {
        Resources res = this.getResources();
        String[] labyrinthRows = res.getStringArray(resId);
        labModel = new LabyrinthModel();
        labModel.initLabyrinth(labyrinthRows);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int sensitivityPercent = 70;
        sensitivity = (G * (100.0 - (double)sensitivityPercent)) / 100.0;
        CONTROL control = CONTROL.ACCELEROMETER;
        if(checkIfHasSensor(control)) {
            inputControl = control;
            this.invalidateOptionsMenu();
        }
        labView.invalidate();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        switch(inputControl) {
            case ACCELEROMETER: {
                switch (mDisplay.getOrientation()) {
                    case Surface.ROTATION_0:
                        mX = sensorEvent.values[0];
                        mY = sensorEvent.values[1];
                        break;
                    case Surface.ROTATION_90:
                        mX = -sensorEvent.values[1];
                        mY = sensorEvent.values[0];
                        break;
                    case Surface.ROTATION_180:
                        mX = -sensorEvent.values[0];
                        mY = -sensorEvent.values[1];
                        break;
                    case Surface.ROTATION_270:
                        mX = sensorEvent.values[1];
                        mY = -sensorEvent.values[0];
                        break;
                }
                currentDetection = System.currentTimeMillis();
                if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && currentDetection - lastDetection > TIME) {

                    if(Math.abs(mX) > sensitivity) {
                        if(mX > 0) {
                            labModel.left();
                        }
                        else {
                            labModel.right();
                        }
                    }

                    if(Math.abs(mY) > sensitivity) {
                        if(mY > 0) {
                            labModel.down();
                        }
                        else {
                            labModel.up();
                        }
                    }

                    lastDetection = System.currentTimeMillis();
                    labView.invalidate();

                    if(labModel.isWinner()) {
                        winner();
                    }
                }
                break;
            }



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private void getAvailableSensors() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    private boolean checkIfHasSensor(CONTROL control) {
        switch (control) {
            case ACCELEROMETER: {
                this.unregisterSensors();
                for (int i = 0; i< sensorList.size(); i++) {
                    if (sensorList.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
                        sensorManager.unregisterListener(this);
                        activeSensor = sensorList.get(i);
                        sensorManager.registerListener(this,activeSensor, SensorManager.SENSOR_DELAY_UI);
                        return true;
                    }
                }
                break;
            }


        }
        return false;
    }


    public void unregisterSensors() {
        if(sr != null) {
            sr.stopListening();
            sr.cancel();
            sr.destroy();
        }
        sensorManager.unregisterListener(this);

    }

    private void winner() {
        unregisterSensors();
        long endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        double ellapsed = interval / 1000.0;
        isEnd = true;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(getString(R.string.dialog_title));
        if(currentDiff != MainMenu.DIFFICULTY.HARD && currentDiff != MainMenu.DIFFICULTY.NET) {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.go_to_mainmenu), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();

                }
            });
            alertDialog.setMessage(getString(R.string.dialog_text_next) + "\n" + getString(R.string.finished_in) + ellapsed + getString(R.string.seconds));
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.go_to_next), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("requestCode", Constants.REQ_CODE);
                    setResult(Constants.NEXT_DIFF);
                    finish();
                    isEnd = false;
                    return;
                }
            });
        }
        else {
            if(currentDiff != MainMenu.DIFFICULTY.NET) {
                alertDialog.setMessage(getString(R.string.finished_in) + ellapsed + getString(R.string.seconds));
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.go_to_mainmenu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("requestCode", Constants.REQ_CODE);

                        finish();
                        isEnd = false;
                        return;
                    }
                });
            }
            else {
                alertDialog.setMessage(getString(R.string.finished_in) + ellapsed + getString(R.string.seconds));
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.go_to_mainmenu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("requestCode", Constants.REQ_CODE);

                        finish();
                        isEnd = false;
                        return;
                    }
                });
            }
        }

        alertDialog.show();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) { }
    @Override
    public void onBeginningOfSpeech() { }
    @Override
    public void onRmsChanged(float v) { }
    @Override
    public void onBufferReceived(byte[] bytes) {}
    @Override
    public void onEndOfSpeech() {}
    @Override
    public void onError(int i) {
        sr.startListening(listenIntent);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


        for (int i = 0; i < data.size(); i++)
        {
            if(data.get(i).equals("left")) {
                labModel.left();
                break;
            }
            if(data.get(i).equals("right")) {
                labModel.right();
                break;
            }
            if(data.get(i).equals("up")) {
                labModel.up();
                break;
            }
            if(data.get(i).equals("down")) {
                labModel.down();
                break;
            }
        }

        if(labModel.isWinner()) {
            winner();
            labView.invalidate();
        }
        else {
            labView.invalidate();
            sr.startListening(listenIntent);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}
