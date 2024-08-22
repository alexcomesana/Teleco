import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.text.*;
import java.util.Date;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.*;  

// Server class
 public class tcpmtser {
    public static void main(String[] args)
    {
    if (args.length!=1){
        System.out.println("Sintaxis de inicio incorrecta");
        System.out.println("La sintaxis correcta es: java tcp1ser puerto");
        System.exit(-1);
        }
        ServerSocket server = null;
        String puertoservidor = args[0];
        int puerto = Integer.parseInt(puertoservidor);
        try {
  
            // servidor oyendo al puerto
            server = new ServerSocket(puerto);
            server.setReuseAddress(true);
  
            // bucle infinito
            while (true) {
  
                // socket para recibir respuestas
                Socket client = server.accept();
  
                // Displaying that new client is connected
                // to server
                System.out.println("Nuevo cliente conectado");
  
                // create a new thread object
                ClientHandler clientSock
                    = new ClientHandler(client);
  
                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  
    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
  
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
  
        public void run()
        { 
            long acul = 0;
            long resultado= 0;
            long tipo = 0;
            DataOutputStream outToClient = null;
            
            try {  
            // para enviar datos al cliente
              outToClient = new DataOutputStream(clientSocket.getOutputStream());
  
                  // lee los datos del cliente
             InputStream in = clientSocket.getInputStream();
            DataInputStream nuevo = new DataInputStream(in);                 
            
               while (true) {
                   
                    // procesamos el mensaje recibido del cliente
                    byte[] byterecibido = new byte[4]; //tamaño del bufer 
                    nuevo.read(byterecibido);
                    tipo = byterecibido[0];
            if(tipo==0){
            break;
            }
            if(tipo ==1){
            long valor1=byterecibido[2];
            long valor2=byterecibido[3];
            resultado = valor1 + valor2;
            acul = acul +resultado;
            
            System.out.println("La operación a realizar es:" + valor1 +"+" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            
            }
            if(tipo ==2){
            long valor1=byterecibido[2];
            long valor2=byterecibido[3];
            resultado = valor1 - valor2;
            acul = acul +resultado;
            
            System.out.println("La operación a realizar es:" + valor1 +"-" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            }
             if(tipo ==3){
            long valor1=byterecibido[2];
            long valor2=byterecibido[3];
            resultado = valor1 * valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"x" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            }
             if(tipo ==4){
            long valor1=byterecibido[2];
            long valor2=byterecibido[3];
            
            if(valor2==0){
            System.out.println("Could not calculate answer. Wrong domain(división por 0)");
            byte[] operacion = new byte[26];
            operacion[0]=(byte)10;
            operacion[1]=(byte)24;
            operacion[2]=(byte)11;
            operacion[3]=(byte)12;
            
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[25]=numero[7];
            operacion[24]=numero[6];
            operacion[23]=numero[5];
            operacion[22]=numero[4];
            operacion[21]=numero[3];
            operacion[20]=numero[2];
            operacion[19]=numero[1];
            operacion[18]=numero[0];
            operacion[17]=(byte)8;
            operacion[16]=(byte)16;
            byte[] bytes = new byte[12];
            String st ="Wrong domain";
            bytes= st.getBytes();
            for(int i=0; i<12;i++){
            operacion[i+4]=bytes[i];
            }
            outToClient.write(operacion);
            }
            else{
            resultado = valor1 / valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"/" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            }
            }
             if(tipo ==5){
            long valor1=byterecibido[2];
            long valor2=byterecibido[3];
            
            if(valor2==0){
            System.out.println("Could not calculate answer. Wrong domain(resto por 0)");
            byte[] operacion = new byte[26];
            operacion[0]=(byte)10;
            operacion[1]=(byte)24;
            operacion[2]=(byte)11;
            operacion[3]=(byte)12;
            
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[25]=numero[7];
            operacion[24]=numero[6];
            operacion[23]=numero[5];
            operacion[22]=numero[4];
            operacion[21]=numero[3];
            operacion[20]=numero[2];
            operacion[19]=numero[1];
            operacion[18]=numero[0];
            operacion[17]=(byte)8;
            operacion[16]=(byte)16;
            byte[] bytes = new byte[12];
            String st ="Wrong domain";
            bytes= st.getBytes();
            for(int i=0; i<12;i++){
            operacion[i+4]=bytes[i];
            }
            outToClient.write(operacion);
            }
            else{
            resultado = valor1 % valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"%" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            }
            }
             if(tipo ==6){
            long valor1=byterecibido[2];
            
            if(valor1<0){
            System.out.println("Could not calculate answer. Wrong domain(factorial negativo)");
            byte[] operacion = new byte[26];
            operacion[0]=(byte)10;
            operacion[1]=(byte)24;
            operacion[2]=(byte)11;
            operacion[3]=(byte)12;
            
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[25]=numero[7];
            operacion[24]=numero[6];
            operacion[23]=numero[5];
            operacion[22]=numero[4];
            operacion[21]=numero[3];
            operacion[20]=numero[2];
            operacion[19]=numero[1];
            operacion[18]=numero[0];
            operacion[17]=(byte)8;
            operacion[16]=(byte)16;
            byte[] bytes = new byte[12];
            String st ="Wrong domain";
            bytes= st.getBytes();
            for(int i=0; i<12;i++){
            operacion[i+4]=bytes[i];
            }
            
            outToClient.write(operacion);                      
            }
            if(valor1<21 && valor1>=0){
            long factorial = 1;
            for(int i = 1; i<=valor1;i++){
            factorial =factorial*i;
            }
            resultado = factorial;
            acul = acul +resultado;
            
            System.out.println("La operación a realizar es:" + valor1 +"!");
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[12];
            operacion[0]=(byte)10;
            operacion[1]=(byte)10;
            operacion[2]=(byte)16;
            operacion[3]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[11]=numero[7];
            operacion[10]=numero[6];
            operacion[9]=numero[5];
            operacion[8]=numero[4];
            operacion[7]=numero[3];
            operacion[6]=numero[2];
            operacion[5]=numero[1];
            operacion[4]=numero[0];
            
            outToClient.write(operacion);
            }
            if(valor1>20){
            System.out.println("Could not calculate answer.Result is out of range");
            byte[] operacion = new byte[36];
            operacion[0]=(byte)10;
            operacion[1]=(byte)34;
            operacion[2]=(byte)11;
            operacion[3]=(byte)22;
            
            byte[] numero = new byte[8];
            
                        
            numero=ByteBuffer.allocate(8).putLong(acul).array();
            operacion[35]=numero[7];
            operacion[34]=numero[6];
            operacion[33]=numero[5];
            operacion[32]=numero[4];
            operacion[31]=numero[3];
            operacion[30]=numero[2];
            operacion[29]=numero[1];
            operacion[28]=numero[0];
            operacion[27]=(byte)8;
            operacion[26]=(byte)16;
            byte[] bytes = new byte[22];
            String st ="Result is out of range";
            bytes= st.getBytes();
            for(int i=0; i<22;i++){
            operacion[i+4]=bytes[i];
            }
            
            outToClient.write(operacion);
            
            }
            }

            }
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (outToClient != null) {
                        outToClient.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
    }
    }

