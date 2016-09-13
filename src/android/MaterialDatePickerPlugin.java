package com.plugin.datepicker;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import android.content.Context;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
//import android.support.v7.app.AlertDialog;
import android.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

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
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.show(manager, DIALOG_DATE);

        callbackContext.success("selectedDate");
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePicker datePicker = new DatePicker(getActivity());
            datePicker.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
            //datePicker.setBackgroundColor(0xFF46a19e);
            //datePicker.setCalendarViewShown(false);

            return new AlertDialog.Builder(getActivity())
                    .setView(datePicker)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }

}