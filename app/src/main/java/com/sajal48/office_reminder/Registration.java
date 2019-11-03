package com.sajal48.office_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText email_field;
    private EditText pass_field;
    private Button register,login_click;
    private Validator validator;
    private ProgressDialog progressDialog;
    private FirebaseAuth mauth;
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth=FirebaseAuth.getInstance();
        Context context;
        progressDialog=new ProgressDialog(this);
        setContentView(R.layout.activity_registration);
        email_field=findViewById(R.id.reg_email);
        pass_field=findViewById(R.id.reg_password);
        register=findViewById(R.id.register_btn);
        login_click=findViewById(R.id.login_click);
        validator= new Validator(getApplicationContext());



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator.fieldcheck(email_field,pass_field))
                {
                    progressDialog.setMessage("Processing......");
                    progressDialog.show();
                    mauth.createUserWithEmailAndPassword(email_field.getText().toString().trim(),pass_field.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Firebase Registration Success",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Firebase Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }




}
