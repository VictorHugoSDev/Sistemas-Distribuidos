public class Escritor implements Runnable {

  private final RecursoCompartilhado recurso;
  private final String nome;
  private int valorInicial;

  public Escritor(RecursoCompartilhado recurso, String nome, int valorInicial) {
      this.recurso = recurso;
      this.nome = nome;
      this.valorInicial = valorInicial;
  }

  @Override
  public void run() {
    int valor = valorInicial;
    while (true) {
        recurso.escrever(valor++, nome);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
    }
  }
}
