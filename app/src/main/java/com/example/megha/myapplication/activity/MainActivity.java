package com.example.megha.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.megha.myapplication.R;
import com.example.megha.myapplication.model.WikiSearchResponse;
import com.example.megha.myapplication.rest.ApiClient;
import com.example.megha.myapplication.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    private EditText editText;
    ApiInterface apiService ;
    String serachText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    serachText=s.toString();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });


        apiService= ApiClient.getClient().create(ApiInterface.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                Call<WikiSearchResponse> call = apiService.getSerachResult("query","json","pageimages|pageterms","prefixsearch","1","2",
                        "thumbnail","50","10","description" ,serachText);
                call.enqueue(new Callback<WikiSearchResponse>() {


                    @Override
                    public void onResponse(Call<WikiSearchResponse> call, Response<WikiSearchResponse> response) {
                        boolean value=response.body().getBatchcomplete();
                    }

                    @Override
                    public void onFailure(Call<WikiSearchResponse> call, Throwable t) {

                    }
                });


            }
        }
    };
}
