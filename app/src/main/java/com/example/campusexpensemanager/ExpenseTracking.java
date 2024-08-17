package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.DatabaseSQLite.ExpenseDB;
import com.example.campusexpensemanager.Expense.Expense;
import com.example.campusexpensemanager.Expense.ExpenseAdapter;

import java.util.ArrayList;
import java.util.List;


public class ExpenseTracking extends AppCompatActivity {

    private TextView textnoexpenses;
    private Button buttonaddexpense;
    private TextView texttotalexpense;
    private RecyclerView recycleviewexpense;
    private ExpenseAdapter expenseAdapter;
    private ListView listView;
    private List<Expense> expenseList;
    private Button buttonback;

    private ExpenseDB dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expensetracking_activity);

        textnoexpenses = findViewById(R.id.text_no_expenses);
        buttonaddexpense = findViewById(R.id.button_add_expense);
        texttotalexpense = findViewById(R.id.text_total_expenses);
        recycleviewexpense = findViewById(R.id.recycler_view_expenses);

        dbHelper = new ExpenseDB(this);

        recycleviewexpense.setLayoutManager(new LinearLayoutManager(this));
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, expenseList);
        recycleviewexpense.setAdapter(expenseAdapter);
        buttonback = findViewById(R.id.button_back);

        dbHelper = new ExpenseDB(this);

        buttonaddexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseTracking.this, AddExpenseActivity.class);
                startActivity(intent) ;
            }
        });

        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseTracking.this, MainActivity.class);
                startActivity(intent) ;
            }
        });

        loadExpense();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpense();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadExpense() {
        List<Expense> expenses = new ArrayList<>();
        double totalAmount = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ExpenseDB.TABLE_EXPENSES,
                null,
                null,
                null,
                null,
                null,
                null
        );
        expenseList.clear();

        if (cursor.getCount() == 0) {
            textnoexpenses.setVisibility(View.VISIBLE);
            recycleviewexpense.setVisibility(View.GONE);
        } else {
            textnoexpenses.setVisibility(View.GONE);
            recycleviewexpense.setVisibility(View.VISIBLE);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseDB.COLUMM_ID));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDB.COLUMM_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDB.COLUMM_DATE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseDB.COLUMM_AMOUNT));

                Expense expense = new Expense(id, description, date, amount);
                expenses.add(expense);
                totalAmount += amount;
            }

            expenseAdapter.setExpenses(expenses);

        }

        texttotalexpense.setText("Total: " + totalAmount + " VND");
        cursor.close();

        expenseList.clear();
        expenseList.addAll(expenses);
        expenseAdapter.setExpenses(expenseList);
        expenseAdapter.notifyDataSetChanged();

    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ExpenseDB.TABLE_EXPENSES, ExpenseDB.COLUMM_ID + " = ?", new String[]{String.valueOf(expenseId)});
        loadExpense();
    }


}
