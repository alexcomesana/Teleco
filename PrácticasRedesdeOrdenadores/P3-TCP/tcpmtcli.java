import java.io.*;
import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.nio.*;

public class tcpmtcli {
    
    
    public static void main(String[] args)throws UnknownHostException, IOException, InterruptedException{
    if (args.length!=2){
        System.out.println("Sintaxis de inicio incorrecta");
        System.out.println("La sintaxis correcta es: java  ID puerto");
        System.exit(-1);
        }
        System.out.println("El formato es el siguiente: valor1 operacion valor2 o valor1 ! (un solo espacio entre simbolos y sin espacio al inicio y final.");
	System.out.println("Ejemplos:1 + 2");
	System.out.println("Ejemplos:20 - 2");
	System.out.println("Ejemplos:134 / 4 (division)");
	System.out.println("Ejemplos:1 % 2(resto de la division)");
	System.out.println("Ejemplos:4 x 5(multiplicacion)");
	System.out.println("Ejemplos:5 !");
	String fin= " ";
	String simbolo = " ";
	int resultado = 0;
	long res = 0;
        // hacemos la conexion
        String id = args[0];
        String puertoservidor= args[1];
        int puerto = Integer.parseInt(puertoservidor);
        try (Socket socket = new Socket()) {
         
           SocketAddress dir = new InetSocketAddress (id,puerto);
           socket.connect( dir, 15000);
            // para escibir  al servidor
             DataOutputStream out= new DataOutputStream(socket.getOutputStream());
            // para recibir del servidor
            InputStream in =socket.getInputStream();
            DataInputStream nuevo = new DataInputStream(in);
  
            
            Scanner sc = new Scanner(System.in);
            String line = null;
  
            while (true) {
                 System.out.println("Escriba un numero (o numeros)para sumar:");
        System.out.println("Inserte QUIT si quiere acabar el programa");
        
                //recibimos los datos
                line = sc.nextLine();
                StringTokenizer op = new StringTokenizer(line);
                fin =op.nextToken();
                int tam = line.length();
  		if(fin.equals("QUIT")){
  		System.out.println("Has introducido QUIT, fin del programa");
  		socket.close();
  		break;
  		}
  		simbolo = op.nextToken();
  		if(simbolo.equals("!")){

  		byte[] operacion = new byte[3];
                operacion[0]= (byte)6;
                operacion[1]= (byte)1;
                String numero = fin;
                int numero1=Integer.parseInt(numero);
               
                operacion[2]= (byte) numero1;
      	        out.write(operacion);
                out.flush();
                     
  		}
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
                out.write(operacion);
                out.flush();
                
                
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
                out.write(operacion);
                out.flush();
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
                out.write(operacion);
                out.flush();
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
                out.write(operacion);
                out.flush();
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
                out.write(operacion);
                out.flush();
  		}
 	        //leemos los datos del servidor y los mostramos
 	       
 	        byte[] byterecibido = new byte[12]; //tamaño del bufer 
 	        
                nuevo.readFully(byterecibido);
               
                int tipo1=(byterecibido[0] & 0xFF);
                int longitud1 =(byterecibido[1] & 0xFF);
                int tipo = (byterecibido[2] & 0xFF);
                int longitud=(byterecibido[3] & 0xFF);
                if(tipo1==10){
                   if(tipo==16){               
                  byte[] byte2 = new byte[8];
                  byte2[0]= byterecibido[4];
                  byte2[1]= byterecibido[5];
                  byte2[2]= byterecibido[6];
                  byte2[3]= byterecibido[7];
                  byte2[4]= byterecibido[8];
                  byte2[5]= byterecibido[9];
                  byte2[6]= byterecibido[10];
                  byte2[7]= byterecibido[11];
                  res = ByteBuffer.wrap(byte2).getLong();
                  System.out.println("El valor del acumulador es: " +res);
                
                }
                   if(longitud1 == 24){
                  System.out.println("El valor del acumulador es: " +res+ " Error: Wrong domain");  
                  byte[] nuevob=new byte[14];
                  nuevo.readFully(nuevob); 
                  }
                  if(longitud1 == 34){
                  byte[] nuevob=new byte[24];
                  nuevo.readFully(nuevob); 
                  System.out.println("El valor del acumulador es: " +res+ " Error: Result is out of range");   
                  }
                } 
        }
        }
        catch (Exception e) {
             System.out.println("Fallo en la inicializacion del programa");
        }
    }
    }
    

