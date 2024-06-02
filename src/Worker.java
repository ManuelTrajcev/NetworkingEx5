import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Worker extends Thread {
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
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
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Random random = new Random();
            String response = reader.readLine();
            System.out.println("SERVER received: " + response);

            if (random.nextInt(2) != 0) {
                writer.write("ERROR WHILE LOGGING IN\n");
                System.out.println("ERROR");
                writer.flush();
                socket.close();
            } else {
                System.out.println("CLIENT logged in");
                String[] pair = response.split(":");
                writer.write(pair[1] + ":" + pair[0] + "\n");
                writer.flush();

                while (true) {
                    response = reader.readLine();
                    String parts[] = response.split(":");
                    if (parts[1].equals("over")) {
                        writer.write("Disconnecting...\n");
                        writer.flush();
                        System.out.println("SERVER: closing connection...");
                        break;
                    } else if (parts[1].equals("attach")) {
                        System.out.println("SERVER: client sending file");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
            reader.close();
            socket.close();
        }
    }
}
