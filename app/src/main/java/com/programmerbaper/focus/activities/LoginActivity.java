package com.programmerbaper.focus.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.programmerbaper.focus.entities.Clock;
import com.programmerbaper.focus.entities.User;
import com.programmerbaper.focus.networking.QueryUtils;
import com.programmerbaper.fokus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = LoginActivity.class.getName();

    private EditText email;
    private EditText password;
    public static User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Login Screen without window
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_login);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        RelativeLayout loginScreen = findViewById(R.id.login_screen);

        LinearLayout error = findViewById(R.id.error);
        error.setVisibility(View.GONE);

        if (isConnected) {
            TextView register = findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

            email = findViewById(R.id.email);
            password = findViewById(R.id.edPassword);
            Button login = findViewById(R.id.login);

            login.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    new LoginAsyncTask(LoginActivity.this).execute(Clock.BASE_PATH + Clock.POST_AUTH);
                }
            });
        } else {
            error.setVisibility(View.VISIBLE);
            loginScreen.setVisibility(View.GONE);
        }


    }


    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        private Boolean status = false;

        LoginAsyncTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            try {
                JSONObject root = new JSONObject(QueryUtils.postWithHttp(QueryUtils.parseStringLinkToURL(urls[0]), createJsonMessage()));
                status = root.getBoolean("status");
                JSONObject data = root.getJSONObject("data");
                JSONObject user = data.getJSONObject("user");
                LoginActivity.user = new User(user.getInt("id") + "", user.getString("name"), user.getString("email"), data.getString("token"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String response) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            View rootDialog = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialogue_loading, null);
            builder.setView(rootDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status) {
                        Toast.makeText(mContext, "Login Sukses", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else
                        Toast.makeText(getBaseContext(), "Username atau password tidak dikenal", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }, 3000);
        }
    }


    private String createJsonMessage() {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("email", email.getText().toString());
            jsonObject.accumulate("password", password.getText().toString());
            ;


        } catch (JSONException e) {
            Log.e("Kelas Login", "Error when create JSON message", e);
        }

        return jsonObject.toString();

    }
}







