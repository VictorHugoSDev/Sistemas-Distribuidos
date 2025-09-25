
import java.util.concurrent.locks.StampedLock;

public class RecursoCompartilhado {
  
  private int valor = 0;
  private final StampedLock lock = new StampedLock();

  public void escrever(int novoValor, String nome){
    long stamp = lock.writeLock();
    try {
      System.out.println(nome + " escrevendo valor: " + novoValor);
      valor = novoValor;
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockWrite(stamp);
      System.out.println(nome + " liberou o lock de escrita.");
    }
  }

  public void ler(String nome){
    long stamp = lock.tryOptimisticRead();
    int valorLido = valor;

    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    if(!lock.validate(stamp)){
      stamp = lock.readLock();
      try {
        System.out.println(nome + " não conseguiu ler, obtendo lock de leitura.");
        valorLido = valor;
        Thread.sleep(300);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        lock.unlockRead(stamp);
        System.out.println(nome + " liberou o lock de leitura.");
      }
    }
    System.out.println(nome + " leu o valor: " + valorLido);
  }
}
