import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws Exception {
        try (ServerSocket s = new ServerSocket(50000, 10)) {
            System.out.println("Servidor ativo na porta 50000. Aguardando conexões...");
            while (true) {
                Socket conexao = s.accept();
                System.out.println("Conexão estabelecida");
                ServidorSocketThread thread = new ServidorSocketThread(conexao);
                new Thread(thread).start();
            }
        }
    }
}