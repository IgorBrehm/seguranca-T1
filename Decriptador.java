import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Decriptador {

    public char[] textoCifrado;
    public double coincidencia = 0.072723; // indice de coincidencia para o portugues
    public ArrayList<String> letras = new ArrayList<String>();
    public ArrayList<Double> frequencias = new ArrayList<Double>();

    public Decriptador(String caminho) throws FileNotFoundException, InterruptedException {

        lerArquivos(caminho);

        int tamanhoChave = encontrarTamanhoChave();

        int[] deslocamentos = encontrarDeslocamentos(tamanhoChave);

        mostrarTextoDecifrado(deslocamentos);
    }

    // Usa as letras da chave para decifrar o texto de entrada e mostrar o texto claro
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
        for(int w = 0; w < tamanhoChave; w++){ // controlando a quantia de iteracoes em relacao a i

            int[] ocorrencias = new int[letras.size()];
            for(int j = w; j < textoCifrado.length; j += tamanhoChave) { // passando por todos os elementos
                for(int k = 0; k < letras.size(); k++){ // comparando o elemento atual com as letras do alfabeto
                    char aux = letras.get(k).charAt(0);
                    //System.out.println(array[j]+" : "+aux+" ==? "+(array[j] == aux));
                    if(aux == textoCifrado[j]){
                        ocorrencias[k] += 1;
                        break;
                    }
                }
            }
            
            // sei as ocorrencias de cada letra
            // a letra que mais apareceu vai ser a letra usada para cifrar o A
            int maior = 0;
            int indice = 0;
            for(int p = 0; p < ocorrencias.length; p++){
                if(ocorrencias[p] > maior){
                    maior = ocorrencias[p]; 
                    indice = p;
                }
            }
            // sei o indice da lista de letras onde tiveram mais ocorrencias
            // verifico o deslocamento entre a letra e o A
            int senha = (int)letras.get(indice).charAt(0);
            int A = (int)letras.get(0).charAt(0);
            resposta[w] = senha - A;
            System.out.println("Senha: "+ senha+" = "+(char)senha);
            System.out.println("Deslocamento "+ (w+1) +": "+resposta[w]);
            //Thread.sleep(1000);
        }
        return resposta;
    }

    // Encontra o tamanho da chave usada para cifrar o texto
    public int encontrarTamanhoChave() {

        for(int i = 1; i <= 10; i++){ // pulando de i em i elementos ate pular de 10 em 10
            for(int w = 0; w < i; w++){ // controlando a quantia de iteracoes em relacao a i

                int[] ocorrencias = new int[letras.size()];
                for(int j = w; j < textoCifrado.length; j += i) { // passando por todos os elementos
                    for(int k = 0; k < letras.size(); k++){ // comparando o elemento atual com as letras do alfabeto
                        char aux = letras.get(k).charAt(0);
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
                    System.out.println("Tamanho da chave encontrado: "+i); // tamanho da chave encontrado
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
        System.out.println("Texto cifrado: "+texto);

        File arquivoFrequencias = new File("frequencias.txt"); // arquivo com as letras e suas frequencias no portugues
        in.close();
        in = new Scanner(arquivoFrequencias);

        while(in.hasNextLine()){
            String[] aux = in.nextLine().split(":");
            //System.out.println(aux[0]);
            //System.out.println(aux[1]);
            letras.add(aux[0]);
            frequencias.add(Double.parseDouble(aux[1]));
        }
        in.close();
    }
}