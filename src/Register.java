import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Register extends JDialog {
    private JPanel contentPane;
    private JTextField usr_name;
    private JTextField usr_email;
    //private JLabel email;
    private JTextField usr_phone;
    private JTextField usr_addr;
    private JTextField usr_dob;
    private JPasswordField usr_pwd;
    private JPasswordField cfm_pwd;
    private JButton btnRegister;
    private JButton btnCancel;


    public Register(JFrame parent) {
        super(parent);
        setTitle("Create Account");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(450, 475));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }


    private void registerUser() {
        String name = usr_name.getText();
        String email = usr_email.getText();
        String phone = usr_phone.getText();
        String addr = usr_addr.getText();
        String dob = usr_dob.getText();
        String password = String.valueOf(usr_pwd.getPassword());
        String cfm_password = String.valueOf(cfm_pwd.getPassword());

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || addr.isEmpty() || dob.isEmpty() || password.isEmpty() || cfm_password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fill all Fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        if(!password.equals(cfm_password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "The two passwords do not match",
                    "Re-enter Password",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        user = addUserToDatabase(name, email, phone, addr, dob, password);

        if(user != null) {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to register user",
                    "Create User",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String date_of_birth, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:5000/javaregistration?serverTimezone=CAT";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stm = conn.createStatement();
            String sql = "INSERT INTO users(name, email, phone, addr, dob, password) VALUES(?,?,?,?,?,?)";
            PreparedStatement prepstm = conn.prepareStatement(sql);
            prepstm.setString(1, name);
            prepstm.setString(2, email);
            prepstm.setString(3, phone);
            prepstm.setString(4, address);
            prepstm.setString(5, date_of_birth);
            prepstm.setString(6, password);

            int addedRows = prepstm.executeUpdate();
            if(addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.addr = address;
                user.dob = date_of_birth;
                user.password = password;
            }

            stm.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        Register regForm = new Register(null);
        User user = regForm.user;

        if(user != null) {
            System.out.println(user.name + " registered successfully!");
        }
        else {
            System.out.println("Registration cancelled");
        }
    }
}
