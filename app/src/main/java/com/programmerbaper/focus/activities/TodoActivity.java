package com.programmerbaper.focus.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.programmerbaper.focus.adapters.TodoRecycleAdapter;
import com.programmerbaper.focus.entities.Clock;
import com.programmerbaper.focus.entities.Todo;
import com.programmerbaper.focus.networking.QueryUtils;
import com.programmerbaper.fokus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TodoActivity extends AppCompatActivity {

    private ArrayList<Todo> mTodos;
    private AHBottomNavigation mBottomNavigation;
    private Drawer mDrawer;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        MainActivity.createBottomNav(this, mBottomNavigation);

        //Set Toolbar
        mToolBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolBar);

        //Init navigation drawer
        new DrawerBuilder().withActivity(this).build();
        mDrawer = MainActivity.createNavigationDrawer(savedInstanceState, mToolBar, this);

        new TodoAsyncTask().execute(Clock.BASE_PATH + Clock.GET_USER + LoginActivity.user.getId() + Clock.GET_TODO);

    }

    private void updateUI(List<Todo> tikets) {

        ProgressBar mProgress = findViewById(R.id.progressBar);
        mProgress.setVisibility(View.GONE);
        mToolBar.setVisibility(View.VISIBLE);
        mBottomNavigation.setVisibility(View.VISIBLE);

        if (!tikets.isEmpty()) {
            TodoRecycleAdapter tiketRecycleAdapter =
                    new TodoRecycleAdapter(this, tikets);
            RecyclerView recyclerView = findViewById(R.id.rvTickets);
            recyclerView.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(tiketRecycleAdapter);
        } else {
            TextView textView = findViewById(R.id.no_ticket);
            textView.setVisibility(View.VISIBLE);
        }

        setTitle(LoginActivity.user.getName().substring(0, 1).toUpperCase() +
                LoginActivity.user.getName().substring(1) + " 's Todo");

    }

    private class TodoAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            mTodos = new ArrayList<>();

            try {
                JSONObject root = new JSONObject(QueryUtils.fetchResponse(urls[0]));
                JSONArray data = root.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject todoNow = data.getJSONObject(i);
                    Todo todo = new Todo(todoNow.getString("name"), convertDate(todoNow.getString("fokus_at")), new Clock(1, 1, 1));
                    mTodos.add(todo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String response) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateUI(mTodos);

                }
            }, 1000);

        }


        private String convertDate(String inputDate) {

            String resutlt = "";

            DateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                resutlt = output.format(input.parse(inputDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return resutlt;

        }

    }

}
