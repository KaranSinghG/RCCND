package com.example.rccnd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    //Variables
    EditText signUpEmail, signUpPassword, signUpFullName, signUsername, signUpPhoneNumber;
    Button btnSignUp;
    TextView goToLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //hooks
        signUpEmail = findViewById(R.id.SignUpEmail);
        signUpPassword = findViewById(R.id.SignUpPassword);
        signUpFullName = findViewById(R.id.SignUpFullName);
        signUsername = findViewById(R.id.SignUpUsername);
        btnSignUp = findViewById(R.id.SignUpButton);
        signUpPhoneNumber = findViewById(R.id.SignUpPhone);

        goToLogin = findViewById(R.id.textGoToLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //Get all values
                final String name = signUpFullName.getText().toString();
                final String username = signUsername.getText().toString();
                final String email = signUpEmail.getText().toString();
                final String phoneNo = signUpPhoneNumber.getText().toString();
                final String password = signUpPassword.getText().toString();

                if(name.isEmpty()){
                    signUpFullName.setError("Please enter your name");
                    signUpFullName.requestFocus();
                }
                else if(username.isEmpty()){
                    signUsername.setError("Please enter a username");
                    signUsername.requestFocus();
                }
                else if(!username.matches("^[a-z0-9_-]{3,15}$")){
                    signUsername.setError("White spaces not allowed");
                    signUsername.requestFocus();
                }
                else if(email.isEmpty()){
                    signUpEmail.setError("Please enter an email address");
                    signUpEmail.requestFocus();
                }
                else if(!email.matches("[^@]+@[^\\.]+\\..+")){
                    signUpEmail.setError("Please enter a valid Email");
                    signUpEmail.requestFocus();
                }
                else if(phoneNo.isEmpty()){
                    signUpPhoneNumber.setError("Please enter Phone Number");
                    signUpPhoneNumber.requestFocus();
                }
                else if(password.isEmpty()){
                    signUpPassword.setError("Please enter Password");
                    signUpPassword.requestFocus();
                }
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                signUpPassword.setError("Password Strength is low");
                                signUpPassword.requestFocus();
                            }
                            else{
                                UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
                                reference.child(username).setValue(helperClass);
                                startActivity(new Intent(SignUp.this,Dashboard.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
//                String email = emailID.getText().toString();
//                String pwd = password.getText().toString();
//                if(email.isEmpty()){
//                    emailID.setError("Please enter email id");
//                    emailID.requestFocus();
//                }
//                else if(pwd.isEmpty()){
//                    password.setError("Please enter password");
//                    password.requestFocus();
//                }
//                else if(email.isEmpty() && pwd.isEmpty()){
//                    Toast.makeText(SignUp.this,"Fields are empty",Toast.LENGTH_SHORT).show();
//                }
//                else if(!(email.isEmpty() && pwd.isEmpty())){
//                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(!task.isSuccessful()){
//                                Toast.makeText(SignUp.this,"Sign Up unsuccessful, please try again",Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                startActivity(new Intent(SignUp.this,Dashboard.class));
//                            }
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(SignUp.this,"Error occurred",Toast.LENGTH_SHORT).show();
//                }

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
            }
        });
    }
}
