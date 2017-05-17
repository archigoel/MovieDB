package com.app.themoviedb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog pDialog;
    List<Movie> movieList;
    ImageView submit;
    EditText search;
    private String url;
    Movie movie;

    private static String web_url = "https://api.themoviedb.org/3/search/movie?api_key=0eedabe2e4f05e9bd4268899648901dd&language=en-US&query=";
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (ImageView) findViewById(R.id.search_button);
        search = (EditText) findViewById(R.id.search_movies_edit);
        submit.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {

                final String name = search.getText().toString();
                url = web_url + name;
                url = url.replaceAll(" ", "%20");

                new MovieInfo().execute();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            }
        });
    }


    private class MovieInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            movieList = new ArrayList<>();

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray results = jsonObj.getJSONArray("results");
                    for(int i = 0; i< results.length();i++){
                        JSONObject temp = results.getJSONObject(i);
                        movie = new Movie();

                        movie.title = temp.getString("original_title");
                        movie.overview = temp.getString("overview");
                        movie.poster = "https://image.tmdb.org/t/p/w640/" + temp.getString("poster_path");
                        movie.releaseDate = temp.getString("release_date");

                        String img = temp.getString("poster_path");
                        if ( img != "null"){ // check if image is null
                            movieList.add(movie);

                        }

                    }


                } catch (final JSONException e) {
                    startHomeActivity();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Sorry no data found: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                });




            }return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            recyclerView = (RecyclerView)findViewById(R.id.movie_list);
            movieAdapter = new MovieAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(movieAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(MainActivity.this));

            pDialog.dismiss();
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);

                    //passing data to activity
                    Intent intent = new Intent(MainActivity.this, SingleItemView.class);

                    intent.putExtra("title", movie.getTitle());
                    intent.putExtra("overview", movie.getOverview());
                    intent.putExtra("image", movie.getPoster());
                    intent.putExtra("release", movie.getReleaseDate());

                    startActivity(intent);
//

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));



        }


    }
    private void startHomeActivity() {

        MainActivity.this.finish();
        Intent mIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mIntent);
    }
    // divider beteween items
    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}

