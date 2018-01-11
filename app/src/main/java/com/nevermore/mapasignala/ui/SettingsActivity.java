package com.nevermore.mapasignala.ui;

import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.nevermore.mapasignala.R;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;
import org.androidannotations.annotations.sharedpreferences.Pref;

@PreferenceScreen(R.xml.preferences)
@EActivity
public class SettingsActivity extends PreferenceActivity {

    @Pref
    protected Settings_ settings;

    @PreferenceByKey(R.string.key_metered)
    protected SwitchPreference metered;

    @PreferenceByKey(R.string.key_interval)
    protected NumberPreference interval;

    @AfterPreferences
    protected void init() {
        metered.setChecked(settings.metered().get());
        interval.setValue(settings.interval().get());
        getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup)findViewById(android.R.id.content));
        int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int verticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        TypedValue val = new TypedValue();
        getTheme().resolveAttribute(R.attr.actionBarSize, val, true);
        int topMargin = (int) val.getDimension(getResources().getDisplayMetrics());
        getListView().setPadding(horizontalMargin, topMargin, horizontalMargin, verticalMargin);

    }

    @PreferenceChange(R.string.key_metered)
    protected void setMetered(boolean value) {
        settings.metered().put(value);
    }

    @PreferenceChange(R.string.key_interval)
    protected void setInterval(String value) {
        settings.interval().put(value);
    }

}
