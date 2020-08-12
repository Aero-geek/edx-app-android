package org.edx.mobile.base;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.edx.mobile.view.ExtensionRegistry;
import org.edx.mobile.view.common.AppDatabase;
import org.edx.mobile.view.custom.MyListData;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;

/**
 * Put any custom application configuration here.
 * This file will not be edited by edX unless absolutely necessary.
 */
public class RuntimeApplication extends MainApplication {

    @SuppressWarnings("unused")
    @Inject
    ExtensionRegistry extensionRegistry;
    String emeil;
    List<MyListData> list_of_animals = new ArrayList<MyListData>();


    @Override
    public void onCreate() {
        super.onCreate();

        // If you have any custom extensions, add them here. For example:
        // extensionRegistry.forType(SettingsExtension.class).add(new MyCustomSettingsExtension());
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N) {
            initializeSSLContext(this);

        }

    }

    public void initializeSSLContext(Context mContext) {
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public String getEmeil() {
        return emeil;
    }

    public void setEmeil(String emeil) {
        this.emeil = emeil;
    }

    public List<MyListData> getList_of_animals() {
        return list_of_animals;
    }

    public void setList_of_animals(List<MyListData> list_of_animals) {
        this.list_of_animals = list_of_animals;
    }
}
