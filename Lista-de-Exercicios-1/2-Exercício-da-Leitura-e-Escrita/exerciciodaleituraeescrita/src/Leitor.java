public class Leitor implements Runnable {
  
  private final RecursoCompartilhado recurso;
  private final String nome;

  public Leitor(RecursoCompartilhado recurso, String nome) {
      this.recurso = recurso;
      this.nome = nome;
  }

  @Override
  public void run() {
      while (true) {
          recurso.ler(nome);
          try {
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break;
          }
      }
  }
}
