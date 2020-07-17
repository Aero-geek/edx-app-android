package org.edx.mobile.base;

import com.android.volley.toolbox.StringRequest;

import org.edx.mobile.view.ExtensionRegistry;

import javax.inject.Inject;

/**
 * Put any custom application configuration here.
 * This file will not be edited by edX unless absolutely necessary.
 */
public class RuntimeApplication extends MainApplication {

    @SuppressWarnings("unused")
    @Inject
    ExtensionRegistry extensionRegistry;
    String emeil;

    @Override
    public void onCreate() {
        super.onCreate();
        // If you have any custom extensions, add them here. For example:
        // extensionRegistry.forType(SettingsExtension.class).add(new MyCustomSettingsExtension());
    }

    public String getEmeil() {
        return emeil;
    }

    public void setEmeil(String emeil) {
        this.emeil = emeil;
    }
}
