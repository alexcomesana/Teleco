import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.text.*;
import java.util.Date;
import java.nio.*;
import java.util.concurrent.TimeUnit;


public class tcp1cli {

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
    
      if (args.length!=2){
        System.out.println("Sintaxis de inicio incorrecta");
        System.out.println("La sintaxis correcta es:java tcp1cli  ID puerto");
        System.exit(-1);
        }
	System.out.println("El formato es el siguiente: valor1 operacion valor2 o valor1 ! (un solo espacio entre simbolos y sin espacio al inicio y final.");
	System.out.println("Ejemplos:1 + 2");
	System.out.println("Ejemplos:20 - 2");
	System.out.println("Ejemplos:134 / 4 (division)");
	System.out.println("Ejemplos:1 % 2(resto de la division)");
	System.out.println("Ejemplos:4 x 5(multiplicacion)");
	System.out.println("Ejemplos:5 !");
	String fin = " ";
        try{
        //  Cree dos cadenas de tipo para recibir y enviar caracteres
        String sentence;
	String enviar;
	String mensaje;
	String simbolo = " ";
	byte[] simbolo1;
	String leer = " ";
	int resultado = 0;
        //  Crear una secuencia de entrada para recibir la entrada del teclado
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        //  Cree un Scoket clientScoket para iniciar la conexión entre el servidor y el cliente
        String id = args[0];
        String puertoservidor= args[1];
        int puerto = Integer.parseInt(puertoservidor);
        Socket clientSocket = new Socket(id, puerto);
        
        
	
 while(true){     
 	
        System.out.println("Inserte 'QUIT' si quiere acabar el cliente.");
        System.out.println("Introduzca una operación para realizar:");
	
        //  Leer los datos a enviar
        sentence = inFromUser.readLine();
 	StringTokenizer op = new StringTokenizer(sentence);
	fin = op.nextToken();
	if(fin.equals("QUIT")){
		System.out.println("Cliente: Fin del programa. Has introducido QUIT");
                System.exit(-1);
        }   
	
        int tam = sentence.length(); 
          
        //  Cree una secuencia de salida que envíe información al servidor
        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());

        //  Cree una secuencia de entrada para recibir la secuencia de bytes del servidor
        InputStream in = clientSocket.getInputStream();
        DataInputStream nuevo = new DataInputStream(in);
        //  Enviar datos al servidor
        if(tam <5){
        simbolo = op.nextToken();
        byte[] operacion = new byte[3];
        operacion[0]= (byte)6;
        operacion[1]= (byte)1;
        String numero = fin;
        int numero1=Integer.parseInt(numero);
        operacion[2]= (byte) numero1;
        
        outToServer.write(operacion);
        } 
        
        if(tam >=5){
        simbolo = op.nextToken();
        	if(simbolo.equals("+")){
        	byte[] operacion = new byte[4];
        	operacion[0]= (byte)1;
                operacion[1]= (byte)2;
                String valor1= fin;
                int valor11 = Integer.parseInt(valor1);
                operacion[2]=(byte) valor11;
                String valor2 =op.nextToken();
                int valor22 = Integer.parseInt(valor2);
                operacion[3]=(byte) valor22;
                outToServer.write(operacion);
        	}
        
        	if(simbolo.equals("-")){
        	byte[] operacion = new byte[4];
        	operacion[0]= (byte)2;
                operacion[1]= (byte)2;
                String valor1= fin;
                int valor11 = Integer.parseInt(valor1);
                operacion[2]=(byte) valor11;
                String valor2 =op.nextToken();
                int valor22 = Integer.parseInt(valor2);
                operacion[3]=(byte) valor22;
                outToServer.write(operacion);
        	}
        
        	if(simbolo.equals("x")){
        	byte[] operacion = new byte[4];
        	operacion[0]= (byte)3;
                operacion[1]= (byte)2;
                String valor1= fin;
                int valor11 = Integer.parseInt(valor1);
                operacion[2]=(byte) valor11;
                String valor2 =op.nextToken();
                int valor22 = Integer.parseInt(valor2);
                operacion[3]=(byte) valor22;
                outToServer.write(operacion);
        	}
        	if(simbolo.equals("/")){
        	byte[] operacion = new byte[4];
        	operacion[0]= (byte)4;
                operacion[1]= (byte)2;
                String valor1= fin;
                int valor11 = Integer.parseInt(valor1);
                operacion[2]=(byte) valor11;
                String valor2 =op.nextToken();
                int valor22 = Integer.parseInt(valor2);
                operacion[3]=(byte) valor22;
                outToServer.write(operacion);
        	}
        	if(simbolo.equals("%")){
        	byte[] operacion = new byte[4];
        	operacion[0]= (byte)5;
                operacion[1]= (byte)2;
                String valor1= fin;
                int valor11 = Integer.parseInt(valor1);
                operacion[2]=(byte) valor11;
                String valor2 =op.nextToken();
                int valor22 = Integer.parseInt(valor2);
                operacion[3]=(byte) valor22;
                outToServer.write(operacion);
        	}
        } 
	

        //  Obtenga los datos recibidos del servidor
      
        byte[] byterecibido = new byte[10]; //tamaño del bufer 
   
        nuevo.readFully(byterecibido);
        
        
        resultado = ((byterecibido[2] & 0xFF) <<56) | ((byterecibido[3] & 0xFF <<48) | ((byterecibido[4] & 0xFF) <<40) | ((byterecibido[5] & 0xFF) <<32) | ((byterecibido[6] & 0xFF) <<24 )| ((byterecibido[7] & 0xFF) <<16) | ((byterecibido[8] & 0xFF) <<8) | ((byterecibido[9] & 0xFF) <<0));    
        //int y = (int) resultado.charAt(9);
        //if(y<=127){
        //System.out.println("El valor es de "+y);
        //}
        //if(y>127){
        //int x =(int) resultado.charAt(8);
        //System.out.println("El valor es de "+x);
       // }
        //int ascii = (int) resultado.charAt;
        System.out.println("El resultado del acumulador es: "+resultado);
        
        
           
    }
    
    }

    catch (IOException e) {
			TimeUnit.SECONDS.sleep(15);
			System.out.println("Temporizador agotado, han pasado 15 segundos sin conexión");
		}
    }
    }


