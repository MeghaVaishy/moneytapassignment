package com.example.megha.myapplication.activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megha.myapplication.Pojo.SearchPojo;
import com.example.megha.myapplication.R;
import com.example.megha.myapplication.utils.CommonUtils;
import com.example.megha.myapplication.adapter.SearchAdapter;
import com.example.megha.myapplication.database.SampleDatabase;
import com.example.megha.myapplication.database.SearchResultEntity;
import com.example.megha.myapplication.model.Page;
import com.example.megha.myapplication.model.WikiSearchResponse;
import com.example.megha.myapplication.rest.ApiClient;
import com.example.megha.myapplication.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchAdapter.itemListListner {

    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    private String serachText;
    private RecyclerView recyclerView;
    Handler handler = new Handler();
    private EditText editText;
    ApiInterface apiService;
    List<SearchPojo> searchlist = new ArrayList<SearchPojo>();
    private SearchAdapter searchAdapter;
    String description;
    String title;
    private SampleDatabase sampleDatabase;
    private AsyncTask insertDataAsync;
    private fetchDtaAsyncTask fetchDataAsync;
    private TextView noResult;
    private ImageView clear;
    public final static String Empty = "";
    List<SearchResultEntity> list;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        editText = findViewById(R.id.search);
        recyclerView = findViewById(R.id.resultcontainer);
        noResult = findViewById(R.id.no_results);
        clear = findViewById(R.id.btn_clear);
        //database instance
        sampleDatabase = Room.databaseBuilder(getApplicationContext(),
                SampleDatabase.class, "sample-db").build();
        //setRecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        searchAdapter = new SearchAdapter(searchlist, this, this);
        recyclerView.setAdapter(searchAdapter);
        // edit text listener
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No Implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    serachText = s.toString();
                    handler.postDelayed(input_finish_checker, delay);
                } else {
                    searchlist.clear();
                    searchAdapter.notifyDataSetChanged();
                    noResult.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(Empty);
                searchlist.clear();
                searchAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                if (CommonUtils.isNetworkAvailable(MainActivity.this)) {
                    serchAPi();
                } else {
                    try {
                        recyclerView.setVisibility(View.VISIBLE);
                        noResult.setVisibility(View.GONE);
                        fetchDataAsync = new fetchDtaAsyncTask();
                        list = fetchDataAsync.execute().get();
                        if (list.size() > 0) {
                            searchlist.clear();
                            for (SearchResultEntity entity : list) {
                                SearchPojo pojo = new SearchPojo(entity.imageUrl, entity.title, entity.description);
                                searchlist.add(pojo);
                            }
                            searchAdapter.notifyDataSetChanged();
                        } else {
                            noResult.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void serchAPi() {
        Call<WikiSearchResponse> call = apiService.getSerachResult("query", "json", "pageimages|pageterms", "prefixsearch", "1", "2",
                "thumbnail", "50", "10", "description", serachText);
        call.enqueue(new Callback<WikiSearchResponse>() {
            @Override
            public void onResponse(Call<WikiSearchResponse> call, Response<WikiSearchResponse> response) {
                searchlist.clear();
                if (response.body() != null && response.body().getQuery() != null) {
                    List<Page> pages = new ArrayList<Page>();
                    if (response.body().getQuery().getPages() != null) {
                        pages = response.body().getQuery().getPages();

                        if (pages.size() > 0) {
                            for (Page page : pages) {
                                if (page.getTerms() != null && page.getTerms().getDescription() != null) {
                                    description = page.getTerms().getDescription().get(0);
                                }
                                if (page.getTitle() != null) {
                                    title = page.getTitle();
                                }
                                String imageUrl = "";
                                if (page.getThumbnail() != null) {
                                    imageUrl = page.getThumbnail().getSource();
                                }

                                SearchPojo itempojo = new SearchPojo(imageUrl, title, description);
                                searchlist.add(itempojo);
                            }
                            try {
                                fetchDataAsync = new fetchDtaAsyncTask();
                                List<SearchResultEntity> list = fetchDataAsync.execute().get();
                                if (list.size() == 0) {
                                    insertDataAsync = new InsertDataAsync().execute();
                                }


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            searchAdapter.notifyDataSetChanged();

                        }
                    } else {
                        noresultView();
                    }
                } else {
                    noresultView();
                }
            }

            @Override
            public void onFailure(Call<WikiSearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void noresultView() {
        noResult.setVisibility(View.VISIBLE);
        noResult.setText(getResources().getString(R.string.no_results_found));
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void navigateToWebPage(String title) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }


    private class InsertDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            List<SearchResultEntity> entitylist = new ArrayList<SearchResultEntity>();
            for (SearchPojo item : searchlist) {
                SearchResultEntity entity = new SearchResultEntity(item.getTitle(), item.getDescription(), item.getImageURL());
                entitylist.add(entity);
            }
            sampleDatabase.daoAccess().insertMultipleListRecord(entitylist);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (insertDataAsync != null)//cancel asynctask
                insertDataAsync.cancel(true);
            Toast.makeText(MainActivity.this, " Results Inserted successfuly", Toast.LENGTH_SHORT).show();
        }
    }


    private class fetchDtaAsyncTask extends AsyncTask<Void, Void, List<SearchResultEntity>> {

        @Override
        protected List<SearchResultEntity> doInBackground(Void... voids) {
            String text = "%" + serachText + "%";
            List<SearchResultEntity> entitylist = sampleDatabase.daoAccess().getRecords(text);
            return entitylist;
        }

        @Override
        protected void onPostExecute(List<SearchResultEntity> searchResultEntities) {
            super.onPostExecute(searchResultEntities);
            if (fetchDataAsync != null) {
                fetchDataAsync.cancel(true);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (insertDataAsync != null)//cancel asynctask
            insertDataAsync.cancel(true);

        if (fetchDataAsync != null) {
            fetchDataAsync.cancel(true);
        }
    }
}
