import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// Classe que executa todo o processo de quebra da cifra
public class Decriptador {

    public char[] textoCifrado; // texto no formato cifrado
    public double coincidencia = 0.072723; // indice de coincidencia para o portugues
    public char[] letrasAlfabetica = new char[26]; // alfabeto em ordem alfabetica

    public Decriptador(String caminho) throws FileNotFoundException {

        lerArquivos(caminho); // ler o arquivo de entrada

        int tamanhoChave = encontrarTamanhoChave(); // encontrar o tamanho da chave

        int[] deslocamentos = encontrarDeslocamentos(tamanhoChave); // encontrar deslocamentos/senhas

        mostrarTextoDecifrado(deslocamentos); // usar os deslocamentos/senhas para quebrar a cifra
    }

    // Usa os deslocamentos para decifrar o texto de entrada e mostrar o texto claro
    public void mostrarTextoDecifrado(int[] deslocamentos){
        
        int min = 65; // A em ASCII
        int max = 90; // Z em ASCII
        int indice = 0;

        for(int g = 0; g < textoCifrado.length; g++){
            int cifrado = (int)textoCifrado[g];
            int deslocamento = deslocamentos[indice];
            if((cifrado - deslocamento) < min){
                int aux = min - (cifrado - deslocamento);
                cifrado = (max - aux) + 1;
                //System.out.println(textoCifrado[g]);
                //System.out.println(deslocamento);
                //System.out.println((char)cifrado);
            }
            else{
                cifrado = cifrado - deslocamento; 
            }

            indice += 1;
            if(indice == deslocamentos.length){
                indice = 0;
            }
            System.out.print((char)cifrado);
        }     
    }

    // Usa o tamanho da chave para descobrir as letras
    public int[] encontrarDeslocamentos(int tamanhoChave){

        int[] resposta = new int[tamanhoChave];
        Scanner input = new Scanner(System.in);
        for(int w = 0; w < tamanhoChave; w++){ 
            int[] ocorrencias = new int[letrasAlfabetica.length];
            for(int j = w; j < textoCifrado.length; j += tamanhoChave) { // passando por todos os elementos
                for(int k = 0; k < letrasAlfabetica.length; k++){ // comparando o elemento atual com as letras do alfabeto
                    char aux = letrasAlfabetica[k];
                    //System.out.println(array[j]+" : "+aux+" ==? "+(array[j] == aux));
                    if(aux == textoCifrado[j]){
                        ocorrencias[k] += 1;
                        break;
                    }
                }
            }
            
            int maior = 0;
            int indiceMaior = 0;
            int indiceSegundoMaior = 0;
            for(int p = 0; p < ocorrencias.length; p++){
                if(ocorrencias[p] > maior){
                    maior = ocorrencias[p]; 
                    indiceSegundoMaior = indiceMaior;
                    indiceMaior = p;
                }
            }

            int senhaA = (int)letrasAlfabetica[indiceMaior];
            //System.out.println("Senha A: "+ senhaA+" = "+(char)senhaA);
            int senhaE = (int)letrasAlfabetica[indiceSegundoMaior];
            //System.out.println("Senha E: "+ senhaE+" = "+(char)senhaE);
            int A = 65;
            int E = 69;
            int deslocamentoA = senhaA - A;
            int deslocamentoE = senhaE - E;
            if(senhaE < E){
                deslocamentoE = (senhaE - 65) + (90 - E);
            }

            System.out.println(" Escolha a senha e deslocamento para a coluna "+ (w+1) +":");
            System.out.println("\n 1. Senha: "+senhaA+"->"+(char)senhaA+" , Deslocamento: "+deslocamentoA);
            System.out.println("\n 2. Senha: "+senhaE+"->"+(char)senhaE+" , Deslocamento: "+deslocamentoE);
            int escolha = input.nextInt();
            if(escolha == 1){
                resposta[w] = deslocamentoA;
            }
            else{
                resposta[w] = deslocamentoE;
            }
            
            //System.out.println("Deslocamento "+ (w+1) +": "+resposta[w]);
        }
        input.close();
        return resposta;
    }

    // Encontra o tamanho da chave usada para cifrar o texto
    public int encontrarTamanhoChave() {

        for(int i = 1; i <= 10; i++){ // pulando de i em i elementos ate pular de 10 em 10
            for(int w = 0; w < i; w++){ // controlando a quantia de iteracoes em relacao a i

                int[] ocorrencias = new int[letrasAlfabetica.length];
                for(int j = w; j < textoCifrado.length; j += i) { // passando por todos os elementos
                    for(int k = 0; k < letrasAlfabetica.length; k++){ // comparando o elemento atual com as letras do alfabeto
                        char aux = letrasAlfabetica[k];
                        //System.out.println(array[j]+" : "+aux+" ==? "+(array[j] == aux));
                        if(aux == textoCifrado[j]){
                            ocorrencias[k] += 1;
                            break;
                        }
                    }
                }
                double soma = 0;
                double total = 0;
                for(int p = 0; p < ocorrencias.length; p++){
                    //System.out.println(letras.get(p)+": "+ocorrencias[p]);
                    soma += ocorrencias[p] * (ocorrencias[p]-1);
                    total += ocorrencias[p];
                }
                double indice = soma / (total * (total-1));
                //System.out.println(i +" : "+ soma+" : "+total+": "+indice);
                double diff = Math.abs(indice - coincidencia);
                //System.out.println(diff + " : " + (Double.compare(diff, 0.00999) < 0));
                //Thread.sleep(1000);
                if(Double.compare(diff, 0.00999) < 0){
                    //System.out.println("Tamanho da chave encontrado: "+i); // tamanho da chave encontrado
                    return i;
                }
            }
        }
        return 1;
    }

    // Le os arquivos de entrada e popula as variaveis e objetos
    public void lerArquivos(String caminho) throws FileNotFoundException {

        File arquivoEntrada = new File(caminho);
        Scanner in = new Scanner(arquivoEntrada);

        String texto = in.nextLine().toUpperCase();
        textoCifrado = texto.toCharArray();
        //System.out.println("Texto cifrado: "+texto);

        int indice = 0;
        for(int i = 65; i <= 90; i++){
            letrasAlfabetica[indice] = (char)i;
            //System.out.println(letrasAlfabetica[indice]);
            indice+=1;
        }
        in.close();
    }
}