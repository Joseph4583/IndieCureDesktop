package DataBaseManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseMainManager {

    public void DataBaseExist() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/indiecuredb","root","");
            System.out.print("Base de datos encontrada");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("No se encontro la base de datos");
        }

    }

}
