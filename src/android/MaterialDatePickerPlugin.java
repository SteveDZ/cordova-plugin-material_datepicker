package com.plugin.datepicker;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import android.content.Context;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.app.FragmentManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaterialDatePickerPlugin extends CordovaPlugin {

    private static final String DIALOG_DATE = "DialogDate";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("displayDatePicker".equals(action)) {
            this.displayDatePicker(callbackContext);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }

    private void displayDatePicker(CallbackContext callbackContext) {
        FragmentManager manager = cordova.getActivity().getFragmentManager();
        DatePickerFragment dialog = new DatePickerFragment(callbackContext);
        dialog.show(manager, DIALOG_DATE);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private CallbackContext mCallbackContext;
        private String selectedDate;

        public DatePickerFragment(CallbackContext callbackContext) {
            this.mCallbackContext = callbackContext;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            final DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callbackContext.success(selectedDate);
                    dialog.dismiss();
                }
            });

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callbackContext.error("CANCELLED");
                    dialog.dismiss();
                }
            });

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            selectedDate = day + "/" + month + "/" + year;
        }
    }

}
