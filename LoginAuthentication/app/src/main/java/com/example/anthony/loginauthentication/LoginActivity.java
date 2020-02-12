package com.example.anthony.loginauthentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //variables
    private EditText name; //This is the master username for the app login.
    private EditText password; //This is the master password for the app login.
    private TextView info; //Number of attempts left.
    private TextView userExists; //Displays if user tries to create account with username that is taken.
    private TextView passHelp; //Informs user of what is required for a password.
    private TextView loginGuide; //Informs the user of how to use the page.
    private Button login; //Triggers intent to move to passwords page if login is successful.
    private Button register; //Prompts for confirmation; if yes, creates new account with credentials submitted.
    private static final int passwordLengthReq = 8; //Password must be at least 8 characters long.
    private int attemptsCounter = 5; //5 by default; decrements after each wrong try; one 0, login is disabled.
    private boolean passStrong; //Boolean for confirming if password is complex enough.
    private HashMap<String, String> accounts = new HashMap<String, String>(); //HashMap for storing users.
    private static final String FILE_USERS = "users.csv"; //File name.

    @Override
    protected void onCreate(Bundle savedInstanceState) //Runs when the activity starts.
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign local variables to the XML layout elements.
        name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPass);
        info = (TextView)findViewById(R.id.tvInfo);
        userExists = (TextView)findViewById(R.id.tvUserExists);
        passHelp = (TextView)findViewById(R.id.tvPassHelp);
        loginGuide = (TextView)findViewById(R.id.tvLoginGuide);
        login = (Button)findViewById(R.id.btnLogin);
        register = (Button)findViewById(R.id.btnRegister);
        readFromFile();

        info.setText("Login attempts remaining: " + attemptsCounter); //Sets into text to inform user of how many attempts are remaining(default 5).

        login.setOnClickListener(new View.OnClickListener() //Listens for the login button being clicked.
        {
            @Override
            public void onClick(View v) //Runs when login button is clicked.
            {
                validate(name.getText().toString(), password.getText().toString()); //Calls the validate method below, passing the user-entered credentials as parameters.
            } //End onClick().
        }); //End login button listener.

        register.setOnClickListener(new View.OnClickListener() //Listens for the register button to be clicked.
        {
            public void onClick(View v) //Runs when register button is clicked.
            {
                confirmation(); //Confirms the user wants to continue with registration.
                registration(name.getText().toString(), password.getText().toString()); //Sets the text value of username and password to string and passes them to Registration().
                writeToFile(FILE_USERS); //Writes the username and password to file for permanent storage.
                //A new account has now been created with the users login credentials. This is written to the .csv file.
            } //End onClick().
        }); //End register button listener.
    } //End onCreate.

    private void validate(String username, String password) //Method that runs to verify user login credentials then proceeds if valid.
    {
        boolean found = false; //Flag if user is found.

        for(String user : accounts.keySet()) //Loop through list to check for existing user.
        {

            if((username.equals("admin")) && (password.equals("password"))) //If entered username & password match current iterations credentials, just placeholder to test logging in.
            {
                Intent goToPasswordPage = new Intent(LoginActivity.this, PasswordsActivity.class); //Creates a new Intent that takes the user from the login page to the passwords page.
                startActivity(goToPasswordPage); //Starts the above activity.
            }
            else //If credentials were not correct.
            {
                --attemptsCounter;
                info.setText("Login attempts remaining: " + String.valueOf(attemptsCounter)); //Changes the info text below the button, informing the user of how many attempts they have left.

                if (attemptsCounter == 0) //When user runs out of attempts (counter reaches 0).
                {
                    login.setEnabled(false); //Disables the login button, a security measure preventing a user from logging in.
                } //End if.
            }
        }
    } //End validate method.

    private void registration(String username, String password) //Method for adding to account to the HashMap "accounts".
    {
        if ((username != ("") & username != (null)) & password != ("") & password !=(null) & password.length() >= 8) //username & password are not empty/null; password is at least 8 characters long.
        {
            for(String user : accounts.keySet()) //Loop through accounts.
            {
                if(accounts.containsKey(username)) //Compare entered username against existing users.
                {
                    userExists.setText("That username is taken, please try another."); //Informs user that the username they entered is already taken (exists in HashMap).
                }
                else
                {
                    if(isStrong()) //Calls isStrong() method to check password strength; runs if true.
                    {
                        passStrong = true; //Password is complex enough.
                        confirmation(); //Call confirmation method; used for prompting the user before proceeding to create account.

                        accounts.put(username, password); //Adds the new user to the HashMap; key = username, value = password.
                    } //End if.
                    else
                    {
                        passHelp.setText("Password is not strong enough. Try again.\nPassword must be 8 characters or more.");
                    } //End else.
                } //End else.
            } //End for.
        } //End if.
    } //End registration() method.

    private void confirmation() //Method for prompting confirmation from the user to create account.
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Registration")
                .setMessage("Do you wish to create a new account with the credentials you entered?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Continue with registration.
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show(); //End AlertBuilder.
    } //End confirmation() method.

    public boolean isStrong() //Boolean method for deciding if the password is complex enough.
    {
        for(int a = 0; a < password.length(); a++)
        {
            if(Character.isLetter(a)) //If password contains a letter.
            {
                if(Character.isDigit(a)) //If password contains a number.
                {
                    //At this point is has checked that at least one index is a letter & at least one is a number. This is complex enough.
                    return true; //Returns that the password is strong enough.
                } //End if.
                else
                {
                    return false;
                } //End else.
            }//End if.
            else
            {
                return false;
            } //End else.
        } //End for.

        return isStrong(); //Returns if complex or not.
    } //End isStrong() method.

    public void writeToFile(String file) //Method for writing the data to a file.
    {
            try //Try to write to file.
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file)); //Creates a FileWriter that will write to a file.

                for(HashMap.Entry<String, String> entry : accounts.entrySet()) //For each entry in accounts.
                {
                    writer.write(String.join(",", entry.getValue())); //Sets the delimiter to "," then writes the value to the file for each iteration of the loop.
                    writer.newLine(); //After one entry has been written, go to the next line.
                }
            } //End try.
            catch(IOException e) //If file can't be written too.
            {
                Toast.makeText(this,"Could not save the account.", Toast.LENGTH_SHORT).show(); //Alert user the account could not be created.
                Log.d("LoginActivity:", "Error: Could not write to file."); //Logs error for debugging.
            } //End catch.
    } //End writeToFile() method.

    public void readFromFile() //Method for reading from the users.csv file.
    {
        InputStream is = getResources().openRawResource(R.raw.users); //InputStream to read from the users.csv file.
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); //BufferedReader for reading each line.

        String line = ""; //Line to be read.

        try //Try reading from the file.
        {
            reader.readLine(); //Skip csv file headers.

            while((line = reader.readLine()) != null) //While the line isn't null/empty.
            {
                Log.d("LoginActivity", "Line: " + line);
                String[] data = line.split(","); //Splits the data using ",", creating multiple values.

                User tempUser = new User(); //New tempUser.
                tempUser.setName(data[0]); //Set users name equal to the data from file.
                tempUser.setPassword(data[1]); //Sets users password equal to the data from file.
                accounts.put(tempUser.getName(), tempUser.getPassword()); //Adds the temp user to the accounts HashMap.
                Log.d("LoginActivity", "Just created: " + tempUser);
            } //End while.
        } //End try.
        catch(IOException e) //Input error.
        {
            Log.wtf("LoginActivity", "Error: Cannot read line from file " + line, e); //Logs that the file cannot be read.
            e.printStackTrace();
        } //End catch.
    } //End readFromFile() method.

    public void displayUsers() //FOR TESTING HASHMAP READING/WRITING, DELETE THIS ONCE CONFIRMED WORKING!!
    {
        for(String user : accounts.keySet()) //Loop through accounts.
        {
            System.out.println("Username: " + user + "   Password: " + accounts.get(user)); //Print out username & password for current user in iteration of loop.
        }
    }

    public void clearHashMap() //Method for clearing the HashMap when exiting.
    {

    }

    public void confirmExit() //Method for prompting confirmation when exiting the app.
    {

    }

} //End activity.