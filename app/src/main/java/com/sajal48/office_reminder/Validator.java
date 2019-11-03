package com.sajal48.office_reminder;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Validator {
    private Context context;

    public Validator(Context context)
    {
        this.context=context;
    }
    public boolean fieldcheck(EditText email, EditText password)
    {
     String e = email.getText().toString().trim();
     String p= password.getText().toString().trim();
     if(TextUtils.isEmpty(e)||TextUtils.isEmpty(p))
     {
         Toast.makeText(context,"Please Fill all Fields",Toast.LENGTH_SHORT).show();
         return false;
     }
     else
     {
      return emailcheck(e);
     }
    }
    public boolean emailcheck(String email)
    {
        String m = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(m);
        if (pat.matcher(email).matches())
        {
            return true;
        }
        else
        {
            Toast.makeText(context,"Please Fill all Fields",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
