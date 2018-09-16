package com.nuryazid.wheelpicker.ui;

import android.content.Context;
import android.view.View;

import com.nuryazid.wheelpicker.R;
import com.nuryazid.wheelpicker.util.WheelPicker;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimePicker {

    private WheelPicker hourWheel;
    private WheelPicker minuteWheel;
    private WheelPicker meridiemWheel;

    private int hourPos = 0;
    private int minutePos = 0;
    private int meridiemPos = 0;

    private List<String> hourList = new ArrayList<String>();
    private List<String> minuteList = new ArrayList<String>();
    private List<String> meridiemList = new ArrayList<String>();

    private PickerListener pickerListener;

    private DialogPlus dialogPicker;

    public TimePicker(Context context) {

        initValue();

        dialogPicker = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_time_picker))
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        int i = view.getId();
                        if (i == R.id.btnCancel) {
                            dialog.dismiss();
                        } else if (i == R.id.btnSave) {
                            dialog.dismiss();
                            String strTimeLocal = format2LenStr(Integer.parseInt(hourList.get(hourWheel.getCurrentItemPosition())))
                                    + ":"
                                    + format2LenStr(Integer.parseInt(minuteList.get(minuteWheel.getCurrentItemPosition())))
                                    + ":00 "
                                    + meridiemList.get(meridiemWheel.getCurrentItemPosition());

                            if (pickerListener != null) {
                                pickerListener.dataSet(strTimeLocal);
                            }
                        }
                    }
                })
                .create();

        View view = dialogPicker.getHolderView();

        hourWheel = view.findViewById(R.id.hourWheel);
        minuteWheel = view.findViewById(R.id.minuteWheel);
        meridiemWheel = view.findViewById(R.id.meridemWheel);

        hourWheel.setData(hourList);
        minuteWheel.setData(minuteList);
        meridiemWheel.setData(meridiemList);

        hourWheel.setSelectedItemPosition(hourPos);
        minuteWheel.setSelectedItemPosition(minutePos);
        meridiemWheel.setSelectedItemPosition(meridiemPos);

    }

    public TimePicker show() {
        dialogPicker.show();
        return this;
    }

    private void initValue() {
        String intH = timeToday().split(":")[0];
        String intM = timeToday().split(":")[1];
        String strA = timeToday().split(":")[2];

        int hourCount = 12;
        int minCount = 59;

        for (int i = 1; i <= hourCount; i++) {
            hourList.add(Integer.toString(i));
        }

        for (int i = 0; i <= minCount; i++) {
            minuteList.add(Integer.toString(i));
        }

        meridiemList.add("AM");
        meridiemList.add("PM");

        hourPos = hourList.indexOf(intH);
        minutePos = minuteList.indexOf(intM);
        meridiemPos = meridiemList.indexOf(strA);
    }

    private static String timeToday() {
        DateFormat formatter = new SimpleDateFormat("h:m:a", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    private static String format2LenStr(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public TimePicker setPickerListener(PickerListener pickerListener) {
        this.pickerListener = pickerListener;
        return this;
    }
}
