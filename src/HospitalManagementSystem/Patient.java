package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

   private Connection connection;
   private Scanner scanner;


   Patient(Connection connection, Scanner scanner)
   {
       this.connection=connection;
       this.scanner=scanner;
   }

   public void addpatient()
   {
       System.out.println("Enter Patient Name: ");
       String name=scanner.next();
       System.out.println("Enter Patient Age: ");
       int Age=scanner.nextInt();
       System.out.println("Enter Patient Gender: ");
       String gender=scanner.next();

       try {
           String query="INSERT INTO patients(name, age, gender) Values(?, ?, ?)";
           PreparedStatement preparedstatement = connection.prepareStatement(query);
           preparedstatement.setString(1, name);
           preparedstatement.setInt(2, Age);
           preparedstatement.setString(3, gender);
           int affectedrows=preparedstatement.executeUpdate();
           if(affectedrows>0)
           {
               System.out.println("Patient Added Successfully!!");
           }
           else
           {
               System.out.println("Failed to add Patient!!");
           }

       }catch (SQLException e) {
           e.printStackTrace();
       }

   }

   public void viewpatients()
   {
       String query="Select * from patients";

       try{
           PreparedStatement preparedStatement= connection.prepareStatement(query);
           ResultSet rs= preparedStatement.executeQuery();
           System.out.println("Patients: ");
           System.out.println("+------------+--------------------+----------+--------------+");
           System.out.println("| Patient Id | Name               | Age      | Gender       |");
           System.out.println("+------------+--------------------+----------+--------------+");
           while(rs.next())
           {
               int id = rs.getInt("id");
               String name = rs.getString("name");
               String age = rs.getString("age");
               String gender = rs.getString("gender");
               System.out.printf("|%-12s|%-20s|%-10s|%-14s|\n",id, name, age, gender);
               System.out.println("+------------+--------------------+----------+--------------+");

           }

       }catch(SQLException e)
       {
           e.printStackTrace();
       }
   }

   public boolean getpatientById(int id)
   {
           String query = "Select * from patients where id=?";
       try {
               PreparedStatement preparedStatement = connection.prepareStatement(query);
               preparedStatement.setInt(1, id);
               ResultSet rs = preparedStatement.executeQuery();

               if (rs.next()) {
                   return true;
               } else {
                   return false;
               }
       }catch (SQLException e)
       {
           e.printStackTrace();
       }

       return false;
   }






}
