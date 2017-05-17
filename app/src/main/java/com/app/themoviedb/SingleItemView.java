package com.app.themoviedb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SingleItemView extends Activity {

	String title;
	String plot;
	String poster;
	String releaseDate;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singleitemview);
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString("title");
		plot = bundle.getString("overview");
		poster = bundle.getString("image");
		releaseDate = bundle.getString("release");

		TextView movieTitle = (TextView) findViewById(R.id.title_movie);
//		TextView moviePlot = (TextView) findViewById(R.id.movie_plot);
		TextView release = (TextView) findViewById(R.id.release_date);
		ImageView moviePoster = (ImageView) findViewById(R.id.movie_full_poster);

		Picasso.with(getApplicationContext()).load(poster).into(moviePoster);
		movieTitle.setText(title);
		release.setText("Released On : " + releaseDate);
	}


}