import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barbearia {
  
  private final int cadeiras;
  private int clientesEsperando = 0;

  private final Lock lock = new ReentrantLock(true);
  private final Condition clienteDisponivel = lock.newCondition();
  private final Condition barbeiroDisponivel = lock.newCondition();

  public Barbearia(int cadeiras) {
    this.cadeiras = cadeiras;
  }
  
  public void cortarCabelo() throws InterruptedException {
    lock.lock();
    try {
      while(clientesEsperando == 0) {
        System.out.println("Barbeiro dormindo...");
        clienteDisponivel.await();
      }
      clientesEsperando--;
      System.out.println("Cliente sendo atendido. Clientes esperando: " + clientesEsperando);
      barbeiroDisponivel.signal();
    } finally {
      lock.unlock();
    }
    Thread.sleep(3000);
    System.out.println("Barbeiro terminou o corte.");
  }

  public void entrarNaBarbearia(int idCliente) throws InterruptedException {
    lock.lock();
    try {
        if (clientesEsperando < cadeiras) {
      clientesEsperando++;
      System.out.println("Cliente " + idCliente + " entrou e está esperando. Total de clientes esperando: " + clientesEsperando);
      clienteDisponivel.signal(); // acorda barbeiro
      barbeiroDisponivel.await(); // espera ser atendido
      System.out.println("Cliente " + idCliente + " está sendo atendido.");
      } else {
      System.out.println("Cliente " + idCliente + " foi embora (barbearia cheia).");
      }
    } finally {
      lock.unlock();
    }
  }
}
