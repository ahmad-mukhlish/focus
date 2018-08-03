package com.programmerbaper.focus.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.programmerbaper.focus.entities.Jam;
import com.programmerbaper.focus.networking.QueryUtils;
import com.programmerbaper.fokus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        Button signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Field tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
                    Toast.makeText(getBaseContext(), "Password tidak cocok", Toast.LENGTH_SHORT).show();
                }
                else
                    new RegisterAsyncTask(getBaseContext()).execute(Jam.BASE_PATH + Jam.POST_REGISTER);
            }



        });

    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        private Context mContext;

        RegisterAsyncTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            try {
                QueryUtils.postWithHttp(QueryUtils.parseStringLinkToURL(urls[0]),createJsonMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(mContext, "Register Success", Toast.LENGTH_SHORT).show();
        }

        private String createJsonMessage() {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.accumulate("email", email.getText().toString());
                jsonObject.accumulate("password", password.getText().toString());
                jsonObject.accumulate("name", username.getText().toString());


            } catch (JSONException e) {
                Log.e("Kelas Register", "Error when create JSON message", e);
            }

            return jsonObject.toString();

        }
    }

}
