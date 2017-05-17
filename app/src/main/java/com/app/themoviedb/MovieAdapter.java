package com.app.themoviedb;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private LayoutInflater inflater = null;
    Movie movie;


    public MovieAdapter(Context context, List<Movie> list) {

        this.context = context;
        this.movieList = list;
        inflater = LayoutInflater.from(context);
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Movie itemList;
        TextView title;
        TextView overview;
        ImageView poster;
        public void bindData(Movie itemList){
            this.itemList=itemList;
        }


        public MyViewHolder(View itemView) {
            super(itemView);
           title = (TextView) itemView.findViewById(R.id.movie_name);
           overview = (TextView) itemView.findViewById(R.id.movie_overview);
           poster = (ImageView) itemView.findViewById(R.id.movie_poster);

        }


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list,viewGroup,false);
        MyViewHolder vh=new MyViewHolder(v);
        v.setTag(movieList.get(i));
        movie =(Movie)v.getTag();

        return vh;


    }

    @Override
    public int getItemCount() {
        return movieList.size();

    }

    @Override
    public void onBindViewHolder(MovieAdapter.MyViewHolder viewHolder, int position) {

        viewHolder.poster.setImageResource(R.drawable.search);
        Picasso.with(context).load(movieList.get(position).getPoster()).into(viewHolder.poster); // load image
//        new DownloadImageTask(viewHolder.poster).execute(movieList.get(position).getPoster());
        viewHolder.title.setText(movieList.get(position).getTitle());
        viewHolder.overview.setText(movieList.get(position).getOverview());
//

    }


}