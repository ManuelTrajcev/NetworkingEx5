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
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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
            String[] parts = response.split(":");
            writer.write(parts[1] + ":" + parts[0] + "\n");
            writer.flush();


        }
    }
}
