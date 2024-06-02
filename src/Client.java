import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private InetAddress serverAddress;
    private int serverPort;
    private File logFile;

    public Client(InetAddress serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.logFile = new File("./LogFile.txt");
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute() throws IOException {
        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        BufferedWriter fileWriter = null;
        try {
            socket = new Socket(serverAddress.getHostAddress(), serverPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile)));

            writer.write("hello:221046\n");
            writer.flush();

            String response = reader.readLine();
            while (response.equals("ERROR WHILE LOGGING IN")) {
                fileWriter.append(response + "\n");
                fileWriter.flush();
                writer.flush();
                System.out.println("CLIENT received: " + response);
                socket = new Socket(serverAddress.getHostAddress(), serverPort);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writer.write("hello:221046\n");
                writer.flush();
                response = reader.readLine();
            }
            System.out.println("CLIENT received: " + response);
            fileWriter.append(response + "\n");
            fileWriter.flush();
            writer.flush();

            //TODO send file

            writer.write("221046:attach:filename.txt\n");
            writer.flush();

            writer.write("221046:fileSize:135\n");
            writer.flush();


            writer.write("221046:over\n");
            writer.flush();
            response = reader.readLine();
            fileWriter.append(response + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
            fileWriter.flush();
            fileWriter.close();
            reader.close();
            socket.close();
        }
    }

    public static void main(String[] args) {
        Client client = null;
        try {
            InetAddress serverAddress = InetAddress.getByAddress(new byte[]{(byte) 194, (byte) 149, (byte) 135, (byte) 49});
            client = new Client(InetAddress.getByName("localhost"), 9753);
            client.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }
}
