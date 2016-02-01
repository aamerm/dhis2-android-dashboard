package org.hisp.dhis.android.dashboard.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class ConfirmDialogFragment extends DialogFragment {

    String title;
    String message;
    String firstOption;
    String secondOption;
    String thirdOption;
    OnClickListener firstOptionListener;
    OnClickListener secondOptionListener;
    OnClickListener thirdOptionListener;
    int iconId = -1;

    public ConfirmDialogFragment(String title, String message, String firstOption, String secondOption, OnClickListener firstOptionListener)
    {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.firstOptionListener = firstOptionListener;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getActivity() );
        if (iconId > 0) {
            alertDialogBuilder.setIcon(iconId);
        }
        alertDialogBuilder.setTitle( title );
        alertDialogBuilder.setMessage( message );
        // null should be your on click listener
        alertDialogBuilder.setPositiveButton(firstOption, firstOptionListener);
        if(secondOptionListener ==null)
            secondOptionListener = new OnClickListener()
            {

                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    dialog.dismiss();
                }
            };
        if(secondOption !=null) {
            alertDialogBuilder.setNegativeButton(secondOption, secondOptionListener);
        }
        if(thirdOption!=null) {
            alertDialogBuilder.setNeutralButton(thirdOption, thirdOptionListener);
        }


        return alertDialogBuilder.create();
    }
}
