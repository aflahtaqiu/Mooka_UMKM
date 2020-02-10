package com.example.mooka_umkm.custom;

import android.util.Log;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by aflah on 11/02/20
 * Email  : aflahtaqiusondha@gmail.com
 * Github : https://github.com/aflahtaqiu
 */


public class DaysWeeksFormatter extends ValueFormatter {

    private final String[] daysIndonesia = new String[]{
            "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
    };

    @Override
    public String getFormattedValue(float value) {

        return daysIndonesia[((int)value)-1];
    }
}
