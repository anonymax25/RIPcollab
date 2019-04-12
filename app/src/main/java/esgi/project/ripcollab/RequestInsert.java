package esgi.project.ripcollab;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RequestInsert {

    private Bdd bdd;
    private String request;
    // private Connection conn = null;
    String result[] = new String[1];
    ArrayList results =new ArrayList(Arrays.asList(result));

    public RequestInsert(Bdd bdd){

        this.bdd = bdd;
    }

    public static Connection startBddReqInsert(Bdd bdd)
    {
        Connection conn = null;
        bdd.startConnect();
        conn = bdd.getConn();
        return conn;
    }




    public ArrayList req(ArrayList arrayInfo,Connection conn){
        try {

            Integer idTrajetExcel = -1;

            if (arrayInfo.get(0) != null)
            {
                arrayInfo.set(0,arrayInfo.get(0));

                ArrayList arrayInfoString = arrayInfo;
                String res2 = String.join(",", arrayInfoString);

                String[] exploded= res2.split(",");


                 idTrajetExcel = Integer.parseInt(exploded[0]);
            }
            else
            {
                arrayInfo.set(0,"NULL");
            }


            // System.out.println(idTrajetExcel);

            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "SELECT idTRajet FROM trajet";
            ResultSet res = state.executeQuery(query);
            int i = 1;
            int count=0;
            System.out.println(arrayInfo);

            while(res.next()) {

                // System.out.println("\tTrajet : "+res.getString("idTrajet"));
                Integer idTrajetBdd = Integer.parseInt(res.getString("idTrajet"));
                if (arrayInfo.get(0) != null && arrayInfo.get(0) != "NULL") {
                    if (idTrajetBdd == idTrajetExcel) {
                        // System.out.println("\t\t* CE TRAJET A DEJA ETE IMPORTER !\n");
                        count++;
                    }

                    i++;
                }
            }
            if (count == 0) {
                PreparedStatement prepare = conn.prepareStatement("INSERT INTO `trajet` (`idTrajet`, `idClient`, `idChauffeur`, `heureDebut`, `heureFin`, `dateResevation`, `distanceTrajet`, `prixtrajet`, `debut`, `fin`, `duration`) VALUES (" +
                        "" + arrayInfo.get(0) + "," +
                        "'" + arrayInfo.get(1) + "'," +
                        "'" + arrayInfo.get(2) + "'," +
                        "'" + arrayInfo.get(3) + "'," +
                        "'" + arrayInfo.get(4) + "'," +
                        "'" + arrayInfo.get(5) + "'," +
                        "'" + arrayInfo.get(6) + "'," +
                        "'" + arrayInfo.get(7) + "'," +
                        "'" + arrayInfo.get(8) + "'," +
                        "'" + arrayInfo.get(9) + "'," +
                        "'" + arrayInfo.get(10) + "');");
                int statut = prepare.executeUpdate();
                if (statut == 1) {
                    System.out.println("Vous avez bien inséré le trajet  dans la base de donnée");
                } else {
                    System.out.println("Il y a eu un problème lors de l'insertion des trajets dans la base de données");

                }
            }


         //   RequestInsert.CheckIfTrajetExist(this.bdd,conn);



    return results;

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            return results;
        }
    }
}
