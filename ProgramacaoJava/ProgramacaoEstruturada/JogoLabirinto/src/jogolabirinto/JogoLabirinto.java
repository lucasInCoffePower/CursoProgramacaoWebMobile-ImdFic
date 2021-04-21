
package jogolabirinto;
/**
 *
 * @author Desconhecido
 */

public class JogoLabirinto {
    /*
        Criando classe que abstrai características e comportamento de um jogo de labirinto
    */
    
    private static final char PAREDE_VERTICAL = '|' ;
    private static final char PAREDE_HORIZONTAL = '-' ;
    private static final char VAZIO =' ' ; 
    private static final char TAMANHO = 10; // Tamanho do tabuleiro
    private static final char BARREIRA = '@'; // Obstaculo a ser colocado no tabuleiro
    private static final char INICIO = 'I'; // Inicio do jogo
    private static final char DESTINO = 'D'; // Destino do jogo
    private static final char CAMINHO_CERTO = '.';
    private static final char CAMINHO_ERRADO = 'x';
    private static int linha_inicial = 0;
    private static int coluna_inicial = 0;
    private static int linha_final = 0;
    private static int coluna_final = 0;
    private static final int limite_inicial_linha_inicio = 1;
    private static final int limite_final_linha_inicio = 2;
    private static final int limite_inicial_coluna_inicio = 1;
    private static final int limite_final_coluna_inicio = 8;
    private static final int limite_inicial_linha_destino = 7;
    private static final int limite_final_linha_final_destino = 8;
    private static final int limite_inicial_coluna_destino = 1;
    private static final int limite_final_coluna_destino = 8;
    private static final double PROBABILIDADE = 7.0; // Constante com probabilidade de uma barreira existir
    private static char[][] tabuleiro; // 
    
    
    public static boolean procurarCaminho(int linha_teste0, int coluna_teste0){
        /*
            Função retorna o resultado do jogo
        */
        boolean status = false;
        
         // verificando se da pra descer
        int linha_teste = linha_teste0-1;
        int coluna_teste = coluna_teste0;
        status = tentarCaminho(linha_teste, coluna_teste);
        
         // verificando se da pra ir para cima
        if(!status){
            
            linha_teste = linha_teste0 +1 ;
            coluna_teste = coluna_teste0;
            status = tentarCaminho(linha_teste,coluna_teste);
        }
        
        // verificando se da pra ir para direita
        if(!status){
            linha_teste = linha_teste0;
            coluna_teste = coluna_teste0 +1;
            status = tentarCaminho(linha_teste,coluna_teste);
        }
        
        // verificando se da pra ir para esquerda
        if(!status){
            linha_teste = linha_teste0;
            coluna_teste = coluna_teste0 -1;
            status = tentarCaminho(linha_teste,coluna_teste);
        }     
        return status;
    }
    
    
    public static boolean tentarCaminho(int linha_teste, int coluna_teste){
        /*
            Função que verifica se o caminho é o DESTINO, se está VAZIO ou se está com BARREIRA ou PAREDE
            returna um tipo boolean
        */
        boolean status = false;
        char teste = tabuleiro[linha_teste][coluna_teste] ;
        if(teste == DESTINO){
            status = true;
        }else if(temVazio(linha_teste, coluna_teste)){
            // Caminho certo
            tabuleiro[linha_teste][coluna_teste] = CAMINHO_CERTO;
            imprimir();
            status = procurarCaminho(linha_teste, coluna_teste);
        }else if( teste != INICIO && teste != PAREDE_VERTICAL && teste != PAREDE_HORIZONTAL){
            if (teste != CAMINHO_CERTO){
                tabuleiro[linha_teste][coluna_teste] = CAMINHO_ERRADO;
            }
            imprimir();
        }
        return status;
    }
    
    
    public static boolean temVazio(int linha_teste, int coluna_teste){
        /*
            Função que verifica se tem um espaço vazio no tabuleiro
        */
        boolean status = false;
        char teste = tabuleiro[linha_teste][coluna_teste];
        if(teste == VAZIO){
            status = true;
        }
        return status;
    }

    public static int gerarNumero(int limite_inferior, int limite_superior){
        /*
            Função que retorna um valor aleatorio entre os limites delimitados
        */
        int probabilidade = (int) Math.round(Math.random()*(limite_superior - limite_inferior));
        return limite_inferior+probabilidade;
    }
    
    
    public static void inicializarMatriz(){
        /*
            Procedimento que inicializa a matriz tabuleiro 
      */
     for(int i =0; i < TAMANHO;  i++){
           tabuleiro[i][0] = PAREDE_VERTICAL;
            tabuleiro[i][TAMANHO - 1] = PAREDE_VERTICAL;
            tabuleiro[0][i] = PAREDE_HORIZONTAL;
            tabuleiro[TAMANHO-1][i] = PAREDE_HORIZONTAL;
        }
        for (int i =1; i < TAMANHO-1; i ++){
            for ( int j =1; j < TAMANHO-1; j ++){
                if (PROBABILIDADE <= Math.random()*10){
                    tabuleiro[i][j] = BARREIRA;
                }else{
                    tabuleiro[i][j] = VAZIO;
                }
                
            }
        } 
        linha_inicial = gerarNumero(limite_inicial_linha_inicio, limite_final_linha_inicio);
        coluna_inicial = gerarNumero(limite_inicial_coluna_inicio, limite_final_coluna_inicio);
        tabuleiro[linha_inicial][coluna_inicial] = INICIO;
        linha_final = gerarNumero(limite_inicial_linha_destino, limite_final_linha_final_destino);
        coluna_final = gerarNumero(limite_inicial_coluna_destino, limite_final_coluna_destino);
        tabuleiro[linha_final][coluna_final] = DESTINO;
    }

    
    public static void imprimir(){
        /*
            Procedimento que imprime a matriz labirinto
        */
        for (int i = 0; i < TAMANHO; i ++){
            for (int j = 0; j < TAMANHO; j++){
                System.out.print(tabuleiro[i][j]);
            }
            System.out.println();
        }
        try{
                Thread.sleep(300);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
    }

    
    public static void main(String[] args) {
        /*
            Procedimento principal
        */
        tabuleiro = new char[TAMANHO][TAMANHO];
        inicializarMatriz();
        imprimir();
        if( procurarCaminho(linha_inicial, coluna_inicial) == true){
            System.out.println("Caminho encontrado");
        }else{
            System.out.println("Caminho não encontrado");
        }
        
    }
}
