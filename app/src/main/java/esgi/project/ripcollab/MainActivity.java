package esgi.project.ripcollab;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private TextView Info;
    private TextView textView;
    private Button Login;
    private int counter = 5;
    private static final String apiURI = "http://192.168.1.24:80/-WEB-R.I.P-Project/API/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        textView = (TextView) findViewById(R.id.txt);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RequestQueue req = Volley.newRequestQueue();

                System.out.println("click");
                validate(Email.getText().toString(),Password.getText().toString());
            }
        });
    }



    private void validate(final String email, final String password){

        String stringURL = apiURI + "users/list.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                stringURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Info.setText(" ");
                            JSONArray jsonarray = new JSONArray(response.toString());
                            //System.out.println(jsonarray);
                            ArrayList<String> emails = new ArrayList<String>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                if (jsonarray.getJSONObject(i) != null){

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);


                                    User user = new User(jsonobject.getInt("id"),jsonobject.getString("email"),jsonobject.getString("password"),
                                            jsonobject.getString("last_name"),jsonobject.getString("first_name"), jsonobject.getString("birthday"),
                                            jsonobject.getString("gender"),jsonobject.getString("avatar"),jsonobject.getString("zip_code") == null?"none":jsonobject.getString("zip_code"),
                                            jsonobject.getString("address") == null?"none":jsonobject.getString("address"),
                                            jsonobject.getInt("isBanned"),jsonobject.getInt("isAdmin"),jsonobject.getInt("isCollaborateur"));

                                    if (email.equals(user.getEmail())){
                                        i=jsonarray.length();
                                        if (user.checkRightPassword(password)){
                                            if (user.getIsCollaborateur() == 1){
                                                Info.setText("Attempts remaining: " + counter);
                                                Intent intent = new Intent(MainActivity.this, CollabHomeActivity.class);
                                                intent.putExtra("SESSION_USER", user);
                                                intent.putExtra("apiURI", apiURI);
                                                startActivity(intent);
                                            } else {
                                                Info.setText("Vous n'etes pas un collaborateur!\nInscrivez vous sur le site\nAttempts remaining: " + counter);
                                            }
                                        } else {
                                            counter--;
                                            Info.setText("Bad password\nAttempts remaining: " + counter);
                                            Toast.makeText(MainActivity.this, "Attempts remaining: " + counter,Toast.LENGTH_LONG).show();

                                            if (counter == 0){
                                                Login.setEnabled(false);
                                            }

                                        }
                                    } else {
                                        Info.setText("Incorrect email\nAttempts remaining: " + counter);
                                    }
                                    //emails.add(jsonobject.getString("email"));
                                    //System.out.println(jsonobject.toString(2));
                                }

                            }
                            /*
                            for (int i = 0; i < jsonarray.length(); i++) {
                                System.out.println(emails);
                            }
                            */



                        }catch(org.json.JSONException e){
                            Log.e("ERR", "json err onResponse: ", e );
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERR", "http err onResponse: " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
