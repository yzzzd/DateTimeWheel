package com.nuryazid.wheelpicker.ui;

import android.content.Context;
import android.view.View;

import com.nuryazid.wheelpicker.R;
import com.nuryazid.wheelpicker.util.OnWheelChangeListener;
import com.nuryazid.wheelpicker.util.WheelPicker;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatePicker {

    private WheelPicker dateWheel;
    private WheelPicker monthWheel;
    private WheelPicker yearWheel;

    private int datePos = 0;
    private int monthPos = 0;
    private int yearPos = 0;

    private List<String> dateList = new ArrayList<String>();
    private List<String> monthList = new ArrayList<String>();
    private List<String> yearList = new ArrayList<String>();

    private PickerListener pickerListener;

    private DialogPlus dialogPicker;

    private Calendar maxDay = null;
    private Calendar minDay = null;

    public DatePicker(Context context) {

        initValue();

        dialogPicker = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_date_picker))
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        int i = view.getId();
                        if (i == R.id.btnCancel) {
                            dialog.dismiss();
                        } else if (i == R.id.btnSave) {
                            dialog.dismiss();
                            String strDateLocal = yearList.get(yearWheel.getCurrentItemPosition())
                                    + "-"
                                    + format2LenStr(month(monthList.get(monthWheel.getCurrentItemPosition())))
                                    + "-"
                                    + format2LenStr(Integer.parseInt(dateList.get(dateWheel.getCurrentItemPosition())));

                            if (pickerListener != null) {
                                pickerListener.dataSet(strDateLocal);
                            }
                        }
                    }
                })
                .create();

        View view = dialogPicker.getHolderView();

        yearWheel = view.findViewById(R.id.yearWheel);
        monthWheel = view.findViewById(R.id.monthWheel);
        dateWheel = view.findViewById(R.id.dateWheel);

        yearWheel.setData(yearList);
        monthWheel.setData(monthList);
        dateWheel.setData(dateList);

        yearWheel.setSelectedItemPosition(yearPos);
        monthWheel.setSelectedItemPosition(monthPos);
        dateWheel.setSelectedItemPosition(datePos);

        yearWheel.setOnWheelChangeListener(new OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int offset) {

            }

            @Override
            public void onWheelSelected(int position) {

            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (month(monthList.get(monthWheel.getCurrentItemPosition())) == 2) {
                    invalidateTotalDate(Integer.parseInt(yearList.get(yearWheel.getCurrentItemPosition())), month(monthList.get(monthWheel.getCurrentItemPosition())));
                }
            }
        });

        monthWheel.setOnWheelChangeListener(new OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int offset) {

            }

            @Override
            public void onWheelSelected(int position) {

            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                invalidateTotalDate(Integer.parseInt(yearList.get(yearWheel.getCurrentItemPosition())), month(monthList.get(monthWheel.getCurrentItemPosition())));
            }
        });

    }

    private void invalidateTotalDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        String dateCurrent = dateList.get(dateWheel.getCurrentItemPosition());

        dateList.clear();

        for (int i = 1; i <= numDays; i++) {
            dateList.add(format2LenStr(i));
        }

        dateWheel.setData(dateList);

        if (Integer.parseInt(dateCurrent) > numDays) {
            dateCurrent = Integer.toString(numDays);
            dateWheel.setSelectedItemPosition(dateList.indexOf(dateCurrent), true);
        }

    }

    public DatePicker setLoop() {
        yearWheel.setCyclic(true);
        monthWheel.setCyclic(true);
        dateWheel.setCyclic(true);
        return this;
    }

    public DatePicker maxDay(Calendar date) {
        maxDay = date;

        int yearRange = 100;
        if (minDay != null) {
            yearRange = Math.abs(maxDay.get(Calendar.YEAR) - minDay.get(Calendar.YEAR));
        }

        yearList.clear();
        for (int i = maxDay.get(Calendar.YEAR) - yearRange; i <= maxDay.get(Calendar.YEAR); i++) {
            yearList.add(Integer.toString(i));
        }

        yearWheel.setData(yearList);

        String intY = dateToday().split(":")[0];

        if (Integer.parseInt(intY) > maxDay.get(Calendar.YEAR)) {
            yearPos = yearList.size() - 1;
        } else {
            yearPos = yearList.indexOf(intY);
        }
        yearWheel.setSelectedItemPosition(yearPos);

        return this;
    }

    public DatePicker minDay(Calendar date) {
        minDay = date;

        int yearRange = 100;
        if (maxDay != null) {
            yearRange = Math.abs(maxDay.get(Calendar.YEAR) - minDay.get(Calendar.YEAR));
        }

        yearList.clear();
        for (int i = minDay.get(Calendar.YEAR); i <= minDay.get(Calendar.YEAR) + yearRange; i++) {
            yearList.add(Integer.toString(i));
        }

        yearWheel.setData(yearList);

        String intY = dateToday().split(":")[0];
        yearPos = yearList.indexOf(intY);
        yearWheel.setSelectedItemPosition(yearPos);

        return this;
    }

    public DatePicker show() {
        dialogPicker.show();
        return this;
    }

    private void initValue() {
        String intY = dateToday().split(":")[0];
        String intM = dateToday().split(":")[1];
        String strD = dateToday().split(":")[2];

        int yearRange = 100;

        for (int i = -(yearRange); i < yearRange * 2; i++) {
            int y = Integer.parseInt(intY) + i;
            yearList.add(Integer.toString(y));
        }

        for (int i = 1; i <= 12; i++) {
            monthList.add(month(i));
        }

        for (int i = 1; i <= 30; i++) {
            dateList.add(Integer.toString(i));
        }

        yearPos = yearList.indexOf(intY);
        monthPos = monthList.indexOf(intM);
        datePos = dateList.indexOf(strD);
    }

    private static String dateToday() {
        DateFormat formatter = new SimpleDateFormat("yyyy:MMMM:d", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    private String month(int month) {

        DateFormat formatter = new SimpleDateFormat("M", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(Integer.toString(month));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            DateFormat formatter_show = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            return formatter_show.format(date);
        }

        return "";
    }

    private int month(String month) {

        DateFormat formatter = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            DateFormat formatter_show = new SimpleDateFormat("M", Locale.ENGLISH);
            return Integer.parseInt(formatter_show.format(date));
        }

        return 0;
    }

    private static String format2LenStr(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public DatePicker setPickerListener(PickerListener pickerListener) {
        this.pickerListener = pickerListener;
        return this;
    }
}
