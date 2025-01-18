package edu.seg2105.edu.server.ui;

import edu.seg2105.client.common.ChatIF;

import java.io.*;
import java.util.Scanner;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {
    final public static int DEFAULT_PORT = 5555;
    EchoServer server;
    Scanner fromConsole;
    public ServerConsole(int port) {
        server = new EchoServer(port, this);
        fromConsole = new Scanner(System.in);
    }
    public void accept() {
        try {
          String message;
    
          while (true) {
            message = fromConsole.nextLine();
            if (message.startsWith("#")) {
              handleServerCommand(message);
            } else {
             
              String serverMessage = "SERVER MESSAGE> " + message;
              display(serverMessage);
              server.sendToAllClients(serverMessage);
            }
          }
        } catch (Exception ex) {
          System.out.println("Unexpected error while reading from console!");
        }
      }
      private void handleServerCommand(String command) {
        String[] tokens = command.split(" ");
        String cmd = tokens[0];
    
        try {
          switch (cmd) {
            case "#quit":
              server.close();
              System.exit(0);
              break;
    
            case "#stop":
              server.stopListening();
              break;
    
            case "#close":
              server.close();
              break;
    
            case "#setport":
              if (server.isListening() || server.getNumberOfClients() > 0) {
                System.out.println("Error: Cannot change port while server is open.");
              } else if (tokens.length < 2) {
                System.out.println("Error: Port not specified.");
              } else {
                int newPort = Integer.parseInt(tokens[1]);
                server.setPort(newPort);
                System.out.println("Port set to " + newPort);
              }
              break;
    
            case "#start":
              if (server.isListening()) {
                System.out.println("Error: The server is already listening for connections.");
              } else {
                server.listen();
              }
              break;
    
            case "#getport":
              System.out.println("Current port: " + server.getPort());
              break;
    
            default:
              System.out.println("Unknown command: " + cmd);
              break;
          }
        } catch (IOException e) {
          System.out.println("Error: Could not execute the command.");
        } catch (NumberFormatException e) {
          System.out.println("Error: Invalid port number.");
        }
      }    
    @Override
    public void display(String message) {
        System.out.println(message);
    }
    public static void main(String[] args) {
        int port = DEFAULT_PORT; 
    
        try {
          if (args.length > 0) {
            port = Integer.parseInt(args[0]); 
          }
        } catch (Throwable t) {
          System.out.println("Invalid port number. Using default port.");
        }
    
        ServerConsole serverConsole = new ServerConsole(port);
    
        try {
          serverConsole.server.listen(); 
        } catch (Exception ex) {
          System.out.println("ERROR - Could not listen for clients!");
        }
    
        serverConsole.accept(); 
    }

}
