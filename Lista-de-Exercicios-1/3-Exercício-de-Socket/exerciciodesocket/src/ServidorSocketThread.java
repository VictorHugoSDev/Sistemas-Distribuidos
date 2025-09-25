import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServidorSocketThread implements Runnable {

    private final Socket socketClient;

    public ServidorSocketThread(Socket socketClient) {
        this.socketClient = socketClient;
    }

     @Override
    public void run() {
        try (ObjectOutputStream saida = new ObjectOutputStream(socketClient.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socketClient.getInputStream())) {

            while (true) {
                try {
                    Pedido pedido = (Pedido) entrada.readObject();
                    if (pedido == null) break;
                    System.out.println("Pedido recebido: " + pedido);
                    saida.writeObject("Pedido recebido com sucesso: " + pedido);
                    saida.flush();
                } catch (Exception e) {
                    System.out.println("Cliente desconectado: " + socketClient.getRemoteSocketAddress());
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Erro na comunicação com cliente: " + e.getMessage());
        } finally {
            try {
                socketClient.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}