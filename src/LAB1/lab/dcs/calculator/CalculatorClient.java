package LAB1.lab.dcs.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CalculatorClient extends JFrame {
    private JTextField txtNum1;
    private JTextField txtNum2;
    private JTextField txtResult;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9090;

    public CalculatorClient() {
        setTitle("Calculator Client-Server TCP");
        setSize(400, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centering the window on the screen
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 5, 12));

        inputPanel.add(new JLabel("First number:"));
        txtNum1 = new JTextField();
        inputPanel.add(txtNum1);

        inputPanel.add(new JLabel("Second number:"));
        txtNum2 = new JTextField();
        inputPanel.add(txtNum2);

        inputPanel.add(new JLabel("Third number:"));
        txtResult = new JTextField();
        txtResult.setEditable(false);
        txtResult.setBackground(Color.WHITE);
        inputPanel.add(txtResult);

        add(inputPanel, BorderLayout.CENTER);

        // The four buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 12, 12, 12));

        JButton btnAdd = new JButton("+");
        JButton btnSub = new JButton("-");
        JButton btnMul = new JButton("*");
        JButton btnDiv = new JButton("/");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSub);
        buttonPanel.add(btnMul);
        buttonPanel.add(btnDiv);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action on each button
        btnAdd.addActionListener((ActionEvent e) -> sendOperation("+"));
        btnSub.addActionListener((ActionEvent e) -> sendOperation("-"));
        btnMul.addActionListener((ActionEvent e) -> sendOperation("*"));
        btnDiv.addActionListener((ActionEvent e) -> sendOperation("/"));
    }

    private void sendOperation(String operator) {
        String num1 = txtNum1.getText().trim();
        String num2 = txtNum2.getText().trim();

        if (num1.isEmpty() || num2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog sa introduci ambele numere!", "Atentie", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Communication through Socket TCP with the server
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // We send the necesary form details, first number, second number, operator
            out.println(num1 + "," + num2 + "," + operator);

            // We read the response and show it to the client
            String response = in.readLine();
            txtResult.setText(response);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to the server!\nAssure that the server is running.", "Socket error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Pornirea interfetei grafice pe thread-ul Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            new CalculatorClient().setVisible(true);
        });
    }
}
