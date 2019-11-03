package com.sajal48.office_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class Login extends AppCompatActivity {
    private final AppCompatActivity activity=Login.this;
    private Validator validator;
    private EditText email_field;
    private EditText pass_field;
    private ProgressDialog progressDialog;
    private Button login,reg_click;
    private FirebaseAuth mauth;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_field=findViewById(R.id.login_email);
        pass_field=findViewById(R.id.login_password);
        login=findViewById(R.id.login_btn);
        reg_click=findViewById(R.id.register_click);
        progressDialog=new ProgressDialog(this);
        validator= new Validator(getApplicationContext());
        mauth= FirebaseAuth.getInstance();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator.fieldcheck(email_field,pass_field))
                {
                    progressDialog.setMessage("Processing......");
                    progressDialog.show();
                   mauth.signInWithEmailAndPassword(email_field.getText().toString().trim(),pass_field.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful())
                           {
                               Toast.makeText(getApplicationContext(),"Firebase Login Success",Toast.LENGTH_SHORT).show();
                               progressDialog.dismiss();
                               Intent i = new Intent(getApplicationContext(),Home.class);
                               startActivity(i);
                           }
                           else
                           {
                               progressDialog.dismiss();
                               Toast.makeText(getApplicationContext(),"Firebase Login Failed",Toast.LENGTH_SHORT).show();
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

        reg_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });

    }
}
