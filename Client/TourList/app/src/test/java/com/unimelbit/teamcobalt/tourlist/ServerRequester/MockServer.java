package com.unimelbit.teamcobalt.tourlist.ServerRequester;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by awhite on 6/10/17.
 */

public class MockServer implements Runnable {

    int port;
    boolean isRunning;
    ServerSocket serverSocket;

    public MockServer(int port) {
        this.port = port;
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        try {
            isRunning = false;
            if (null != serverSocket) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException e) {
            Log.e("server", "Error closing the server socket.", e);
        }
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                handle(socket);
                socket.close();
            }
        } catch (SocketException e) {
            // The server was stopped; ignore.
        } catch (IOException e) {
            Log.e("server", "Web server error.", e);
        }

    }

    private void handle(Socket socket) throws IOException {
        BufferedReader reader = null;
        PrintStream output = null;
        try {
            String result = null;

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            line = reader.readLine();
            while (line != null) {

                if (line.startsWith("GET /")) {
                    result = "GET";
                    break;
                }
                if (line.startsWith("POST /")) {
                    result = "POST";
                    break;
                }
                if (line.startsWith("PUT /")) {
                    result = "PUT";
                    break;
                }

                line = reader.readLine();
            }

            // Output stream that we send the response to
            output = new PrintStream(socket.getOutputStream());

            // Prepare the content to send.
            if (null == result) {
                writeServerError(output);
                return;
            }
            byte[] bytes = new byte[1024];

            // Send out the content.
            output.println("HTTP/1.0 200 OK");
            output.println("Content-Type: " + "json");
            output.println("Content-Length: " + 1024);
            output.println();
            output.println(result);
            output.flush();
        } finally {
            if (null != output) {
                output.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }

}
