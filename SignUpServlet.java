package myPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String screenName = request.getParameter("screen-name");
        String dobStr = request.getParameter("month") + " " + request.getParameter("day") + ", " + request.getParameter("year");
        String gender = request.getParameter("gender");
        String country = request.getParameter("country");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Format the date to YYYY-MM-DD
        String dob = null;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM d, yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dobDate = inputFormat.parse(dobStr);
            dob = outputFormat.format(dobDate);
        } catch (ParseException e) {
            throw new ServletException("Date parsing error", e);
        }

        String url = "jdbc:mysql://localhost:3306/userdatabase";
        String username = "root";
        String dbPassword = "password";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO users (first_name, last_name, screen_name, dob, gender, country, email, phone, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection conn = DriverManager.getConnection(url, username, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, screenName);
            stmt.setString(4, dob);
            stmt.setString(5, gender);
            stmt.setString(6, country);
            stmt.setString(7, email);
            stmt.setString(8, phone);
            stmt.setString(9, password);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                response.getWriter().println("User registered successfully!");
            } else {
                response.getWriter().println("Failed to register user!");
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error", e);
        }
    }
}
