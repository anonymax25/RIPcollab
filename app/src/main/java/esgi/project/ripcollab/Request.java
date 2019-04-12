package esgi.project.ripcollab;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Request {
    private Bdd bdd;
    private String request;
    // private Connection conn = null;
    String result[] = new String[1];
    ArrayList results =new ArrayList(Arrays.asList(result));

    public Request(Bdd bdd){

        this.bdd = bdd;
    }

    public ArrayList req(String request, String column){
            try {
            Connection conn = null;
            bdd.startConnect();
            conn = bdd.getConn();
            Statement stmt = null;
            stmt = conn.createStatement();

            String sql = request;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //Retrieve by column name
                result[0] = rs.getString(column);

                results = new ArrayList(Arrays.asList(result));
                System.out.println(results);
            }
            bdd.stopConnect(conn);
             rs.close();
            return results;


        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            return results;
        }
    }

}
