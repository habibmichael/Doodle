package com.l2l.androided.mh122354.doodle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private DoodleView doodleView;
    private boolean dialogOnScreen=false;

    //constant used to determine a shake on device
    private static final int ACCELERATION_THRESHOLD=10000;

    //used to indentify the request for saving
    private static final int SAVE_IMAGE_REQUEST_CODE=1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

        //acceleration values
        acceleration=0;
        currentAcceleration= SensorManager.GRAVITY_EARTH;
        lastAcceleration=SensorManager.GRAVITY_EARTH;



        return view;
    }


    @Override
    public void onResume(){

        super.onResume();
        enableAccelerometerListening();
    }

    private void enableAccelerometerListening() {

        //Get Manager
        SensorManager sensorManager= (SensorManager)getActivity().getSystemService
                (Context.SENSOR_SERVICE);

        //Register to listen to accelerometer events
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();

        disableAccelerometerListening();
    }

    private void disableAccelerometerListening() {

        //Get Manager
        SensorManager sensorManager= (SensorManager)getActivity().getSystemService
                (Context.SENSOR_SERVICE);

        //Unregister listener to accelerometer events
        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

    }

    private final SensorEventListener sensorEventListener =
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    //ensure other dialogs are not displayed
                    if(!dialogOnScreen){
                        float x = sensorEvent.values[0];
                        float y = sensorEvent.values[1];
                        float z=sensorEvent.values[2];

                        ///save previous & get current acceleration
                        lastAcceleration=currentAcceleration;
                        currentAcceleration = x*x + y*y + z*z;

                        acceleration=currentAcceleration*
                                (currentAcceleration-lastAcceleration);

                        if(acceleration>ACCELERATION_THRESHOLD){
                            confirmErase();
                        }
                    }
                }

                private void confirmErase(){
                    EraseDialogFragment fragment = new EraseDialogFragment();
                    fragment.show(getFragmentManager(),"erase dialog");
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };





}
