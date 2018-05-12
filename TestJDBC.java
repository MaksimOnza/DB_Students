import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TestJDBC {

    public static void main(String args[]) {
        // Снова используем файл для вывода из-за кодировки
        try {
            System.setOut(new PrintStream("out.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        System.out.println("Copyright 2009, Anton Saburov");
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/students";
            con = DriverManager.getConnection(url, "root", "casperonza_83");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM studentss");
            while (rs.next()) {
                String str = rs.getString(1) + ":" + rs.getString(2);
                printString(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Эта часть позволяет нам закрыть все открытые ресуры
            // В противном случае возмжожны проблемы. Поэтому будьте
            // всегда аккуратны при работе с коннектами
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.err.println("Error: " + ex.getMessage());
            }
       }
    }

    // Снова используем этот метод для вывода из-за кодировки
    public static void printString(Object s) {
        try {
            System.out.println(new String(s.toString().getBytes("utf8"), "utf8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

}