package com.nevermore.mapasignala.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

/**
 * https://stackoverflow.com/a/27046784
 * String hacks are for AndroidAnnotations compatibility
 */
public class NumberPreference extends DialogPreference {

    private NumberPicker picker;
    private int value;

    public NumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);
        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);
        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(5);
        picker.setMaxValue(1200);
        picker.setWrapSelectorWheel(true);
        picker.setValue(this.value);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            String newValue = String.valueOf(picker.getValue());
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return String.valueOf(a.getInt(index, 30));
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedString("1200") : String.valueOf(defaultValue));
    }

    void setValue(String value) {
        this.value = Integer.parseInt(value);
        persistString(String.valueOf(this.value));
    }

}
