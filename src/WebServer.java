import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Vincent on 6/8/2017.
 */


public class WebServer {

    private static final int PORT_NUMBER=8080;

    int portNum;


    public WebServer(int portNum){
        this.portNum = portNum;

        callServer();
    }

    private void callServer(){

        try {
            ServerSocket serverSocket = new ServerSocket(portNum);

            while (true) {

                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String input;
                String url;
                String getArray[] = null;
                String data = "";
                while ((input = br.readLine()) != null) {

                    if (input.contains("GET")) {
                        getArray = input.split(" ", 3);
                    }
                }

                url = getArray[1];

                File file = new File("www" + url);
                PrintStream outStream = new PrintStream(socket.getOutputStream(), false);

                if (file.exists()) {

                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                    data="";
                    while ((input = fileReader.readLine()) != null) {
                        data += input;
                    }
                    String header = "HTTP/1.1 200 OK\nContent-type: text/html\nContent-length: " + data.length();
                    outStream.println(header + data);
                    outStream.flush();

                } else {

                    file = new File("www/notfound.html");
                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                    data = "";

                    while ((input = fileReader.readLine()) != null) {
                        data += input;
                    }

                    String header = "HTTP/1.1 404 Not Found\nContent-type: text/html\nContent-length: " + data.length();
                    outStream.println(header + data);
                    outStream.flush();
                }
                socket.close();

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WebServer(PORT_NUMBER);
    }

}
