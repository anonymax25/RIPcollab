package esgi.project.ripcollab;

import android.content.Intent;
import android.net.http.AndroidHttpClient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

import static org.apache.http.protocol.HTTP.USER_AGENT;


public class MainActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private TextView Info;
    private TextView textView;
    private Button Login;
    private int counter = 5;

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

        String stringURL = "http://192.168.43.220:80/mrbriatte/esgiPark/api/users/list.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                stringURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{

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

                                        if (user.checkRightPassword(password)){
                                            if (user.getIsCollaborateur() == 1){
                                                System.out.println("Connected");

                                                Intent intent = new Intent(MainActivity.this, CollabHome.class);

                                                //user.serialize();
                                                intent.putExtra("SESSION", user);
                                                intent.putExtra("SESSION_ID", user.getId());
                                                intent.putExtra("SESSION_EMAIL", user.getEmail());
                                                intent.putExtra("SESSION_PASSWORD", user.getPassword());
                                                intent.putExtra("SESSION_LAST_NAME", user.getLast_name());
                                                intent.putExtra("SESSION_FIRST_NAME", user.getFirst_name());
                                                intent.putExtra("SESSION_BIRTHDAY", user.getBirthday());
                                                intent.putExtra("SESSION_GENDER", user.getGender());
                                                intent.putExtra("SESSION_AVATAR", user.getAvatar());
                                                intent.putExtra("SESSION_ZIP_CODE", user.getZip_code());
                                                intent.putExtra("SESSION_ADDRESS", user.getAddress());
                                                intent.putExtra("SESSION_ISBANNED", user.getIsBanned());
                                                intent.putExtra("SESSION_ISADMIN", user.getIsAdmin());
                                                intent.putExtra("SESSION_ISCOLLABORATEUR", user.getIsCollaborateur());



                                                System.out.println("Connected 2");
                                                startActivity(intent);

                                            } else {
                                                System.out.println("Not collaborateur!");
                                                Info.setText("Vous n'etes pas un collaborateur!\nInscrivez vous sur le site\nAttempts remaining: " + counter);
                                            }
                                        } else {
                                            System.out.println("bad password!");
                                            counter--;

                                            Info.setText("Attempts remaining: " + counter);

                                            if (counter == 0){
                                                Login.setEnabled(false);
                                            }

                                        }
                                    } else {
                                        System.out.println("Incorrect email!");
                                        Info.setText("Wrong email\nAttempts remaining: " + counter);
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
