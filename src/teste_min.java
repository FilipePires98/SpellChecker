import java.util.*;

public class teste_min {
    public static void main(String[] args) {
        //                                ----------------------Teste Unitário do módulo de Similhanças------------------
        Scanner s=new Scanner(System.in);
        boolean corre=true;
        while(corre==true) {
            System.out.println("Insira uma palavra(1 para sair do programa): ");
            String res=s.next();
            if(res.equals("1")) {
                System.out.println("Terminando...");
                break;
            }
            Palavra word=new Palavra(res);
            while(true) {
                System.out.println("Insira uma palavra para comparar com a anterior(1 para mudar palavra base): ");
                String comp=s.next();
                if(comp.equals("1")) {
                    break;
                }
                System.out.println("Insira também o tamanho que assinatura: ");
                int ass=s.nextInt();
                System.out.println("Insira também a similaridade mínima que quer aceitar: ");
                double sim=s.nextDouble();
                System.out.println("As palavras tem uma similaridade de " + word.compare(comp, ass, sim));
            }
        }
    }
}