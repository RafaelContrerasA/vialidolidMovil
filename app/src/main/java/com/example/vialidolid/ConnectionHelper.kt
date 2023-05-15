package com.example.vialidolid

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionHelper {

    fun connect(){
        try {
            println("Hola")
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            var conn = DriverManager.getConnection("jdbc:mariadb://137.117.123.255:3306","root","12345678A");
            println("Connection success")
        }catch(ex: SQLException){
            println("Error: ${ex.message}")

        }catch (ex:Exception){
            println("Error: ${ex}")

        }


    }


}