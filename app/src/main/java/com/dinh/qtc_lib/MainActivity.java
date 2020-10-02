package com.dinh.qtc_lib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Filter;
import android.widget.Toast;

import com.dinh.api.APIService;
import com.dinh.api.APIUntil;
import com.dinh.dinhlibrary.edittext.FormattedEditText;
import com.dinh.dinhlibrary.searchdialog.SimpleSearchDialogCompat;
import com.dinh.dinhlibrary.searchdialog.core.BaseFilter;
import com.dinh.dinhlibrary.searchdialog.core.BaseSearchDialogCompat;
import com.dinh.dinhlibrary.searchdialog.core.SearchResultListener;
import com.dinh.dinhlibrary.searchdialog.core.Searchable;
import com.dinh.model.Product;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FormattedEditText formattedEditText_simple;
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = APIUntil.getServer();
        formattedEditText_simple = findViewById(R.id.formattedEditText_simple);
        provideSimpleDialogWithApiCalls();


        formattedEditText_simple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(MainActivity.this, ""+formattedEditText_simple.getRealText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void provideSimpleDialogWithApiCalls() {
        final SimpleSearchDialogCompat<Product> searchDialog =
                new SimpleSearchDialogCompat(MainActivity.this, "Tìm kiếm...",
                        "Nhập tên sản phẩm?", null, new ArrayList(),
                        new SearchResultListener<Searchable>() {
                            @Override
                            public void onSelected(
                                    BaseSearchDialogCompat dialog,
                                    Searchable item, int position
                            ) {
                                Toast.makeText(MainActivity.this, item.getTitle(),
                                        Toast.LENGTH_SHORT
                                ).show();
                                dialog.dismiss();
                            }
                        }
                );
        BaseFilter apiFilter = new BaseFilter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                doBeforeFiltering();
                Filter.FilterResults results = new Filter.FilterResults();
                results.values = new ArrayList<Product>();
                results.count = 0;
                try {
                    ArrayList<Product> users = (ArrayList<Product>) apiService.searchProduct(charSequence.toString())
                            .execute().body();
                    if (users != null) {
                        results.values = users;
                        results.count = users.size();
                    }
                    return results;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                if (filterResults != null) {
                    ArrayList<Product> filtered = (ArrayList<Product>) filterResults.values;
                    if (filtered != null) {
                        searchDialog.getFilterResultListener().onFilter(filtered);
                    }
                    doAfterFiltering();
                }
            }
        };
        searchDialog.setFilter(apiFilter);
        searchDialog.show();
    }
}