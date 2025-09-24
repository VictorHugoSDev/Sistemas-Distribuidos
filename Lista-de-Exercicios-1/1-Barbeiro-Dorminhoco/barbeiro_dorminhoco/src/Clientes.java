public class Clientes extends Thread {
  
  private final Barbearia barbearia;
  private final int id;

  public Clientes(Barbearia barbearia, int id) {
    this.barbearia = barbearia;
    this.id = id + 1;
  }

  @Override
  public void run() {
    try {
        barbearia.entrarNaBarbearia(id);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Cliente " +  id + " interrompido.");
    }
  }
}
