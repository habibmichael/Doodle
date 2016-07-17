package com.l2l.androided.mh122354.doodle;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    //used to identify the request for saving
    private static final int SAVE_IMAGE_REQUEST_CODE=1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

        doodleView = (DoodleView)view.findViewById(R.id.doodleView);

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


                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        //display menu
        inflater.inflate(R.menu.doodle_fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getFragmentManager(),"color dialog");
                return true;

            case R.id.line_width:
                LineDialogFragment lineDialog=new LineDialogFragment();
                lineDialog.show(getFragmentManager(),"line dialog");
                return true;

            case R.id.delete_drawing:
                confirmErase();
                return true;

            case R.id.save:
                saveImage();
                return true;

            case R.id.print:
                doodleView.printImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmErase(){
        EraseDialogFragment fragment = new EraseDialogFragment();
        fragment.show(getFragmentManager(),"erase dialog");
    }

    private void saveImage(){


        //check to see if app doesn't have permission needed
        if(getContext().checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){


            //show an explanation of why permission is needed
            if(shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                AlertDialog.Builder builder=
                        new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.permission_explanation);

                //add an ok button to the dialog
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which){


                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        SAVE_IMAGE_REQUEST_CODE);
                            }

                        }
                );
                builder.create().show();
            }
            else{
                //no dialog, request permission
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SAVE_IMAGE_REQUEST_CODE);
                        }

            }
            //if app already has permission
            else{
            doodleView.saveImage();
        }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                          int[] grantResults){


        switch(requestCode){
            //choose appropriate action based on which feature asked for permission
            case SAVE_IMAGE_REQUEST_CODE:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    doodleView.saveImage();
                    return;
                }
        }

    }

    public DoodleView getDoodleView(){
        return doodleView;
    }

    public void setDialogOnScreen(boolean visible){
        dialogOnScreen=visible;
    }


}






