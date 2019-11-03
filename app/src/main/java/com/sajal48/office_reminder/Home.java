package com.sajal48.office_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import Data.Data;

public class Home extends AppCompatActivity {
    Data data;

    private FloatingActionButton fl;

    //firebase
    private FirebaseAuth mauth;
    private DatabaseReference mdatabase;

    //Recycycler

    public RecyclerView recyclerView;


    @Override
    public void onBackPressed() {
        //by sajal
        //by piyal
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fl=findViewById(R.id.floating_btn);

        //firebase
        mauth=FirebaseAuth.getInstance();
        if (mauth.getCurrentUser()==null)
        {
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        FirebaseUser mUser = mauth.getCurrentUser();
        String uId= mUser.getUid();
        mdatabase= FirebaseDatabase.getInstance().getReference().child(uId);
        mdatabase.keepSynced(true);


        recyclerView.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


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
        switch (id)
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


           FirebaseRecyclerAdapter<Data,Myviewholder> adapter = new FirebaseRecyclerAdapter<Data, Myviewholder>(
                   Data.class,
                   R.layout.item_data,
                   Myviewholder.class,
                   mdatabase) {
               protected void populateViewHolder(Myviewholder myviewholder, Data data, int i) {

                   myviewholder.setTitle(data.getTitle());
                   myviewholder.setNote(data.getNote());
                   myviewholder.setDate(data.getDate());
               }
           };
           recyclerView.setAdapter(adapter);


        }

    public static class Myviewholder extends RecyclerView.ViewHolder
    {
        View myview;
        public Myviewholder(View itemview)
        {
            super(itemview);
            myview=itemview;
        }
        public void setTitle(String title)
        {
            TextView mtitle=myview.findViewById(R.id.show_title);
            mtitle.setText(title);
        }

        public void setNote(String note)
        {
            TextView mnote=myview.findViewById(R.id.show_note);
            mnote.setText(note);
        }

        public void setDate(String date)
        {
            TextView mdate=myview.findViewById(R.id.show_note);
            mdate .setText(date);
        }

    }

    }



