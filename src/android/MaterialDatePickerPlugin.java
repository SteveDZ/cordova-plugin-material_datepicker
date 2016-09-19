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
import android.util.Log;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaterialDatePickerPlugin extends CordovaPlugin {

    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private static final String CANCELLED_STATUS = "CANCELLED";

    private static final String STATUS_KEY = "status";
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";

    private static final String OPTIONS_SELECTED_DAY = "selectedDay";
    private static final String OPTIONS_SELECTED_MONTH = "selectedMonth";
    private static final String OPTIONS_SELECTED_YEAR = "selectedYear";

    private static final String DIALOG_DATE = "DialogDate";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("displayDatePicker".equals(action)) {
            this.displayDatePicker(callbackContext, args);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }

    private void displayDatePicker(CallbackContext callbackContext, JSONArray args) {
        FragmentManager manager = cordova.getActivity().getFragmentManager();
        DatePickerFragment dialog = new DatePickerFragment(callbackContext, args);
        dialog.show(manager, DIALOG_DATE);
    }

    public static class DatePickerFragment extends DialogFragment {

        private static final String DATE_PICKER_TAG = "DatePickerFragment";

        private CallbackContext mCallbackContext;
        private String selectedDate;
        private JSONArray options;

        public DatePickerFragment(CallbackContext callbackContext, JSONArray options) {
            this.mCallbackContext = callbackContext;
            this.options = options;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            try {
                JSONObject dateObject = options.optJSONObject(0);

                year = dateObject.getInt(OPTIONS_SELECTED_YEAR);
                month = dateObject.getInt(OPTIONS_SELECTED_MONTH);
                day = dateObject.getInt(OPTIONS_SELECTED_DAY);
            } catch(JSONException jsonException) {
                Log.e(DATE_PICKER_TAG, "Error parsing plugin date options", jsonException);
            }

            final DatePickerDialog dialog = new DatePickerDialog(getActivity(), null, year, month, day);

            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                DatePicker datePicker = dialog.getDatePicker();

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject selectedDate = createDateSelectedJSONObject(datePicker);
                    
                    mCallbackContext.success(selectedDate.toString());
                    
                    Log.i(DATE_PICKER_TAG, "success: " + selectedDate.toString());

                    dialog.dismiss();
                }
            });

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject cancelledObject = createCancelledJSONObject();

                    Log.e(DATE_PICKER_TAG, "cancelled: " + cancelledObject.toString());

                    dialog.dismiss();
                }
            });

            return dialog;
        }

        private JSONObject createDateSelectedJSONObject(DatePicker datePicker) {
            JSONObject selectedDateObject = new JSONObject();

            try {
                addStatusToJSONObject(selectedDateObject, SUCCESS_STATUS);
                selectedDateObject.put(DAY, datePicker.getDayOfMonth());
                selectedDateObject.put(MONTH, datePicker.getMonth());
                selectedDateObject.put(YEAR, datePicker.getYear());
            }catch(JSONException jsonException) {
                selectedDateObject = handleJSONException(selectedDateObject, jsonException);
            }

            return selectedDateObject;
        }

        private JSONObject createCancelledJSONObject() {
            JSONObject cancelledObject = new JSONObject();

            try {
                cancelledObject.put(STATUS_KEY, CANCELLED_STATUS);
            }catch(JSONException jsonException) {
                cancelledObject = handleJSONException(cancelledObject, jsonException);
            }

            return cancelledObject;
        }

        private JSONObject handleJSONException(JSONObject jsonObject, JSONException jsonException) {
            try {
                addStatusToJSONObject(jsonObject, ERROR_STATUS);
                Log.e(DATE_PICKER_TAG, "ERROR SERIALIZING SELECTED DATE TO JSON", jsonException);
            }catch(JSONException exception){
                Log.e(DATE_PICKER_TAG, "ERROR SERIALIZING SELECTED DATE TO JSON", exception);
            }

            return jsonObject;
        }

        private JSONObject addStatusToJSONObject(JSONObject jsonObject, String status) throws JSONException {
            jsonObject.put(STATUS_KEY, status);

            return jsonObject;
        }
    }

}
