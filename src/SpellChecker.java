import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SpellChecker {
    public static Scanner sc = new Scanner(System.in);
    // Funções:
    public static List<String> dict_creator() {
        List<String> dicionario = new ArrayList<>();
        try {
            System.out.println("Would you like to write in English (EN) or Portuguese (PT)?");
            Path dic; Charset charset = Charset.defaultCharset();
            switch(sc.nextLine()) {
                case "EN":
                    dic = new File("EN_dictionary.txt").toPath();
                    break;
                case "en":
                    dic = new File("EN_dictionary_short.txt").toPath();
                    break;
                case "PT":
                    dic = new File("PT_dicionário.txt").toPath();
                    break;
                default:
                    System.out.println("Warning: Invalid Input. Dictionary set to EN.");
                    dic = new File("EN_dictionary.txt").toPath();
            }
            dicionario = Files.readAllLines(dic, charset);
            return dicionario;
        } catch(IOException e) {
            System.out.println("Unable to upload dictionary.");
            System.exit(1);
        }
        return dicionario;
    }

    public static String[] word_processor(String[] nao_membros, List<String> dicionario,boolean autocorrect) {
        for(int i = 0; i < nao_membros.length; i++) {
            if(nao_membros[i] != null) {                                                // se posicao estiver ocupada por nao_membro
                Palavra palavra = new Palavra(nao_membros[i]);
                HashMap<String,Double> candidatos = new HashMap<>();
                String m_candidato1 = ""; double mc1 = 0;
                String m_candidato2 = ""; String m_candidato3 = ""; String m_candidato4 = ""; String m_candidato5 = "";
                for (String p: dicionario) {
                    if (nao_membros[i].substring(0,0).equals(p.substring(0,0))) {       // se começam pela mesma letra
                        double c = palavra.compare(p,200,0.4);
                        if(c != 0) {
                            //System.out.println(c);
                            candidatos.put(p,c);
                            if(c > mc1) {
                                m_candidato5 = m_candidato4;
                                m_candidato4 = m_candidato3;
                                m_candidato3 = m_candidato2;
                                m_candidato2 = m_candidato1;
                                mc1 = c; m_candidato1 = p;
                            }
                        }
                    }
                }
                if (m_candidato1.equals("")) {
                    System.out.printf("Warning: no alternatives to the word %s where found.\n",nao_membros[i]);
                } else {
                    if(autocorrect == true) {
                        nao_membros[i] = m_candidato1;
                    } else {
                        if (m_candidato4.equals("")) {
                            System.out.printf("Choose one of the options (to replace %s):\n > %s (1)\n > %s (2)\n > %s (3)\n",nao_membros[i],m_candidato1,m_candidato2,m_candidato3);
                        } else {
                            System.out.printf("Choose one of the options (to replace %s):\n > %s (1)\n > %s (2)\n > %s (3)\n > %s (4)\n > %s (5)\n",nao_membros[i],m_candidato1,m_candidato2,m_candidato3,m_candidato4,m_candidato5);
                        }
                        switch(sc.nextLine()) {
                            case "1":
                                nao_membros[i] = m_candidato1;
                                break;
                            case "2":
                                nao_membros[i] = m_candidato2;
                                break;
                            case "3":
                                nao_membros[i] = m_candidato3;
                                break;
                            case "4":
                                nao_membros[i] = m_candidato4;
                                break;
                            case "5":
                                nao_membros[i] = m_candidato5;
                                break;
                            default:
                        }
                    }
                }
            }
        }
        return nao_membros;
    }

    // MAIN:
    public static void main(String[] args) {
        // Apresentação:
        System.out.println("\nInitializing the program...\n" +
                "This is an Input Corrector Program. Once the user writes the phrase to be analysed, \n" +
                "the program (according to the user's desire) either automatically corrects all the \n" +
                "wrong words or gives alternative words for the user to choose from. The program ends \n" +
                "whenever the users want's it to.\n");

        // Criação do Bloom Filter e inserção de todas as palavras do dicionário escolhido (PT / EN):
        List<String> dicionario = dict_creator();

        BloomFilter bf = new BloomFilter(dicionario.size(),0.1);
        for (int j = 0; j < dicionario.size(); j++) {
            bf.insertMember(dicionario.get(j),bf.getK());
        }

        //Decisão do utilizador da forma de correção de palavras:
        boolean autocorrect = true;
        String answer = "";
        while(!answer.equals("y") && !answer.equals("n")) {
            System.out.println("Would you like to activate autocorrect? (y/n)");
            answer = sc.nextLine();
        }
        if(answer.equals("n")) {
            autocorrect = false;
        }

        // Programa:
        while(true) {
            // elementos da frase do user:
            System.out.println("\nUser input (start with a word and use lowercase; type EXIT to exit the program):");
            String frase_user = sc.nextLine();
            String[] palavras_user = frase_user.split("\\W+");//nao resolve o problema no caso do utilizador comecar a frase com pontuacao
            String[] pontuacao_user = frase_user.split("\\w+");
            if (!palavras_user[0].equals("")) {
                if ((palavras_user[0].toUpperCase()).equals("EXIT")) {
                    break;
                }
            } else {
                System.out.println("Invalid Input.");
                continue;
            }

            // verificacao da existencia dos elementos no dicionario:
            String[] membros = new String[palavras_user.length];
            String[] nao_membros = new String[palavras_user.length];
            for(int i = 0; i < palavras_user.length; i++) {
                if(bf.isMember(palavras_user[i],bf.getK())) {
                    membros[i] = palavras_user[i];
                } else {
                    nao_membros[i] = palavras_user[i];
                }
            }

            // impressao dos resultados:
            /*
            System.out.println("\nWords that belong to the dictionary:");
            for(int j = 0; j < palavras_user.length; j++) {
                if (membros[j] != null) {
                    System.out.printf(" > %s\n", membros[j]);
                }
            }
            */

            // impressao da frase do user inicial remontada:
            /*
            System.out.println("\nInitial user phrase:");
            if(palavras_user.length == 1) {
                System.out.print(palavras_user[0]);
            } else {
                for(int i = 0; i < palavras_user.length; i++) {
                    System.out.print(palavras_user[i]);
                    if(i == palavras_user.length-1) {
                        if(palavras_user.length < pontuacao_user.length) {
                            System.out.print(pontuacao_user[i + 1]);
                        }
                    } else {
                        System.out.print(pontuacao_user[i+1]);
                    }
                }
            }
            */

            // processamento das palavras que nao pertencem ao dicionario e substituicao pela mais similar que pertenca:
            nao_membros = word_processor(nao_membros, dicionario, autocorrect);

            // impressao da frase do user final remontada:
            System.out.println("\nFinal phrase:"); System.out.print("\"");
            if(palavras_user.length == 1) {
                if(membros[0] == null) {
                    System.out.print(nao_membros[0]);
                } else {
                    System.out.print(membros[0]);
                }
            } else {
                for(int i = 0; i < palavras_user.length; i++) {
                    if(membros[i] == null) {
                        System.out.print(nao_membros[i]);
                    } else {
                        System.out.print(membros[i]);
                    }
                    if(i == palavras_user.length-1) {
                        if(palavras_user.length < pontuacao_user.length) {
                            System.out.print(pontuacao_user[i + 1]);
                        }
                    } else {
                        System.out.print(pontuacao_user[i+1]);
                    }
                }
            } System.out.print("\"\n");
        }

        System.out.println("\n\nEnding the program...");
        System.exit(0);
    }
}
