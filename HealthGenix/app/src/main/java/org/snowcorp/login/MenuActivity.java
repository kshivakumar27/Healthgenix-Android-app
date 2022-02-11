package org.snowcorp.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SessionManager session;
    private DatabaseHandler db;
    private SearchView search;

    private TextView name;
    private TextView email;
    private static final String TAG = MenuActivity.class.getSimpleName();

    private ProgressDialog pDialog;
private FloatingActionButton spk;
    private HashMap<String,String> user = new HashMap<>();

    private final int REQ_CODE_SPEECH_INPUT=100;

    private TextToSpeech t1;

    private String spokenWords;
    MediaPlayer mp;
    ImageButton imgButton,imgButton1,imgButton2,imgButton3;
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        name = (TextView) findViewById(R.id.textView);
        email = (TextView) findViewById(R.id.email);

        myDialog = new Dialog(this);
        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MenuActivity.this, "Result: "+query, Toast.LENGTH_LONG).show();
                if(query.equals("ಕ್ಯಾನ್ಸರ್"))
                {
                   mp=MediaPlayer.create(MenuActivity.this,R.raw.cancer);
                    mp.start();
                    ShowPopup();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                return false;
            }

        });



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from database
        String nam = user.get("name");
        String emai = user.get("email");

        //name.setText(nam);
       // email.setText(emai);

        System.out.println("********************"+nam+"****************88");




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput();
            }
        });


        imgButton =(ImageButton)findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, FeverActivity.class);
                startActivity(i);
            }
        });
        imgButton1 =(ImageButton)findViewById(R.id.imageButton2);
        imgButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, TimeActivity.class);
                startActivity(i);
            }
        });


        imgButton2 =(ImageButton)findViewById(R.id.imageButton3);
        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp=MediaPlayer.create(MenuActivity.this,R.raw.cancer);
                mp.start();

            }
        });


        imgButton3 =(ImageButton)findViewById(R.id.imageButton4);
        imgButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, BookActivity.class);
                startActivity(i);

            }
        });



                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_kid) {
            Intent i = new Intent(MenuActivity.this, HomeActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(String.format(Locale.ENGLISH, "https://www.google.com/maps/search/?api=1&query=hospital+nearby")));
            startActivity(intent);


        } else if (id == R.id.nav_manage) {
           // Intent i = new Intent(MenuActivity.this, MapsActivity.class);
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://192.168.0.106/pp/index.php"));
            startActivity(viewIntent);
            //startActivity(i);


        } else if (id == R.id.nav_share) {
            Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, "Download HealthGenix an health care website" );
            startActivity(Intent.createChooser(intent2, "Share via"));

        } else if (id == R.id.nav_cpass) {


            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MenuActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.change_password, null);

            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Change Password");
            dialogBuilder.setCancelable(false);

            final TextInputLayout oldPassword = (TextInputLayout) dialogView.findViewById(R.id.old_password);
            final TextInputLayout newPassword = (TextInputLayout) dialogView.findViewById(R.id.new_password);

            dialogBuilder.setPositiveButton("Change",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // empty
                }
            });

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final AlertDialog alertDialog = dialogBuilder.create();

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(oldPassword.getEditText().getText().length() > 0 &&
                            newPassword.getEditText().getText().length() > 0){
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            oldPassword.getEditText().addTextChangedListener(textWatcher);
            newPassword.getEditText().addTextChangedListener(textWatcher);

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setEnabled(false);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email = user.get("email");
                            String old_pass = oldPassword.getEditText().getText().toString();
                            String new_pass = newPassword.getEditText().getText().toString();

                            if (!old_pass.isEmpty() && !new_pass.isEmpty()) {
                                changePassword(email, old_pass, new_pass);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Fill all values!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });

            alertDialog.show();
        }


        else if (id == R.id.nav_logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(getApplicationContext());
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void changePassword(final String email, final String old_pass, final String new_pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.RESET_PASS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reset Password Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Reset Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("tag", "change_pass");
                params.put("email", email);
                params.put("old_password", old_pass);
                params.put("password", new_pass);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        // Adding request to volley request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
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



    private void askSpeechInput()
    {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a) {

        }
    }

    private void tellSpokenWords(){
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                    //String toSpeak = voiceInput.getText().toString();
                    //Toast.makeText(getApplicationContext(), spokenWords,Toast.LENGTH_SHORT).show();
                    if(spokenWords.equalsIgnoreCase("Who are you"))//question
                    {
                        t1.speak("i am your health assistant healthgenix", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }else if(spokenWords.equalsIgnoreCase("symptoms of cancer"))//question
                    {
                        t1.speak("Changes in bowel or bladder habits.\n" +
                                "A sore that does not heal.\n" +
                                "Unusual bleeding or discharge.\n" +
                                "Thickening or lump in the breast or any other part of the body.\n" +
                                "Indigestion or difficulty swallowing.\n" +
                                "Obvious change in a wart or mole.\n" +
                                "Nagging cough or hoarseness.", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("college"))//question
                    {
                        t1.speak("ATME", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("father name"))//question
                    {
                        t1.speak("rajesh kumar", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("mother name"))//question
                    {
                        t1.speak("bhanumathi", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("whatsapp"))//question
                    {
                        Intent sendIntent = new Intent();
                        sendIntent.setPackage("com.whatsapp");
                        startActivity(sendIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("what's up"))//question
                    {
                        Intent sendIntent = new Intent();
                        sendIntent.setPackage("com.whatsapp");
                        startActivity(sendIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("hi"))//question
                    {
                        t1.speak("hello how can i help you", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("hey"))//question
                    {
                        t1.speak("hello how can i help you", TextToSpeech.QUEUE_FLUSH, null);//answer

                    }
                    else if(spokenWords.equalsIgnoreCase("google"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.google.com/"));
                        startActivity(viewIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("facebook"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.facebook.com/"));
                        startActivity(viewIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("flipkart"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.flipkart.com/"));
                        startActivity(viewIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("amazon"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.amazon.in/"));
                        startActivity(viewIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("wikipedia"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.wikipedia.org/"));
                        startActivity(viewIntent);
                    }
                    else if(spokenWords.equalsIgnoreCase("gmail"))//question
                    {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.gmail.com/"));
                        startActivity(viewIntent);
                    }


                    else{
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://www.google.com/search?source=hp&ei=Qj2BXOeoLYvVvATt9pyQCQ&q="+spokenWords+"&btnK=Google+Search&oq="+spokenWords+"&gs_l=psy-ab.3..0i131i67j0i67l2j0i131i67l2j0i67j0i131j0j0i131j0i67.5358.7473..9417...2.0..0.254.1045.0j4j2......0....1..gws-wiz.....6..35i39.uXm-WtSjJzU"));
                        startActivity(viewIntent);

                    }


                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path="sdcard/camera_app/cam_image.jpg";
        //img.setImageDrawable(Drawable.createFromPath(path) );
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    spokenWords =result.get(0);
                    //voiceInput.setText(result.get(0));
                    tellSpokenWords();
                }
                break;
            }

        }
    }
    public void ShowPopup() {
        TextView name,sym,about;
        Button btnclose;
        myDialog.setContentView(R.layout.activity_fever);
        name =(TextView) myDialog.findViewById(R.id.name);
        name.setText("ಕ್ಯಾನ್ಸರ್");
        sym =(TextView) myDialog.findViewById(R.id.sym);
        sym.setText("ರೋಗ ಸೂಚನೆ ಹಾಗೂ ಲಕ್ಷಣಗಳು");
        about =(TextView) myDialog.findViewById(R.id.about);
        about.setText("ಸ್ಥಳೀಯ ಲಕ್ಷಣಗಳು ,ಅಸಹಜ ಗೆಡ್ಡೆ ಅಥವಾ ಬಾವು ");
        btnclose = (Button) myDialog.findViewById(R.id.btnokay);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
