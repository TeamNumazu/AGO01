package com.example.silve.ago01.fragments;

import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.example.silve.ago01.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText editText = (EditText)getActivity().findViewById(R.id.expired_at);

        // 月が1ヶ月ずれるので強引にfix
        // 年末年始でもおかしな日付にはならんかった
        int fixedMonth = month + 1;

        editText.setText(year + "/" + fixedMonth + "/" + dayOfMonth);
    }

}
