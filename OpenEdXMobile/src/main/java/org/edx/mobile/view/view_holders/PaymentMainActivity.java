package org.edx.mobile.view.view_holders;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.edx.mobile.R;
import org.edx.mobile.VolleySingleton;
import org.edx.mobile.base.RuntimeApplication;
import org.edx.mobile.view.MainDashboardActivity;
import org.edx.mobile.view.common.AppDatabase;
import org.edx.mobile.view.common.LocalCourse;
import org.edx.mobile.view.common.LocalCourseDao;
import org.edx.mobile.view.common.Users;
import org.edx.mobile.view.custom.MyListData;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class PaymentMainActivity extends Activity {
    RuntimeApplication app;
    TextView txt_name;
    List<MyListData> data = new ArrayList<>();
    EditText edphoneNumber;
    //Declare Daraja :: Global Variable
    Daraja daraja;

    String phoneNumber;
    Button button_okay;
    int amount = 0;
    private ProgressBar spinner;
    LocalCourseDao localCourseDao;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String token;
    TextView txt_header;
    Button button_close;
    StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_main);
        app = (RuntimeApplication) getApplicationContext();
        txt_name = findViewById(R.id.txt_name);
        button_okay = findViewById(R.id.button_okay);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("subscribedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        localCourseDao = db.userDao();
        localCourseDao.nukeTable();

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        edphoneNumber = findViewById(R.id.phoneNumber);
        button_close = findViewById(R.id.button_close);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        txt_header = findViewById(R.id.txt_header);

        //Init Daraja
        //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
        daraja = Daraja.with("fzZx0IyoeygA7adD2lD4WIWJaV3Op9i0", "n6v2jqFlQHslRlgn", Env.PRODUCTION, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(PaymentMainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
//                Toast.makeText(PaymentMainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
                token = accessToken.getAccess_token();
            }

            @Override
            public void onError(String error) {
                Log.e(PaymentMainActivity.this.getClass().getSimpleName(), error);
            }
        });

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMainActivity.this, MainDashboardActivity.class));
                finish();
            }
        });

        data = app.getList_of_animals();

        if (data.size() > 0) {

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                result.append(i + 1 + ". " + data.get(i).getName());
                amount = amount + Integer.parseInt(data.get(i).getPrice());
            }
            txt_name.setText(result.toString());

        } else {
            txt_name.setText("You did not pick any course");
            edphoneNumber.setVisibility(View.GONE);
            button_okay.setVisibility(View.GONE);


        }


        button_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = edphoneNumber.getText().toString().trim();
                edphoneNumber.setEnabled(false);

                if (phoneNumber.substring(0, 1).equals("0")) {
                    phoneNumber = "254" + phoneNumber.substring(1);

                } else {
                    phoneNumber = "254" + phoneNumber;
                }

                spinner.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(phoneNumber)) {
                    edphoneNumber.setError("Please Provide a Phone Number");
                    return;
                }

                //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
                LNMExpress lnmExpress = new LNMExpress(
                        "7116859",
                        "9623653c885e8f06a86f1616168b90729d0d7b9b34c793367745d7d06ba0b09d",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline,
                        String.valueOf(amount),
                        "254746303512",
                        "7116859",
                        phoneNumber,
                        "https://smartedoo.co.ke/checkout.php",
                        "Account",
                        "Course Subscription Payment"
                );

                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.e(PaymentMainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                                spinner.setVisibility(View.GONE);

                                Gson gson = new Gson();
                                String json = gson.toJson(app.getList_of_animals());

                                editor.putString("data", json);  // Saving string

                                editor.apply();


                                txt_header.setText("Checking payment");
                                txt_name.setText("please wait.......");
                                button_okay.setVisibility(View.GONE);
                                edphoneNumber.setVisibility(View.GONE);


                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
//                                        startActivity(new Intent(PaymentMainActivity.this, MainDashboardActivity.class));
//                                        finish();

                                        callCheckPayment(lnmResult.CheckoutRequestID);
//                                        callvolly(lnmResult.CheckoutRequestID);


                                    }
                                }, 20000);


//                                if (lnmResult.CheckoutRequestID.equalsIgnoreCase("0")) {
                                result = new StringBuilder();

                                for (int i = 0; i < data.size(); i++) {
                                    localCourseDao.insert(new LocalCourse(i, data.get(i).getJina()));
                                    result.append(",").append(data.get(i).getJina());

                                }





//                                } else {
//                                    Toast.makeText(PaymentMainActivity.this, "Failed: " + lnmResult.ResponseDescription, Toast.LENGTH_SHORT).show();
//                                    spinner.setVisibility(View.GONE);
//
//                                }


                            }

                            @Override
                            public void onError(String error) {
                                Log.i(PaymentMainActivity.this.getClass().getSimpleName(), error);
                            }
                        }
                );
            }
        });

    }

    private void callCheckPayment(String ref_key) {

        String api_url = "https://api.safaricom.co.ke/mpesa/stkpushquery/v1/query";

        HashMap<String, String> params = new HashMap<>();

        params.put("BusinessShortCode", "7116859");
        params.put("Password", "NzExNjg1OTk2MjM2NTNjODg1ZThmMDZhODZmMTYxNjE2OGI5MDcyOWQwZDdiOWIzNGM3OTMzNjc3NDVkN2QwNmJhMGIwOWQyMDIwMDQzMDE4Mzk0OQ==");
        params.put("Timestamp", "20200430183949");
        params.put("CheckoutRequestID", ref_key);


        Log.e("params", params.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, api_url, new JSONObject(params),

                (JSONObject response) -> {

                    try {


                        Log.e("Response", response.toString(4));


                        Users user = new Users(app.getEmeil(), result.toString());
                        String domain = app.getEmeil().split("@")[0];

                        String ResultCode = "";
                        String ResultDesc = "";
                        button_okay.setVisibility(View.GONE);
                        edphoneNumber.setVisibility(View.GONE);
                        for (int i = 0; i < response.length(); i++) {

                            ResultCode = response.getString("ResultCode");
                            ResultDesc = response.getString("ResultDesc");


                        }
                        if (ResultCode.equalsIgnoreCase("1032")) {
                            localCourseDao.nukeTable();
                            txt_name.setText("Failed");
                            txt_header.setText("" + ResultDesc);
                            txt_header.setTextColor(Color.RED);


                        } else {
                            txt_name.setText("Success");
                            txt_header.setText("" + ResultDesc);

                            mFirebaseDatabase.child(domain).setValue(user);

                        }
                        Toasty.error(getApplicationContext(), "" + ResultDesc.toString(), Toast.LENGTH_LONG, true).show();

                        button_close.setVisibility(View.VISIBLE);


                    } catch (JSONException e) {


                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("JsonResponse", String.valueOf(error.networkResponse.statusCode));

                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    Log.e("Eror", jsonObject.toString());
                    button_close.setVisibility(View.VISIBLE);


                    Toast.makeText(getApplicationContext(), " " + jsonObject.toString(), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Handle a malformed json response
                    Toast.makeText(getApplicationContext(), " " + e.toString(), Toast.LENGTH_LONG).show();

                }



            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");

                String auth = "Bearer " + token;
                Log.e("--", auth);
                headers.put("Authorization", auth);
                return headers;
            }


        };
//            reserverequestQueue.getCache().clear();
        req.setRetryPolicy(new DefaultRetryPolicy(20000, -1, 0));

        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }


}