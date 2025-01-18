package edu.seg2105.client.backend;

import com.lloseng.ocsf.client.*;
import java.io.*;
import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author 
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  /**
   * The client's login ID.
   */
  private String loginID;

  /**
   * Flag to indicate if the disconnection was initiated by the client.
   */
  private boolean isInitiatedByClient = false;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param loginID  The login ID for the client.
   * @param clientUI The interface type variable.
   * @throws IOException if an I/O error occurs when opening the connection.
   */
  public ChatClient(String host, int port, String loginID, ChatIF clientUI) throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI.
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
    try {
      if (message.startsWith("#")) {
        handleClientCommand(message);
      } else {
        sendToServer(message);
      }
    } catch (IOException e) {
      clientUI.display("Could not send message to server. Terminating client.");
      quit();
    }
  }

  /**
   * Handles commands entered by the client.
   *
   * @param command The command entered by the client.
   */
  private void handleClientCommand(String command) {
    String[] tokens = command.split(" ");
    String cmd = tokens[0];

    try {
      switch (cmd) {
        case "#quit":
          isInitiatedByClient = true;
          closeConnection();
          System.exit(0);
          break;

        case "#logoff":
          if (isConnected()) {
            isInitiatedByClient = true;
            closeConnection();
          } else {
            clientUI.display("Error: Not connected.");
          }
          break;

        case "#sethost":
          if (isConnected()) {
            clientUI.display("Error: Cannot change host while connected.");
          } else if (tokens.length < 2) {
            clientUI.display("Error: Host not specified.");
          } else {
            setHost(tokens[1]);
            clientUI.display("Host set to " + tokens[1]);
          }
          break;

        case "#setport":
          if (isConnected()) {
            clientUI.display("Error: Cannot change port while connected.");
          } else if (tokens.length < 2) {
            clientUI.display("Error: Port not specified.");
          } else {
            int newPort = Integer.parseInt(tokens[1]);
            setPort(newPort);
            clientUI.display("Port set to " + newPort);
          }
          break;

        case "#login":
          if (isConnected()) {
            clientUI.display("Error: Already connected.");
          } else {
            openConnection();
          }
          break;

        case "#gethost":
          clientUI.display("Current host: " + getHost());
          break;

        case "#getport":
          clientUI.display("Current port: " + getPort());
          break;

        default:
          clientUI.display("Unknown command: " + cmd);
          break;
      }
    } catch (IOException e) {
      clientUI.display("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
      clientUI.display("Error: Invalid port number.");
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
      // Ignore exception
    }
    System.exit(0);
  }

  // Methods overridden from AbstractClient **************************

  /**
   * Hook method called after the connection has been closed.
   */
  @Override
  protected void connectionClosed() {
    if (isInitiatedByClient) {
      clientUI.display("Connection closed.");
    } else {
      clientUI.display("The server has shut down.");
    }
    isInitiatedByClient = false; // Reset the flag for future connections
  }

  /**
   * Hook method called each time an exception is thrown by the
   * client's thread that is waiting for messages from the server.
   *
   * @param exception the exception raised.
   */
  @Override
  protected void connectionException(Exception exception) {
      clientUI.display("The server has shut down.");
      isInitiatedByClient = false;
      quit();
  }
  

  /**
   * Hook method called after a connection has been established.
   */
  @Override
  protected void connectionEstablished() {
    clientUI.display("Connection established.");
    try {
      sendToServer("#login " + loginID);
      clientUI.display(loginID + " has logged on.");
    } catch (IOException e) {
      clientUI.display("Error: Could not send login ID to server.");
    }
  }
}
// End of ChatClient class
