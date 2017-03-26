package com.webandrioz.scopeafterug.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webandrioz.scopeafterug.R;
import com.webandrioz.scopeafterug.adapters.BookListViewAdapter;
import com.webandrioz.scopeafterug.constants.Constants;
import com.webandrioz.scopeafterug.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooksActivity extends BaseActivity {
    private  final String TAG = getClass().getName();
    ListView listView;
    ArrayList<Book> book=new ArrayList<>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        listView= (ListView) findViewById(R.id.bookListView);
        id=getIntent().getStringExtra("id");
        getBooks(id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Books");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getBooks(final String id){
        String REGISTER_URL= Constants.BASE_URL+ Constants.BOOKS;
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response );
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("success").equals("1")){
                                dialog.dismiss();
                                JSONArray jsonArray=jsonObject.getJSONArray("books");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    book.add(new Book(jsonObject1.getString("books_name"),jsonObject1.getString("author"),jsonObject1.getString("link")));
                                }

                                listView.setAdapter(new BookListViewAdapter(BooksActivity.this,book));
                            }else{
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: "+e.toString() );
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BooksActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onErrorResponse: "+error.toString());
                    }
                }){


            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
//                /params.put("name",name);
                params.put("id",id);
//                params.put("email", email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
