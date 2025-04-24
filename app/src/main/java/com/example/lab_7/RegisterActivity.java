package com.example.lab_7;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Registration";
    private static final List<String> COUNTRIES = Arrays.asList(
            "Россия", "Беларусь", "Казахстан", "Украина", "Другая страна"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Настройка выпадающего списка стран
        AutoCompleteTextView countryInput = findViewById(R.id.countryInput);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                COUNTRIES
        );
        countryInput.setAdapter(adapter);

        // Обработка нажатия кнопки регистрации
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> validateAndRegister());
    }

    private void validateAndRegister() {
        boolean isValid = true;

        // Валидация ФИО
        TextInputEditText fioInput = findViewById(R.id.fioInput);
        TextInputLayout fioLayout = findViewById(R.id.fioLayout);
        String fio = fioInput.getText().toString().trim();
        if (!fio.matches("[А-Яа-яёЁ\\s-]+")) {
            fioLayout.setError("Только кириллические буквы, пробелы и дефис");
            logError("ФИО", fio);
            isValid = false;
        } else {
            fioLayout.setError(null);
        }

        // Валидация логина
        TextInputEditText loginInput = findViewById(R.id.loginInput);
        TextInputLayout loginLayout = findViewById(R.id.loginLayout);
        String login = loginInput.getText().toString().trim();
        if (!login.matches("[a-zA-Z]+")) {
            loginLayout.setError("Только латинские буквы");
            logError("Логин", login);
            isValid = false;
        } else {
            loginLayout.setError(null);
        }

        // Валидация email
        TextInputEditText emailInput = findViewById(R.id.emailInput);
        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        String email = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Некорректный email");
            logError("Email", email);
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Валидация телефона
        TextInputEditText phoneInput = findViewById(R.id.phoneInput);
        TextInputLayout phoneLayout = findViewById(R.id.phoneLayout);
        String phone = phoneInput.getText().toString().trim();
        if (!phone.matches("\\+\\d{11,15}")) {
            phoneLayout.setError("Формат: +код страны номер");
            logError("Телефон", phone);
            isValid = false;
        } else {
            phoneLayout.setError(null);
        }

        // Валидация пароля
        TextInputEditText passwordInput = findViewById(R.id.passwordInput);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        String password = passwordInput.getText().toString();
        if (password.length() < 6) {
            passwordLayout.setError("Минимум 6 символов");
            logError("Пароль", "Слишком короткий");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        // Проверка совпадения паролей
        TextInputEditText repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
        TextInputLayout repeatPasswordLayout = findViewById(R.id.repeatPasswordLayout);
        String repeatPassword = repeatPasswordInput.getText().toString();
        if (!password.equals(repeatPassword)) {
            repeatPasswordLayout.setError("Пароли не совпадают");
            logError("Повтор пароля", "Не совпадает");
            isValid = false;
        } else {
            repeatPasswordLayout.setError(null);
        }

        // Валидация даты рождения
        TextInputEditText birthDateInput = findViewById(R.id.birthDateInput);
        TextInputLayout birthDateLayout = findViewById(R.id.birthDateLayout);
        String birthDateStr = birthDateInput.getText().toString().trim();
        if (!isValidDate(birthDateStr)) {
            birthDateLayout.setError("Формат: дд.мм.гггг, не ранее 1900");
            logError("Дата рождения", birthDateStr);
            isValid = false;
        } else {
            birthDateLayout.setError(null);
        }

        // Проверка выбора страны
        AutoCompleteTextView countryInput = findViewById(R.id.countryInput);
        TextInputLayout countryLayout = findViewById(R.id.countryLayout);
        String country = countryInput.getText().toString().trim();
        if (country.isEmpty()) {
            countryLayout.setError("Выберите страну");
            logError("Страна", "Не выбрана");
            isValid = false;
        } else {
            countryLayout.setError(null);
        }

        // Проверка согласия
        CheckBox agreementCheckbox = findViewById(R.id.agreementCheckbox);
        if (!agreementCheckbox.isChecked()) {
            Toast.makeText(this, "Необходимо согласие на обработку данных", Toast.LENGTH_SHORT).show();
            logError("Согласие", "Не получено");
            isValid = false;
        }

        // Если все проверки пройдены
        if (isValid) {
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Успешная регистрация: " + email);
            // Здесь можно добавить сохранение данных или переход на другой экран
        }
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateStr);
            if (date == null) return false;

            // Проверка что дата не ранее 1900 года
            Date minDate = sdf.parse("01.01.1900");
            return !date.before(minDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private void logError(String field, String value) {
        Log.e(TAG, "Ошибка валидации: " + field + " - " + value);
    }
}