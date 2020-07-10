package testing;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaServer {

    //initialize socket and input stream
    private Socket		 socket = null;
    private ServerSocket server = null;
    private InputStream in	 = null;
    private OutputStream out = null;

        // constructor with port
	public JavaServer(int port)
        {
            // starts server and waits for a connection
            try
            {
                server = new ServerSocket(port);
                System.out.println("Server started");

                System.out.println("Waiting for a client ...");

                socket = server.accept();

                System.out.println("Client accepted");
                System.out.println();

                //writes on client socket
                out = new DataOutputStream(socket.getOutputStream());
                // takes input from the client socket
                in = new DataInputStream(socket.getInputStream());


                // Receiving data from client
                while (socket!= null){
                    int res = in.read();
                    if(res == -1)
                        break;
                    System.out.println("Received from client : "+res);
                    // send to client
                    out.write(res+1);

                }


                // Receiving data from client
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[293];
//                baos.write(buffer, 0 , in.read(buffer));
//
//                byte[] result = baos.toByteArray();
//                String res = Arrays.toString(result);

                System.out.println("Closing connection");
                // close connection
                socket.close();
                in.close();
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }

        public static void main(String args[])
        {

            new JavaServer(8080);
        }



}
