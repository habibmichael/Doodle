package com.l2l.androided.mh122354.doodle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by mh122354 on 7/13/2016.
 */
public class LineDialogFragment extends DialogFragment {

    private ImageView widthImageView;

    @Override
    public Dialog onCreateDialog(Bundle bundle){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View lineWidthDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_line_width,null);
        builder.setView(lineWidthDialogView);

        builder.setTitle(R.string.title_line_width_dialog);

        widthImageView = (ImageView)lineWidthDialogView.findViewById(R.id.widthImageView);

        final DoodleView doodleView = getDoodleFragment().getDoodleView();
        final SeekBar widthSeekBar = (SeekBar)lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(lineWidthChanged);
        widthSeekBar.setProgress(doodleView.getLineWidth());

        builder.setPositiveButton(R.string.button_set_line_width,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doodleView.setLineWidth(widthSeekBar.getProgress());
                    }
                });

        return builder.create();

    }

    public MainActivityFragment getDoodleFragment(){
        return (MainActivityFragment)getFragmentManager().findFragmentById(
                R.id.doodleFragment);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null)
            fragment.setDialogOnScreen(true);
    }

    @Override
    public void onDetach(){
        super.onDetach();

        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null){
            fragment.setDialogOnScreen(false);
        }

    }

    private final SeekBar.OnSeekBarChangeListener lineWidthChanged =
            new SeekBar.OnSeekBarChangeListener() {

                final Bitmap bitmap = Bitmap.createBitmap(
                        400,100,Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap);
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    Paint p = new Paint();
                    p.setColor(
                            getDoodleFragment().getDoodleView().getDrawingColor()
                    );
                    p.setStrokeCap(Paint.Cap.ROUND);
                    p.setStrokeWidth(i);

                    //erase and redraw line
                    bitmap.eraseColor(
                            getResources().getColor(android.R.color.transparent,
                                    getContext().getTheme()));
                    canvas.drawLine(30,50,370,50,p);
                    widthImageView.setImageBitmap(bitmap);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
}
