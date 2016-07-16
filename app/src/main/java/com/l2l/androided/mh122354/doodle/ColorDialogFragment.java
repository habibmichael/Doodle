package com.l2l.androided.mh122354.doodle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by mh122354 on 7/10/2016.
 */
public class ColorDialogFragment extends DialogFragment{

    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar blueSeekBar;
    private SeekBar greenSeekBar;
    private View colorView;
    private int color;

    @Override
    public Dialog onCreateDialog (Bundle bundle){

        //create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_color,null);
        builder.setView(colorDialogView);

        builder.setTitle(R.string.title_color_dialog);

        //get Seekbars and set their change listeners
        alphaSeekBar=(SeekBar)colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar=(SeekBar)colorDialogView.findViewById(R.id.redSeekBar);
        blueSeekBar=(SeekBar)colorDialogView.findViewById(R.id.blueSeekBar);
        greenSeekBar=(SeekBar)colorDialogView.findViewById(R.id.greenSeekBar);

        colorView=colorDialogView.findViewById(R.id.colorView);

        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        //use current drawing color to set SeekBar values
        final DoodleView doodleView = getDoodleFragment().getDoodleView();
        color = doodleView.getDrawingColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        blueSeekBar.setProgress(Color.blue(color));
        greenSeekBar.setProgress(Color.green(color));

        //add set Color button
        builder.setPositiveButton(R.string.button_set_color,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doodleView.setDrawingColor(color);
                    }
                }
        );
        return builder.create();
    }

    private MainActivityFragment getDoodleFragment(){

        return (MainActivityFragment)getFragmentManager().findFragmentById(
                R.id.doodleFragment);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null){
            fragment.setDialogOnScreen(true);
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null)
            fragment.setDialogOnScreen(false);
    }

    private final SeekBar.OnSeekBarChangeListener colorChangedListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b){
                        color = Color.argb(alphaSeekBar.getProgress(),
                                redSeekBar.getProgress(),greenSeekBar.getProgress(),
                                blueSeekBar.getProgress());
                        colorView.setBackgroundColor(color);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

}
