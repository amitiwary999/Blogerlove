package com.virupawadegmail.sdhmancharpune;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
private RecyclerView mblogList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu){
         getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> FirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>( Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase) {


            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());


            }
        };
        mblogList.setAdapter(FirebaseRecyclerAdapter);

    }










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this,Post_Activity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View MView;


        public BlogViewHolder(View itemView) {
            super(itemView);
        }
        public void setTitle(String title){
            TextView post_title=(TextView)MView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc=(TextView)MView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
    }
}
