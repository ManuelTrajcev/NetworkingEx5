import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    int port;

    public Server(int port) {
        this.port = port;
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
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("SERVER: starting...");
        while (true) {
            System.out.println("SERVER: waiting for connection...");
            Socket socket = serverSocket.accept();
            System.out.println("SERVER: client accepted...");
            Worker worker = new Worker(socket);
            worker.start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9753);
        server.start();
    }
}
