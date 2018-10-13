package com.mbass.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends Activity {
    private Button loginButton;
    private TextView message;
    private Button registrate_button;
    private TextView userinfo;
    private Button logout_button;
    private Switch logreg;
    private EditText email;
    private EditText name;
    private EditText pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);
        loginButton = (Button) findViewById(R.id.login_button1);
        message = (TextView) findViewById(R.id.textView2);
        userinfo = (TextView) findViewById(R.id.textView3);
        email = (EditText) findViewById(R.id.email_field1);
        name = (EditText) findViewById(R.id.name_field);
        pass = (EditText) findViewById(R.id.pass_field1);
        registrate_button = (Button) findViewById(R.id.registrate_button);
        logreg = findViewById(R.id.switch1);
        logout_button = (Button) findViewById(R.id.logout);
        logout_button.setEnabled(false);
        logout_button.setAlpha(0.0f);
        name.setEnabled(false);
        registrate_button.setEnabled(false);
        name.setAlpha(0.0f);
        registrate_button.setAlpha(0.0f);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        logreg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    name.setEnabled(true);
                    registrate_button.setEnabled(true);
                    name.setAlpha(1.0f);
                    registrate_button.setAlpha(1.0f);
                    loginButton.setEnabled(false);
                    loginButton.setAlpha(0.0f);
                } else{
                    name.setEnabled(false);
                    registrate_button.setEnabled(false);
                    name.setAlpha(0.0f);
                    registrate_button.setAlpha(0.0f);
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1.0f);
                }
            }
        });
        registrate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrate();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
    }
    private void Registrate() {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email.getText().toString());
        user.setPassword(pass.getText().toString());
        user.setProperty( "name", name.getText().toString());
        try {
            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    message.setText("You have registrated");
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    message.setText(fault.getMessage());
                }
            });
        } finally {

        }
    }

    private void Login() {
        Backendless.UserService.login(email.getText().toString(),
                pass.getText().toString(), new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        message.setText("You have logined");
                        BackendlessUser user = Backendless.UserService.CurrentUser();
                        userinfo.setText("Email= "+user.getEmail()+"\nName= "+user.getProperty("name"));
                        HideAfterLogin();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        message.setText(fault.getMessage());
                    }
                });
    }

    private void HideAfterLogin(){
        logout_button.setEnabled(true);
        logout_button.setAlpha(1.0f);
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.0f);
        registrate_button.setEnabled(false);
        registrate_button.setAlpha(0.0f);
        email.setEnabled(false);
        email.setAlpha(0.0f);
        name.setEnabled(false);
        name.setAlpha(0.0f);
        pass.setEnabled(false);
        pass.setAlpha(0.0f);
        logreg.setEnabled(false);
        logreg.setAlpha(0.0f);
    }
    private void ShowAfterLogout(){
        logout_button.setEnabled(false);
        logout_button.setAlpha(0.0f);
        loginButton.setEnabled(true);
        loginButton.setAlpha(1.0f);
        email.setEnabled(true);
        email.setAlpha(1.0f);
        pass.setEnabled(true);
        pass.setAlpha(1.0f);
        logreg.setEnabled(true);
        logreg.setAlpha(1.0f);
    }
    private void Logout() {
        final AsyncCallback<Void> outRespond = new AsyncCallback<Void>()
        {
            @Override
            public void handleResponse( Void vvoid )
            {
                message.setText("You have logged out");
                ShowAfterLogout();
                userinfo.setText("");
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                System.out.println( "Error " + backendlessFault.getMessage() );
            }
        };
        Backendless.UserService.logout( outRespond );
    }

}
                                    