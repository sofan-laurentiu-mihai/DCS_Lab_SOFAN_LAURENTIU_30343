package LAB1.lab.dcs.calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
    public static final int PORT = 9090;

    public static void main(String[] args) {
        System.out.println("Computer server on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // We will wait for connections from the client
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // We treat each client on a separate thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request;
            // We read the request in the form of e.g. +, -, 12,....
            while ((request = in.readLine()) != null) {
                String[] parts = request.split(",");
                if (parts.length != 3) {
                    out.println("Error: Invalid format received!");
                    continue;
                }

                try {
                    double n1 = Double.parseDouble(parts[0].trim());
                    double n2 = Double.parseDouble(parts[1].trim());
                    String op = parts[2].trim();

                    double result;
                    switch (op) {
                        case "+":
                            result = n1 + n2;
                            out.println(result);
                            break;
                        case "-":
                            result = n1 - n2;
                            out.println(result);
                            break;
                        case "*":
                            result = n1 * n2;
                            out.println(result);
                            break;
                        case "/":
                            if (n2 == 0) {
                                out.println("Error: Division by 0!");
                            } else {
                                result = n1 / n2;
                                out.println(result);
                            }
                            break;
                        default:
                            out.println("Error: Unknown operator (" + op + ")!");
                    }
                } catch (NumberFormatException ex) {
                    out.println("Error: Values are not valid numbers!");
                }
            }
        } catch (Exception e) {
            System.out.println("Connection closed for client.");
        }
    }
}