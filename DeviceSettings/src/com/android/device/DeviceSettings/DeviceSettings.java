/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.android.device.DeviceSettings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import com.android.device.DeviceSettings.ModeSwitch.DCModeSwitch;
import com.android.device.DeviceSettings.ModeSwitch.HBMModeSwitch;

public class DeviceSettings extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String KEY_SRGB_SWITCH = "srgb";
    public static final String KEY_HBM_SWITCH = "hbm";
    public static final String KEY_DC_SWITCH = "dc";
    public static final String KEY_DCI_SWITCH = "dci";
    public static final String KEY_NIGHT_SWITCH = "night";
    public static final String KEY_ADAPTIVE_SWITCH = "adaptive";
    public static final String KEY_ONEPLUS_SWITCH = "oneplus";
    public static final String KEY_FPS_INFO = "fps_info";
    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_SETTINGS_PREFIX = "device_setting_";
    public static final String KEY_BUTTON_SWAP_CATEGORY = "button_swap_category";
    public static final String KEY_BUTTON_SWAP = "button_swap";
    public static final String KEY_CHG_VOLTAGE_CHECK = "disableChargerVoltageCheck";

    private static final boolean sIsOnePlus5t = android.os.Build.DEVICE.equals("OnePlus5T");
    private PreferenceCategory mButtonSwapCategory;
    private SwitchPreference mButtonSwap;
    private SwitchPreference mChgVoltageCheck;
    private SwitchPreference mDCModeSwitch;
    private SwitchPreference mHBMModeSwitch;
    private SwitchPreference mFpsInfo;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main);

        mDCModeSwitch = findPreference(KEY_DC_SWITCH);
        mDCModeSwitch.setEnabled(DCModeSwitch.isSupported());
        mDCModeSwitch.setChecked(DCModeSwitch.isCurrentlyEnabled());
        mDCModeSwitch.setOnPreferenceChangeListener(new DCModeSwitch());

        mHBMModeSwitch = findPreference(KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled());
        mHBMModeSwitch.setOnPreferenceChangeListener(new HBMModeSwitch());

        mFpsInfo = findPreference(KEY_FPS_INFO);
        mFpsInfo.setChecked(isFPSOverlayRunning());
        mFpsInfo.setOnPreferenceChangeListener(this);

        if (!sIsOnePlus5t) {
            mButtonSwap = findPreference(KEY_BUTTON_SWAP);
            mButtonSwap.setEnabled(ButtonSwap.isSupported());
            mButtonSwap.setChecked(ButtonSwap.isCurrentlyEnabled());
            mButtonSwap.setOnPreferenceChangeListener(new ButtonSwap());
        } else {
            mButtonSwapCategory = findPreference(KEY_BUTTON_SWAP_CATEGORY);
            mButtonSwapCategory.setVisible(false);
        }

        mChgVoltageCheck = findPreference(KEY_CHG_VOLTAGE_CHECK);
        mChgVoltageCheck.setEnabled(ChgVoltageCheck.isSupported());
        mChgVoltageCheck.setChecked(ChgVoltageCheck.isCurrentlyEnabled());
        mChgVoltageCheck.setOnPreferenceChangeListener(new ChgVoltageCheck());
    }

    @Override
    public void onResume() {
        super.onResume();
        mFpsInfo.setChecked(isFPSOverlayRunning());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFpsInfo) {
            boolean enabled = (Boolean) newValue;
            Intent fpsinfo = new Intent(getContext(), FPSInfoService.class);
            if (enabled) {
                getContext().startService(fpsinfo);
            } else {
                getContext().stopService(fpsinfo);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFPSOverlayRunning() {
        ActivityManager am = (ActivityManager) getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                am.getRunningServices(Integer.MAX_VALUE))
            if (FPSInfoService.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
   }
}
