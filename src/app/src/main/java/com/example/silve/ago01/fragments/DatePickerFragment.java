package com.example.silve.ago01.fragments;

import android.content.Context;
//import android.icu.util.Calendar;
import java.util.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.silve.ago01.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month,
                day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        EditText editText = (EditText)getActivity().findViewById(R.id.expired_at);
        editText.setText(year + "/" +month+ "/" + dayOfMonth);
    }


}
