package com.nevermore.mapasignala.ui;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface Settings {
    @DefaultString("60")
    String interval();

    @DefaultBoolean(false)
    boolean metered();
}
