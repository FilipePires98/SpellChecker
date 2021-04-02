import java.util.*;

public class teste_bf {
    public static void main(String[] args) {
        //                                  -----------------Teste Unitário BloomFilter------------------------
        String abc="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        BloomFilter B;
        int count=0;
        Scanner s=new Scanner(System.in);
        boolean corre=true;
        while(corre==true) {
            System.out.println("Quer com controlo de falsos positivos ou nao?(s/n)");
            String res=s.next();
            switch(res){
                case "s":
                    System.out.println("Quantos números irá adicionar?");
                    int numeros=s.nextInt();
                    System.out.println("Qual a probabilidade de falsos positivos que pertende?(0<x<0,5) ");
                    double prob=s.nextDouble();
                    while(prob<0 || prob>=0.5) {
                        System.out.println("Número inválido");
                        prob=s.nextDouble();
                    }
                    B = new BloomFilter(numeros, prob);
                    int k=B.getK();
                    System.out.printf("====Inserindo %d palavras(com tamanho 100)====\n", numeros);
                    for(int h=0; h<numeros; h++) {
                        String a="";
                        for(int i=0; i<100; i++) {
                            int n=(int) Math.floor(Math.random()*abc.length());
                            a=a+abc.charAt(n);
                        }
                        B.insertMember(a, k);
                    }

                    System.out.println("Quantas palavras quer tentar ver se existem no filtro? ");
                    int keys=s.nextInt();
                    count=0;
                    for(int h=0; h<keys; h++) {
                        String a="";
                        for(int i=0; i<100; i++) {
                            int n=(int) Math.floor(Math.random()*abc.length());
                            a=a+abc.charAt(n);
                        }
                        if(B.isMember(a, k)==true) {
                            count++;
                        }
                    }
                    System.out.println((double)count/keys);
                    break;

                case "n":
                    System.out.println("Qual o tamanho que quer que o Bloom Filter tenha? ");
                    int tamanho=s.nextInt();
                    System.out.println("Qual o numero de hash functions que pertende estabelecer neste filtro? ");
                    int hf=s.nextInt();

                    System.out.println("Quantas palavras quer inserir? ");
                    int palavras=s.nextInt();

                    B = new BloomFilter(tamanho);
                    System.out.printf("====Inserindo %d palavras(como tamanho 100)====\n", palavras);
                    for(int h=0; h<palavras; h++) {
                        String a="";
                        for(int i=0; i<100; i++) {
                            int n=(int) Math.floor(Math.random()*abc.length());
                            a=a+abc.charAt(n);
                        }
                        B.insertMember(a, hf);
                    }

                    System.out.println("Quantas palavras quer tentar ver se existem no filtro? ");
                    int chaves=s.nextInt();
                    count=0;
                    for(int h=0; h<chaves; h++) {
                        String a="";
                        for(int i=0; i<100; i++) {
                            int n=(int) Math.floor(Math.random()*abc.length());
                            a=a+abc.charAt(n);
                        }
                        if(B.isMember(a, hf)==true) {
                            count++;
                        }
                    }
                    System.out.println((double)count/chaves);
                    break;

                default:
                    System.out.println("Terminando....");
                    corre=false;
                    break;
            }
        }
    }
}
