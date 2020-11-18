/**
 * An chat server listening on port 10,000
 *
 * @author - JB.
 */

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.charset.StandardCharsets;


public class  chatRoom
{
    public static final int DEFAULT_PORT = 10000;

    // construct a thread pool for concurrency
    private static final Executor exec = Executors.newCachedThreadPool();
    
    
    public static void main(String[] args) throws IOException {
        ServerSocket sock = null;
        
        HashMap<String,Socket> map = new HashMap<String,Socket>(25);
        try {
            // establish the socket
            sock = new ServerSocket(DEFAULT_PORT);

            while (true) {
                /**
                 * now listen for connections
                 * and service the connection in a separate thread.
                 */
                Runnable task = new Connection(sock.accept(),map);
                exec.execute(task);
            }
        }
        catch (IOException ioe) { System.err.println(ioe); }
        finally {
            if (sock != null)
                sock.close();
        }
    }
}

class Handler
{

    /**
     * this method is invoked by a separate thread
     */
    public void process(Socket client) throws java.io.IOException {
        DataOutputStream toClient = null;
        BufferedReader readFrmSvr = new BufferedReader(new InputStreamReader(client.getInputStream()));

        int count = 0;
        byte[] buffer = new byte[10000];


        try {
            
            while (true) {
                System.out.println("enter while loop");

                readFrmSvr = new BufferedReader(new InputStreamReader(client.getInputStream()));

                System.out.println(readFrmSvr.readLine());
                // toClient.writeBytes(message);

                    
                try {
                        Thread.sleep(5000);
                }
                catch (InterruptedException ie) { }

                    count++;
                }
            }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
        finally {
            // close streams and socket
            if (toClient != null)
                toClient.close();
        }
    }
}

class Connection implements Runnable
{
    int count = 0;
    private Socket client;
    private static Handler handler = new Handler();
    
    public Connection(Socket client,HashMap<String,Socket> map) {

        this.client = client;
        map.put("clinet",client);
        System.out.print(map);
    }

    /**
     * This method runs in a separate thread.
     */
    public void run() {
        try {
            handler.process(client);
        }
        catch (java.io.IOException ioe) {
            System.err.println(ioe);
        }
    }
}



