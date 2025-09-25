import java.io.Serializable;

public class Pedido implements Serializable {
    private String cpf;
    private double valor;

    public Pedido(String cpf, double valor) {
        this.cpf = cpf;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Pedido [cpf=" + cpf + ", valor=" + valor + "]";
    }

    public String getCPF() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
}
