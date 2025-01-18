package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client. It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF {
  // Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginID) {
    try {
      client = new ChatClient(host, port, loginID, this);

    } catch (IOException exception) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    }

    fromConsole = new Scanner(System.in);
  }


  // Instance methods ************************************************

  /**
   * This method waits for input from the console. Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept() {
    try {
      String message;

      while (true) {
        message = fromConsole.nextLine();
        if (message.startsWith("#")) {
          handleClientCommand(message);
        } else {
          client.handleMessageFromClientUI(message);
        }
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }
  private void handleClientCommand(String command) {
    String[] tokens = command.split(" ");
    String cmd = tokens[0];

    try {
      switch (cmd) {
        case "#quit":
          client.closeConnection();
          System.exit(0);
          break;

        case "#logoff":
          client.closeConnection();
          break;

        case "#sethost":
          if (client.isConnected()) {
            System.out.println("Error: Cannot change host while connected.");
          } else if (tokens.length < 2) {
            System.out.println("Error: Host not specified.");
          } else {
            client.setHost(tokens[1]);
            System.out.println("Host set to " + tokens[1]);
          }
          break;

        case "#setport":
          if (client.isConnected()) {
            System.out.println("Error: Cannot change port while connected.");
          } else if (tokens.length < 2) {
            System.out.println("Error: Port not specified.");
          } else {
            int newPort = Integer.parseInt(tokens[1]);
            client.setPort(newPort);
            System.out.println("Port set to " + newPort);
          }
          break;

        case "#login":
          if (client.isConnected()) {
            System.out.println("Error: Already connected.");
          } else {
            client.openConnection();
          }
          break;

        case "#gethost":
          System.out.println("Current host: " + client.getHost());
          break;

        case "#getport":
          System.out.println("Current port: " + client.getPort());
          break;

        default:
          System.out.println("Unknown command: " + cmd);
          break;
      }
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Error: Invalid port number.");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface. It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
    public static void main(String[] args) {
    String host = "localhost";
    int port = DEFAULT_PORT;

    if (args.length < 1) {
      System.out.println("ERROR - No login ID specified. Connection aborted.");
      System.exit(0);
    }

    String loginID = args[0];

    if (args.length > 1) {
      host = args[1];
    }

    if (args.length > 2) {
      try {
        port = Integer.parseInt(args[2]);
      } catch (NumberFormatException e) {
        System.out.println("Invalid port number. Using default port.");
      }
    }

    ClientConsole chat = new ClientConsole(host, port, loginID);
    chat.accept(); // Wait for console data
  }
}
// End of ConsoleChat class
