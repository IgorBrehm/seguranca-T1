import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Decriptador {

    public ArrayList<String> textoCifrado = new ArrayList<String>();
    public double coincidencia = 0.072723;

    public Decriptador(String caminho) throws FileNotFoundException {

        File arquivo = new File(caminho);
        Scanner in = new Scanner(arquivo);
        while(in.hasNextLine()){
            textoCifrado.add(in.nextLine());
        }






        in.close();
    }
}