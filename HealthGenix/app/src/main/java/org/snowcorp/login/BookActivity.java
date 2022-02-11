package org.snowcorp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.snowcorp.login.helper.Functions;

import java.util.HashMap;
import java.util.Map;

public class BookActivity extends AppCompatActivity {


    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnBook;
    private TextInputLayout tname, tfathername, tmothername, tdob, tgender, tphone, temail, taddress, tcity, tstate, tpincode, tmed, thistory, tbdate, thospital;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        tname = (TextInputLayout) findViewById(R.id.rTextName);
        tfathername = (TextInputLayout) findViewById(R.id.rTextFName);
        tmothername = (TextInputLayout) findViewById(R.id.rTextMName);
        tdob = (TextInputLayout) findViewById(R.id.rTextDob);
        tgender = (TextInputLayout) findViewById(R.id.rTextGen);
        tphone = (TextInputLayout) findViewById(R.id.rTextPhone);
        temail = (TextInputLayout) findViewById(R.id.rTextEmail);
        taddress = (TextInputLayout) findViewById(R.id.rTextAddr);
        tcity = (TextInputLayout) findViewById(R.id.rTextCity);
        tpincode = (TextInputLayout) findViewById(R.id.rTextCode);
        tstate = (TextInputLayout) findViewById(R.id.rTextState);
        thistory = (TextInputLayout) findViewById(R.id.rTextHis);
        tmed = (TextInputLayout) findViewById(R.id.rTextMed);
        tbdate = (TextInputLayout) findViewById(R.id.rTextBDate);
        thospital = (TextInputLayout) findViewById(R.id.rTextHos);
        btnBook = (Button) findViewById(R.id.btnBook);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        // Login button Click Event
        btnBook.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(BookActivity.this);

                String name = tname.getEditText().getText().toString().trim();
                String fname = tfathername.getEditText().getText().toString().trim();
                String mname = tmothername.getEditText().getText().toString().trim();
                String dob = tdob.getEditText().getText().toString().trim();
                String gen = tgender.getEditText().getText().toString().trim();
                String phone = tphone.getEditText().getText().toString().trim();
                String email = temail.getEditText().getText().toString().trim();
                String address = taddress.getEditText().getText().toString().trim();
                String city = tcity.getEditText().getText().toString().trim();
                String state = tstate.getEditText().getText().toString().trim();
                String pincode = tpincode.getEditText().getText().toString().trim();
                String history = thistory.getEditText().getText().toString().trim();
                String med = tmed.getEditText().getText().toString().trim();
                String bdate = tbdate.getEditText().getText().toString().trim();
                String hospital = thospital.getEditText().getText().toString().trim();

                System.out.println("***********"+phone);

                // Check for empty data in the form
                if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !hospital.isEmpty()) {
                    if (Functions.isValidEmailAddress(email)) {
                        //bookUser(name, email, password, phone);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void bookUser(final String name, final String email, final String password, final String phone) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}