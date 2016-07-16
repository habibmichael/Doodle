package com.l2l.androided.mh122354.doodle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by mh122354 on 7/13/2016.
 */
public class EraseDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle bundle){

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.message_erase);

        //add erase button
        builder.setPositiveButton(R.string.button_erase,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDoodleFragment().getDoodleView().clear();
                    }
                });

        //add cancel button
        builder.setNegativeButton(android.R.string.cancel,null);
        return builder.create();
    }

    private MainActivityFragment getDoodleFragment(){

        return (MainActivityFragment)getFragmentManager().findFragmentById(
                R.id.doodleFragment);
    }

    @Override
    public void onDetach(){

        super.onDetach();
        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null)
            fragment.setDialogOnScreen(false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        MainActivityFragment fragment = getDoodleFragment();

        if(fragment!=null)
            fragment.setDialogOnScreen(true);
    }


}
