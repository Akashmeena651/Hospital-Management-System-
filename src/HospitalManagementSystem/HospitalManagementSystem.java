package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";

    private static final String username = "root";

    private static final String password = "AKmeena@0811";

    public static void main(String[] args)
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Scanner scanner= new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true)
            {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice=scanner.nextInt();

                switch(choice){
                    case 1:
                         // Add patient
                        patient.addpatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patient
                        patient.viewpatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctor
                        doctor.viewdoctors();;
                        System.out.println();
                        break;
                    case 4:
                        //Book appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                        break;

                }


            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner)
    {
        System.out.print("Enter Patient Id: ");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId= scanner.nextInt();
        System.out.print("Enter Appointment date (YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if(patient.getpatientById(patientId) && doctor.getdoctorById(doctorId))
        {
            if(CheckDoctorAvailability(doctorId,appointmentDate,connection))
            {
                String appointmentquery = "INSERT INTO appointments(patient_id, doctor_id, Appointment_date) Values(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int affectedrow = preparedStatement.executeUpdate();
                    if(affectedrow>0)
                    {
                        System.out.println("Appointment Booked");
                    }
                    else
                    {
                        System.out.println("Failed to Book Appointment");
                    }


                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Doctor not available on this date!!");
            }
        }
        else
        {
            System.out.println("Either Doctor or Patient doesn't exist!!!");
        }
    }

public static boolean CheckDoctorAvailability(int doctorId,String appointmentdate,Connection connection) {
    String appointmentquery = "Select COUNT(*) from appointments where doctor_id = ? AND Appointment_date = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(appointmentquery);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setString(2, appointmentdate);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
        {
            int count=rs.getInt(1);
            if(count==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }


    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;

}


}



