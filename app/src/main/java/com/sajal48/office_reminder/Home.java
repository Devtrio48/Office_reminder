package com.sajal48.office_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.load.model.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import Data.Data;

public class Home extends AppCompatActivity {

    //firebase
    private FirebaseAuth mauth;
    private DatabaseReference mdatabase;

    //Recycycler



    private FirebaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    public void onBackPressed() {
        //by sajal
        //by piyal
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.list_view);
        FloatingActionButton fl = findViewById(R.id.floating_btn);

        //Firebase
        mauth=FirebaseAuth.getInstance();
        if (mauth.getCurrentUser()==null)
        {
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        FirebaseUser mUser = mauth.getCurrentUser();
        String uId= mUser.getUid();
        mdatabase= FirebaseDatabase.getInstance().getReference().child(uId);
        mdatabase.keepSynced(true);

        //Recyclerview

        LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        fetch();





        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(Home.this);
                LayoutInflater inflater = LayoutInflater.from(Home.this);
                View mview = inflater.inflate(R.layout.custominputfield,null);
                alertdialog.setView(mview);
                final AlertDialog dialog = alertdialog.create();
                final EditText title= mview.findViewById(R.id.edt_title);
                final EditText note= mview.findViewById(R.id.edt_Note);
                Button sava_btn = mview.findViewById(R.id.save_btn);

                sava_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mTitle=title.getText().toString().trim();
                        String mNote=note.getText().toString().trim();

                        if(TextUtils.isEmpty(mTitle))
                        {
                            title.setError("Required Field");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)
                        ) {
                            note.setError("Required Field");
                            return;
                        }

                        String id=mdatabase.push().getKey();
                        String date= DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(mTitle,mNote,date,id);
                        assert id != null;
                        mdatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Note Inserted",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ex, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.logout_btn:
                mauth.signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder
    {
        View myview;
        Myviewholder(View itemview)
        {
            super(itemview);
            myview=itemview;
        }
        void setTitle(String title)
        {
            TextView mtitle=myview.findViewById(R.id.show_title);
            mtitle.setText(title);
        }

        void setNote(String note)
        {
            TextView mnote=myview.findViewById(R.id.show_note);
            mnote.setText(note);
        }

        void setDate(String date)
        {
            TextView mdate=myview.findViewById(R.id.date);
            mdate.setText(date);
        }

    }
    private void fetch()
    {
        Query query=FirebaseDatabase.getInstance().getReference().child(mauth.getCurrentUser().getUid());
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(query, new SnapshotParser<Data>() {

                            @NonNull
                            @Override
                            public Data parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Data(snapshot.child("title").getValue().toString(),snapshot.child("note").getValue().toString(),snapshot.child("date").getValue().toString(),snapshot.child("id").getValue().toString());
                            }
                        }).build();
        adapter = new FirebaseRecyclerAdapter<Data,Myviewholder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull Myviewholder holder, int position, @NonNull Data data) {
                holder.setTitle(data.getTitle());
                holder.setNote(data.getNote());
                holder.setDate(data.getDate());
            }

            @NonNull
            @Override
            public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
                return new Myviewholder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    }



