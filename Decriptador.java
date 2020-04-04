import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Decriptador {

    public String textoCifrado;
    public double coincidencia = 0.072723; // indice de coincidencia para o portugues
    public ArrayList<String> letras = new ArrayList<String>();
    public ArrayList<Double> frequencias = new ArrayList<Double>();

    public Decriptador(String caminho) throws FileNotFoundException, InterruptedException {

        lerArquivos(caminho);

        int tamanhoChave = encontrarTamanhoChave();
        //int[] deslocamentos = encontrarDeslocamentos(tamanhoChave);

        //mostrarTextoDecifrado(deslocamentos);
    }

    public void mostrarTextoDecifrado(int[] deslocamentos){
        
        System.out.println();
    }

    public int[] encontrarDeslocamentos(int tamanhoChave){

        return new int[5];
    }

    public int encontrarTamanhoChave() {

        char[] array = textoCifrado.toCharArray();

        for(int i = 1; i <= 10; i++){ // pulando de i em i elementos ate pular de 10 em 10
            for(int w = 0; w < i; w++){ // controlando a quantia de iteracoes em relacao a i

                int[] ocorrencias = new int[letras.size()];
                for(int j = w; j < array.length; j += i) { // passando por todos os elementos
                    for(int k = 0; k < letras.size(); k++){ // comparando o elemento atual com as letras do alfabeto
                        char aux = letras.get(k).charAt(0);
                        //System.out.println(array[j]+" : "+aux+" ==? "+(array[j] == aux));
                        if(aux == array[j]){
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
                    //System.out.println(i); // tamanho da chave encontrado
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

        /*
        String[] entrada = in.nextLine().split(":");
        System.out.println("Texto Claro: "+entrada[0].toUpperCase());
        System.out.println("Chave a ser encontrada: "+entrada[1].toUpperCase());
        System.out.println("Texto cifrado: "+entrada[2].toUpperCase());
        textoCifrado = entrada[2].toUpperCase();
        */
        textoCifrado = in.nextLine().toUpperCase();

        File arquivoFrequencias = new File("frequencias.txt");
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