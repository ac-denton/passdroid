package com.example.anthony.loginauthentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class PasswordsActivity extends AppCompatActivity {

    private String user;
    private EditText title;
    private EditText password;
    private Button account;
    private HashMap<String, String> passwords = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);


    } //End onCreate().

    public void searchList() //Method for using the search bar to find entry.
    {
        //User taps search bar, types, show result.
    }

    public void addEntry() //Method for adding a new entry to the list.
    {

    }

    public void deleteEntry() //Method for deleting an entry.
    {
        //User holds down on entry, has option to delete.
    }

} //End activity.
