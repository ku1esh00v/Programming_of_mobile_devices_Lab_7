package com.example.lab_7;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateDeviceList();

        // Кнопка смены языка
        Button btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnChangeLanguage.setOnClickListener(v -> toggleLanguage());

        // Новая кнопка перехода на регистрацию
        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void toggleLanguage() {
        Locale currentLocale = getResources().getConfiguration().locale;

        if (currentLocale.getLanguage().equals("de")) {
            setLocale("ru");
        } else {
            setLocale("de");
        }

        recreate();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void updateDeviceList() {
        ListView listView = findViewById(R.id.listViewDevices);
        String[] devices = getResources().getStringArray(R.array.peripheral_devices);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                devices
        );

        listView.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(updateBaseContextLocale(newBase));
    }

    private Context updateBaseContextLocale(Context context) {
        Locale locale = Locale.getDefault();
        Locale.setDefault(locale);

        Resources res = context.getResources();
        android.content.res.Configuration config = res.getConfiguration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}