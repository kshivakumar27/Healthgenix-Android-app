package org.snowcorp.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.snowcorp.login.helper.DatabaseHandler;
import org.snowcorp.login.helper.Functions;
import org.snowcorp.login.helper.SessionManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akshay Raj on 6/16/2016.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private TextView txtName, txtEmail;
    private Button btnChangePass, btnLogout;
    private SessionManager session;
    private DatabaseHandler db;

    private ProgressDialog pDialog;

    private HashMap<String,String> user = new HashMap<>();


    private TextView startDateDisplay;
    private TextView endDateDisplay;
    private TextView txtResult;
    private TextView txtMonthDays;
    private TextView txtWeekDays;
    private TextView txtTotalDays;
    private Button startPickDate;
    private Button endPickDate;
    private Button btnCalculate;
    private Button btnRegister;
    private Calendar startDate;
    private Calendar endDate;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
        startPickDate = (Button) findViewById(R.id.btnStartDate);

        /* get the current date */
        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startDateDisplay, startDate);
            }
        });

        /* capture our View elements for the end date function */
        endDateDisplay = (TextView) findViewById(R.id.endDateDisplay);
        endPickDate = (Button) findViewById(R.id.btnEndDate);

        /* get the current date */
        endDate = Calendar.getInstance();

        /* add a click listener to the button   */
        endPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endDateDisplay, endDate);
            }
        });

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtMonthDays = (TextView) findViewById(R.id.txtMonthDay);
        txtWeekDays = (TextView) findViewById(R.id.txtWeekDays);
        txtTotalDays = (TextView) findViewById(R.id.txtTotalDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculateAge);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        /* add a click listener to the button   */
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar startDate1=Calendar.getInstance();

                startDate1.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH));

                Calendar endDate1=Calendar.getInstance();
                endDate1.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH)+1, endDate.get(Calendar.DAY_OF_MONTH));

                DateCalculator dateCaculator=DateCalculator.calculateAge(startDate1,endDate1);
                String age = "Age: " + dateCaculator.getYear() + " Years " + dateCaculator.getMonth() + " Months " + dateCaculator.getDay()+ " Days";
                int num_weeks = (int) dateCaculator.getTotalDay()/7;
                int num_months = dateCaculator.getYear()*12 + dateCaculator.getMonth();
                System.out.println(dateCaculator.getYear());
                txtResult.setText(age);
                int days=dateCaculator.getTotalDay();
                int weeks=dateCaculator.getTotalDay()%7;
                int months=dateCaculator.getDay();
                //txtTotalDays.setText(""+dateCaculator.getTotalDay()+" Days");
                Calendar tst= Calendar.getInstance();
                //txtWeekDays.setText(tst.get(Calendar.DAY_OF_MONTH)+"/"+tst.get(Calendar.MONTH)+"/"+tst.get(Calendar.YEAR));
                //txtWeekDays.setText(""+num_weeks+" Weeks " + dateCaculator.getTotalDay()%7 + " Days");
                //txtMonthDays.setText(""+num_months+" Months "+dateCaculator.getDay()+" Days ");


                if(days==0)
                {
                    tst.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH));
                    txtMonthDays.setText("Vaccination date: "+tst.get(Calendar.DAY_OF_MONTH)+"/"+tst.get(Calendar.MONTH)+"/"+tst.get(Calendar.YEAR));
                    txtWeekDays.setText("Vaccinations: \n1.Bacillus Calmette–Guérin (BCG)\n2.Oral polio vaccine (OPV 0)\n3.Hepatitis B (Hep – B1)");
                }

                else if(days<=42 && days>0)
                {
                    tst.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH)+42);
                    txtMonthDays.setText("Vaccination date: "+tst.get(Calendar.DAY_OF_MONTH)+"/"+tst.get(Calendar.MONTH)+"/"+tst.get(Calendar.YEAR));
                    txtWeekDays.setText("Vaccinations: \n1.Diptheria, Tetanus and Pertussis vaccine (DTwP 1)\n2.Inactivated polio vaccine (IPV 1)\n3.Hepatitis B  (Hep – B2)\n4.Haemophilus influenzae type B (Hib 1)\n5.Rotavirus 1\n6.Pneumococcal conjugate vaccine (PCV 1)");
                }
                else
                {
                    txtMonthDays.setText("Enter correct dates");
                }

                //showDateDialog(endDateDisplay, endDate);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, KidRegister.class);

                startActivity(i);
            }});

        /* display the current date (this method is below)  */
        updateDisplay(startDateDisplay, startDate);
        updateDisplay(endDateDisplay, endDate);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);




        // Hide Keyboard

    }

    /**
     *  update the date at ui text view
     * @param dateDisplay text view where the date will be shown
     * @param date selected date
     */
    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));

    }

    /**
     * display the date dialog
     * @param dateDisplay
     * @param date
     */
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





}