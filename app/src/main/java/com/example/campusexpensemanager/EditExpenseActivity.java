package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.ExpenseDB;

public class EditExpenseActivity extends AppCompatActivity {
    private EditText editDescription;
    private EditText editDate;
    private EditText editAmount;
    private Button btnSave;

    private ExpenseDB dbHepler;
    private int expenseId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_expense_activity);

        editDescription = findViewById(R.id.edit_description);
        editDate = findViewById(R.id.edit_date);
        editAmount = findViewById(R.id.edit_amount);
        btnSave = findViewById(R.id.button_save);

        dbHepler = new ExpenseDB(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("expense_id")) {
            expenseId = intent.getIntExtra("expense_id", -1);
            loadExpenseData(expenseId);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
                finish();
            }
        });
    }

    private void loadExpenseData(int expenseId) {
        SQLiteDatabase db = dbHepler.getReadableDatabase();
        Cursor cursor = db.query(ExpenseDB.TABLE_EXPENSES, null, ExpenseDB.COLUMM_ID + " = ?", new String[]{String.valueOf(expenseId)}, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(ExpenseDB.COLUMM_DESCRIPTION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(ExpenseDB.COLUMM_DATE));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(ExpenseDB.COLUMM_AMOUNT));

            editDescription.setText(description);
            editDate.setText(date);
            editAmount.setText(String.valueOf(amount));
        }

        cursor.close();
    }

    private void saveExpense() {
        String description = editDescription.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        double amount = 0.0;

        try {
            amount = Double.parseDouble(editAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu giá trị không phải là số hợp lệ
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHepler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseDB.COLUMM_DESCRIPTION, description);
        values.put(ExpenseDB.COLUMM_DATE, date);
        values.put(ExpenseDB.COLUMM_AMOUNT, amount);

        if (expenseId == -1) {
            db.insert(ExpenseDB.TABLE_EXPENSES, null, values);
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        } else {
            db.update(ExpenseDB.TABLE_EXPENSES, values, ExpenseDB.COLUMM_ID + " = ?", new String[]{String.valueOf(expenseId)});
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        }
    }

}
