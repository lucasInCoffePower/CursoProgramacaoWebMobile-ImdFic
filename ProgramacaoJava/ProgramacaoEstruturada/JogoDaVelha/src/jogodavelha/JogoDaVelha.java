/*
 * Módulo para jogo da velha
 */

package jogodavelha;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class JogoDaVelha {
    /*
        Classe que abstrai jogo da velha
    */
    private static Scanner in = new Scanner(System.in); //  variável usada para ler entradas do teclado
    private static final String GAMER1 = "X"; // Simbolo do jogador 1
    private static final String GAMER2 = "O"; // Simbolo do jogador 2
    private static final String VAZIO = " "; // Simbolo para casa vazia no campo
    private static final int SIZE = 3; // tamanho do campo no jogo da velha
    private static int field[][] = new int[SIZE][SIZE]; // definicao da matriz do campo
   private static int win, column, line; // venceder, coluna para testar, linha para testar
   private static int gamer; // jogador
   private static Jogadores jogador[] = new Jogadores[50];
   private static int quantidade_jogadores = 0;
   private static int[] indices = new int[2];
   private static long time;
   private static File arquivo = new File("ranking.obj");
   
    public static void main(String[] args) {
        /*
            Procedimento principal
        */
        inicializarJogadores();
        char e = 'N';
        lerJogadores();
        do{
            cadastro();
            inicializarCampo();
            setTime();
            drawField(); // Desenhando o jogo
            for ( int i  =0; i< 9; i++){
                    jogar(i%2);
                    drawField(); // Desenhando o jogo
                   if(i>=4){
                       checarVencedor();
                       if(win != 0){
                           break;
                       }
                   }
            }
            if(win != 0){
                System.out.println("O vencedor "+jogador[indices[win-1]].nome);
                jogador[indices[win-1]].venceu += 1;
            }else{
                System.out.println("Ouve um empate" );
            }
            
            System.out.println("Tempo da partida: " + getTime()+"s");
            in.nextLine();
            salvarJogadores();
            imprimirJogadore();
            System.out.println("Deseja continuar a jogar ? (S/N)");
            e = in.nextLine().charAt(0);
        }while(e == 'S');
    }
    
    public static void salvarJogadores(){
        /*
            Procedimento para salvar jogadores
        */
        try{
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(jogador);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    
    public static void lerJogadores() {
        /*
            Procedimento para ler o registro Jogadores
        */
        int q_jogadores = quantidade_jogadores;
          try { 
              ObjectInputStream saida = new ObjectInputStream(new FileInputStream(arquivo));
               jogador = (Jogadores[])saida.readObject();
          } catch (FileNotFoundException e) {
            // Não faz nada 
          } catch (Exception e) { 
	throw new RuntimeException(e); 
         } 
    }
    
    
    public static void imprimirJogadore(){
        /*
            Método responsavel por imprimir jogadores
        */
        for (int i =0; i <quantidade_jogadores; i++){
            System.out.println("Nome: "+jogador[i].nome);
            System.out.println("Partidas: "+jogador[i].partidas);
            System.out.println("Venceu: "+jogador[i].venceu);
        }
    }

    public static void inicializarJogadores(){
        /*
            Inicializar vetor jogador
        */
        for(int i =0; i < 50; i++){
            jogador[i] = new Jogadores();
        }
    }
   
    public static void setTime(){
        /*
            Iniciando a contagem
        */
        time = System.currentTimeMillis(); // Retorna o agregado de segundos do dia 1/1/1970 até o dia atual
    }
    
    public static long getTime(){
        /*
            Finalizando e retornando a contagem
        */
        long timeCurrent = (System.currentTimeMillis() - time)/1000;
        return  timeCurrent;
    }
    
    public static void cadastro(){
        /*
            procedimento que cadastra jogadores
        */
        String jogador1 = null;
        int indice = 0;
        
        for(int i =0; i <2; i++){
            System.out.println("Insira o nome do jogador"+(i+1));
            jogador1  = in.nextLine();
            indice = buscarJogador(jogador1);
            if(indice == quantidade_jogadores){
                jogador[indice].nome = jogador1;
                indices[i] = indice;
                quantidade_jogadores+=1;
            }else{
                indices[i] = indice;
            }
            jogador[indice].partidas+=1;
        }
        
    }
    
    public static int buscarJogador(String nome){
        /*
            Procedimento responsável por buscar jogador 
        */
        for (int i = 0; i < 50; i++){
            if( nome.equalsIgnoreCase(jogador[i].nome)){
                return  i;
            }
        }
        return quantidade_jogadores;   
    }
    
    public static void inicializarCampo(){
        /*
            Inicializando campo de jogo para uma nova partida
        */
        System.out.println("Nova rodada iniciando");
        for (int i =0; i <SIZE; i++){
            for(int j=0; j<SIZE; j++){
                field[i][j] = 0;
            }
        }
        win = 0;
        line = 0;
        column = 0;
    }
    
    public static void drawField(){
        /*
            Método para desenhar campo do jogo
        */
        System.out.println("   1   2   3");
        for(int i = 0;  i < SIZE;  i++){
                System.out.print((i+1+"  "));
                for(int j =0;  j < SIZE;  j++){
                    drawChoices(i, j);
                    if (j!=2)
                    System.out.print(" | ");
                }
                System.out.println();
                if (i != 2){
                    System.out.println("   ---------");
                }
        }
    }
    
    
    public static void drawChoices(int line1, int column1){
        /*
            Método para desenhar as escolhas dos jogadores
        */
        if(field[line1][column1] == 1){
            System.out.print(GAMER1);
        }else if (field[line1][column1] == 2){
            System.out.print(GAMER2);
        }else if(field[line1][column1] == 0) {
            System.out.print(VAZIO);
        }
    }
    
    
    public static void jogar(int player){
        /*
            Função que define quem irá jogar e as jogadas
        */
        // Definindo de quem é a vez na partida
        
        column= 0 ; 
        line = 0;
        
        if (player == 0){
            gamer = 1;
        }else{
            gamer = 2;
        }
        
        System.out.println("Vez do " + jogador[indices[gamer-1]].nome);
        do{
            // Loop para definir a escolha de linha a ser jogada
            do{
                System.out.println("Qual coordenada da linha que você quer jogar: 1,2,3 ? ");
                line = in.nextInt();
                if( line<1 || line >3){
                    System.out.println("Você inseriu um valor fora do intervalo permitido. Digite novamente");
                }else{
                    line -=1;
                    break;
                }
            }while(true);

            // Loop para definir a escolha de coluna a se jogada
            do{
                System.out.println("Qual a coordenada da coluna que você quer jogar: 1,2,3 ?");
                column = in.nextInt();
                if (column<1 || line>3){
                    System.out.println("Você inseriu um valor fora do intervalo permitido. Digite novamente");
                }else{
                    column -=1;
                    break;
                }
            }while(true);
            
             // Validando posição
            if(field[line][column] != 1 && field[line][column] != 2){
                field[line][column] = gamer;
                break;
            }else{
                System.out.println("Está posição já está ocupada. Insira as coordenadas da jogada novamente");
            }
        }while(true);
    }
    
    
    public static void checarVencedor(){
        /*
            Procedimento que verifica se ouve vencedor
        */
        
        // checando se há vencedor na horizontal
          for (int  i = 0; i < SIZE; i++){
            if (field[i][0] == field[i][1] && field[i][2] == field[i][1]){
                if (field[i][0] == 1){
                    win = 1;
                }else if(field[i][0] == 2){
                    win = 2;
                }
             }
          }
          // checando se há vencedor na vertical
          for (int  j= 0; j < SIZE; j++){
            if (field[0][j] == field[1][j] && field[1][j] == field[2][j]){
                if (field[0][j] == 1){
                    win = 1;
                }else if(field[0][j] == 2){
                    win = 2;
                }
            }
          }
          // Checando na diagonal principal e na diagonal secundária
          if ((field[0][0]==field[1][1]&& field[1][1]==field[2][2])||(field[0][2]==field[1][1]&& field[1][1]==field[2][0])){
                if (field[1][1] == 1){
                    win = 1;
                }else if(field[1][1] == 2){
                    win = 2;
                }
          }
    }
}
