public class App {
    public static void main(String[] args) throws Exception {
        Barbearia barbearia = new Barbearia(3); // 3 cadeiras na barbearia
        Barbeiro barbeiro = new Barbeiro(barbearia);
        barbeiro.start();

        for (int i = 0; i < 10; i++) { // 10 clientes tentando entrar na barbearia
            Clientes cliente = new Clientes(barbearia, i);
            cliente.start();
            try{
                 Thread.sleep(2000); // Novo cliente a cada 2 segundos
            }
           catch(InterruptedException e){
                e.printStackTrace();;
            }
        }
    }
}