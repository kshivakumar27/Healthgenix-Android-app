package org.snowcorp.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.snowcorp.login.helper.Functions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class KidRegister extends AppCompatActivity {


    private static final String TAG = KidRegister.class.getSimpleName();

    private Button btnRegister, btnDate;
    private TextInputLayout inputName, inputEmail, inputPhone;
    private TextView inputDOB;
    private Calendar Dob;
    private ProgressDialog pDialog;
    private TextView activeDateDisplay;
    private Calendar activeDate;
    static final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_register);

        inputName = (TextInputLayout) findViewById(R.id.rTextName);
        inputPhone = (TextInputLayout) findViewById(R.id.rTextPhone);
        inputEmail = (TextInputLayout) findViewById(R.id.rTextEmail);
        inputDOB = (TextView) findViewById(R.id.dob);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnDate = (Button) findViewById(R.id.btnDOB);

        /* get the current date */
        Dob = Calendar.getInstance();

        /* add a click listener to the button   */
        btnDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(inputDOB, Dob);
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }


    private void init() {
        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(KidRegister.this);

                String name = inputName.getEditText().getText().toString().trim();
                String phone = inputPhone.getEditText().getText().toString().trim();
                String email = inputEmail.getEditText().getText().toString().trim();
                Calendar startDate1=Calendar.getInstance();
                startDate1.set(Dob.get(Calendar.YEAR), Dob.get(Calendar.MONTH)+1, Dob.get(Calendar.DAY_OF_MONTH));
                String dob = Dob.get(Calendar.DAY_OF_MONTH)+"/"+Dob.get(Calendar.MONTH)+"/"+Dob.get(Calendar.YEAR);
                System.out.println("*******************"+dob);

                // Check for empty data in the form
                if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                    if (Functions.isValidEmailAddress(email)) {
                        registerDob(name, email, dob, phone);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }





    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));

    }



    private void registerDob(final String name, final String email, final String password, final String phone) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.KID_URL, new Response.Listener<String>() {

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
                params.put("dob", password);
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
