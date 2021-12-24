package viewer;

import javax.swing.*;
import java.sql.*;

public class SQLiteViewer extends JFrame {

    private static JComboBox box;
    private static JButton openButton;
    private static JTextField dbName;
    private static JTextArea textBox;

    public SQLiteViewer() {
        super("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        initComponents();
        actionsHandler();

        setVisible(true);
    }

    private void initComponents()  {
        dbName = new JTextField();
        dbName.setBounds(30, 30, 500,20);
        dbName.setName("FileNameTextField");
        add(dbName);

        openButton = new JButton("Open");
        openButton.setBounds(550, 30, 100, 20);
        openButton.setName("OpenFileButton");
        add(openButton);

        box = new JComboBox();
        box.setName("TablesComboBox");
        box.setBounds(30 , 60, 620, 20);
        add(box);

        textBox = new JTextArea();
        textBox.setName("QueryTextArea");
        textBox.setBounds(30,90,500, 50);
        add(textBox);

        JButton executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");
        executeButton.setBounds(550, 90, 100, 20);
        add(executeButton);
    }

    private void actionsHandler() {

        openButton.addActionListener(x -> {
            String name = dbName.getText();

            if (name != null && name.trim().length() > 0) {
                try {
                    box.removeAllItems();
                    Class.forName("org.sqlite.JDBC");
                    String url = String.format("jdbc:sqlite:%s", name);
                    String query = "SELECT name FROM sqlite_master" +
                            " WHERE type ='table' AND name NOT LIKE 'sqlite_%';";

                    Connection con = DriverManager.getConnection(url);
                    // if (!con.isValid(5)) {return;}
                    Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        String data = rs.getString("name");
                        box.addItem(data);
                    }
                    rs.close();
                    statement.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        box.addActionListener(x -> {
            String tableName = (String)box.getSelectedItem();
            String query = String.format("SELECT * FROM %s;", tableName);
            textBox.setText(query);
        });
    }
}