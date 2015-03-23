package chatappclient;

import ClientGUIPackage.WriteToClientGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnect extends Thread {

    WriteToClientGUI gui;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    

    public ClientConnect(WriteToClientGUI g) {
        gui = g;
        serverPort = 1234;

    }
    
    public void listenConnectRead() {
        Thread a = new Thread(new Runnable() {
            public void run() {

                try {
                    socket = new Socket("localhost", serverPort);
                    //System.out.println("Connected to: " + socket.getPort());
                    gui.write("You are now connected!");
                    //System.out.println("Connected to: " + socket.getInetAddress());
                    output = new ObjectOutputStream(socket.getOutputStream());
                    output.flush();
                    //System.out.println("Client's output stream is ok");
                    input = new ObjectInputStream(socket.getInputStream());
                    //System.out.println("Client's input stream is ok");

                    while (true) {
                        try {
                            //System.out.println("Trying to read messages now.");
                            String message = (String) input.readObject();
                            //System.out.println("Read a message from the server:");
                            gui.write(message);
                            if (message.equals("BYE")) {
                                gui.write("Bye!");
                                break;
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            gui.write("Server has disconnected.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    gui.write("Something went wrong.");
                }

            }

        });
        a.start();
    }

    public void sendMessage(final String m) {
        Thread b = new Thread(new Runnable() {
            public void run() {

                try {
                    String message = m;
                    output.writeObject("Client: " + message);
                    output.flush();
                    //System.out.println("Sent your message.");
                } catch (IOException e) {
                    gui.write("Can't send your message.");
                }

            }

        });
        b.start();
    }

}
