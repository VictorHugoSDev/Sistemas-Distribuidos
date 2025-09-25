public class App {
    public static void main(String[] args) throws Exception {
        RecursoCompartilhado recurso = new RecursoCompartilhado();

        for (int i = 0; i <= 5; i++) {
            new Thread(new Leitor(recurso, "Leitor - " + i)).start();
        }

        new Thread(new Escritor(recurso, "Escritor - 1", 100)).start();
        new Thread(new Escritor(recurso, "Escritor - 2",200)).start();
    }
}
