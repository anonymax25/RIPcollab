package esgi.project.ripcollab;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    private int id;
    private String email;
    private String password;
    private String last_name;
    private String first_name;
    private String birthday;
    private String gender;
    private String avatar;
    private String zip_code;
    private String address;
    private int isBanned;
    private int isAdmin;
    private int isCollaborateur;
    private int idEntreprise;
    private int isDirecteur;

    public User(){

    }

    public User(int id, String email, String password, String last_name, String first_name,
                String birthday, String gender, String avatar, String zip_code, String address,
                int isBanned, int isAdmin, int isCollaborateur) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birthday = birthday;
        this.gender = gender;
        this.avatar = avatar;
        this.zip_code = zip_code;
        this.address = address;
        this.isBanned = isBanned;
        this.isAdmin = isAdmin;
        this.isCollaborateur = isCollaborateur;
    }

    public String toString(){
        String string = "ID: " + id + " nom: " + last_name + " " + first_name;
        return string;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsCollaborateur() {
        return isCollaborateur;
    }

    public void setIsCollaborateur(int isCollaborateur) {
        this.isCollaborateur = isCollaborateur;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public int getIsDirecteur() {
        return isDirecteur;
    }

    public void setIsDirecteur(int isDirecteur) {
        this.isDirecteur = isDirecteur;
    }

    public boolean checkRightPassword(String password){
        final String salt = "SuP4rS4aL4g3";

        if (MD5.getMd5(salt + password).equals(this.password)){
            return true;
        } else {
            return false;
        }
    }



    /*
    public void serialize(){
        try {
            String path = Environment.getExternalStorageDirectory() + "/";

            FileOutputStream fileOut = new FileOutputStream(path + "user.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in /tmp/employee.ser");

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static User unserialize(){
        User user = null;
        try {
            String path = Environment.getExternalStorageDirectory() + "/";

            FileInputStream fileIn = new FileInputStream(path + "/user.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            user = (User) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        return user;
    }
    */
}
