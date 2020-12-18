package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserViewPost extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUserDatabase;
    private ArrayList<PostImages> postImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts_business);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Postings").getRef();

        mUsersList = (RecyclerView) findViewById(R.id.postView);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        postImagesList = new ArrayList<>();
    }


    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").limitToLast(50);
        FirebaseRecyclerOptions<PostImages> options = new FirebaseRecyclerOptions.Builder<PostImages>().setQuery(mUserDatabase, PostImages.class).build();
        FirebaseRecyclerAdapter<PostImages, UserViewPost.UsersViewHolder> adapter = new FirebaseRecyclerAdapter<PostImages, UserViewPost.UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewPost.UsersViewHolder holder, int position, @NonNull PostImages model) {

                holder.setImage(model.getImageURL());
                holder.setDescription(model.getDescription());
                final String userId = getRef(position).getKey();

            }

            @NonNull
            @Override
            public UserViewPost.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_single_layout, parent, false);
                UserViewPost.UsersViewHolder viewHolder = new UserViewPost.UsersViewHolder(view);
                return viewHolder;
            }
        };

        mUsersList.setAdapter(adapter);
        adapter.startListening();

    }




    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }



        public void setDescription(String description) {
            TextView statusView = mView.findViewById(R.id.singleDescription);
            statusView.setText(description);
        }

        public void setImage(String postImage) {
            ImageView userImageView = mView.findViewById(R.id.singlePostImage);
            Picasso.get().load(postImage).placeholder(R.drawable.hqdefault).into(userImageView);
        }
    }


}