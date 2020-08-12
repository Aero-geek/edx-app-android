package org.edx.mobile.view.view_holders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.edx.mobile.R;
import org.edx.mobile.VolleySingleton;
import org.edx.mobile.view.adapters.MpesaAdapter;
import org.edx.mobile.view.custom.MyListData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mpesaSubscriptionActivity extends Activity {
    public List<MyListData> myListData = new ArrayList<>();

    MpesaAdapter adapter;
    RecyclerView rvCouselist;
    private ProgressBar spinner;
    Button tvConfirm, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_subscription);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);

        rvCouselist = (RecyclerView) findViewById(R.id.rvCouselist);
        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);

        spinner.setVisibility(View.VISIBLE);
        callProductsWP();

        adapter = new MpesaAdapter(myListData, mpesaSubscriptionActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCouselist.setLayoutManager(layoutManager);
        rvCouselist.setAdapter(adapter);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mpesaSubscriptionActivity.this, PaymentMainActivity.class));
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void callProductsWP() {

        final String url = "https://smartedoo.co.ke/wp-json/wc/v2/products";

        // prepare the Request
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.e("Health data", response.toString());
                        spinner.setVisibility(View.GONE);


                        try {
                            JSONArray data = new JSONArray(response);

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonobj_response = data.getJSONObject(i);

                                String id = jsonobj_response.getString("id");

                                String name = jsonobj_response.getString("name");
                                String price = jsonobj_response.getString("price");
                                String short_description = jsonobj_response.getString("short_description");
                                String image = jsonobj_response.getJSONArray("images").getJSONObject(0).getString("src");


                                myListData.add(new MyListData(name + "\n" + android.text.Html.fromHtml(short_description).toString()
                                        , price, image, name));


                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        spinner.setVisibility(View.GONE);

                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                String auth = "Basic " + "Y2tfMWM4OGRiZjY0MzgxYjM4ZWY0MjViMDkxYzM5Zjc2MzA3M2ExYzlhNjpjc19hNGMxNmQ0YTVjZjNjMDlhNDdlZWRkZjNjNDlhZjJiZmQxZDJmZjAw";
                headers.put("Authorization", auth);
                return headers;
            }


        };

        // add it to the RequestQueue
        req.setRetryPolicy(new DefaultRetryPolicy(20000, -1, 0));

        VolleySingleton.getInstance(this).addToRequestQueue(req);


    }

}