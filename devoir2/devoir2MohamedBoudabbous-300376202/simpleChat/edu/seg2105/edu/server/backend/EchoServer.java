package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import com.lloseng.ocsf.server.*;
import edu.seg2105.client.common.ChatIF;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  private ChatIF serverUI;

  final public static int DEFAULT_PORT = 5555;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   *  @param serverUI The interface type variable.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String message = msg.toString();

    try {
      if (message.startsWith("#login ")) {
      
        if (client.getInfo("loginID") != null) {
          client.sendToClient("Error: Already logged in.");
          client.close();
        } else {
          String loginID = message.substring(7).trim();          
          System.out.println("Message received: " + message + " from null.");        
          client.setInfo("loginID", loginID);
          System.out.println(loginID + " has logged on.");
          client.sendToClient("Welcome, " + loginID + "!");
        }
      } else {
      
        if (client.getInfo("loginID") == null) {
          client.sendToClient("Error: You need to login first.");
          client.close();
        } else {
          
          String loginID = (String) client.getInfo("loginID");
          System.out.println("Message received: " + message + " from " + loginID);         
          String messageToSend = loginID + ": " + message;
          sendToAllClients(messageToSend);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  @Override
  protected void serverStarted() {
      String message = (getPort() == DEFAULT_PORT) 
          ? "Server listening for clients on port " + getPort() 
          : "Server listening for connections on port " + getPort();
      serverUI.display(message);
  }
  

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    serverUI.display("Server has stopped listening for connections.");
  }
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");
  }
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    String loginID = (String) client.getInfo("loginID");
    if (loginID != null) {
      System.out.println(loginID + " has disconnected.");
    } else {
      System.out.println("An unidentified client has disconnected.");
    }
  }
}



  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555
   *                if no argument is entered.
   */
 /**
   public static void main(String[] args) {
    int port = 0; // Port to listen on

    try {
      port = Integer.parseInt(args[0]); // Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; // Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try {
      sv.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
    **/


// End of EchoServer class
