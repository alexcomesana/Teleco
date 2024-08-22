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

public class tcp1ser {
    public static void main(String[] args) throws IOException {
        if (args.length!=1){
        System.out.println("Sintaxis de inicio incorrecta");
        System.out.println("La sintaxis correcta es: java tcp1ser puerto");
        System.exit(-1);
        }
        
        int acul = 0;
        int tipo = 0;
	int resultado = 0;
        String clientSentence= " ";
        String serverSentence = " ";
        String puertoservidor = args[0];
        int puerto = Integer.parseInt(puertoservidor);
        try{
        //  El servidor abre el puerto para establecer una conexión.
        ServerSocket welcomeSocket = new ServerSocket(puerto); 
       
            //  Use esta conexión para obtener y enviar flujos de datos de clientes
            

while (true) {
             Socket connectionSocket = welcomeSocket.accept();
            //  Obtenga el flujo de datos del cliente
             InputStream in = connectionSocket.getInputStream();
             DataInputStream nuevo = new DataInputStream(in);

            //  Prepárese para enviar el flujo de datos modificado
            DataOutputStream outToClient = new DataOutputStream(
                    connectionSocket.getOutputStream());
	
            while(true){
            //  Leer los datos recibidos
            byte[] byterecibido = new byte[4]; //tamaño del bufer 
            
            nuevo.read(byterecibido);
            
            tipo = byterecibido[0];
            if(tipo==0){
            break;
            }
            if(tipo ==1){
            int valor1=byterecibido[2];
            int valor2=byterecibido[3];
            resultado = valor1 + valor2;
            acul = acul +resultado;
            
            System.out.println("La operación a realizar es:" + valor1 +"+" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            
            }
            if(tipo ==2){
            int valor1=byterecibido[2];
            int valor2=byterecibido[3];
            resultado = valor1 - valor2;
            acul = acul +resultado;
            
            System.out.println("La operación a realizar es:" + valor1 +"-" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            }
             if(tipo ==3){
            int valor1=byterecibido[2];
            int valor2=byterecibido[3];
            resultado = valor1 * valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"x" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            }
             if(tipo ==4){
            int valor1=byterecibido[2];
            int valor2=byterecibido[3];
            resultado = valor1 / valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"/" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            }
             if(tipo ==5){
            int valor1=byterecibido[2];
            int valor2=byterecibido[3];
            resultado = valor1 % valor2;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"%" +valor2);
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            }
             if(tipo ==6){
            int valor1=byterecibido[2];
            int factorial = 1;
            for(int i =valor1; i>0; i--){
            factorial =factorial*i;
            }
            resultado = factorial;
            acul = acul +resultado;
            System.out.println("La operación a realizar es:" + valor1 +"!");
            System.out.println("El resultado es:" + resultado);
            byte[] operacion = new byte[10];
            operacion[0]=(byte)16;
            operacion[1]=(byte)8;
            byte[] numero = new byte[8];
            
            numero=ByteBuffer.allocate(8).putInt(acul).array();
            operacion[9]=numero[3];
            operacion[8]=numero[2];
            operacion[7]=numero[1];
            operacion[6]=numero[0];
            operacion[5]=numero[7];
            operacion[4]=numero[6];
            operacion[3]=numero[5];
            operacion[2]=numero[4];
            outToClient.write(operacion);
            }
           
	}    
        
    }
    }catch(Exception e){
    System.out.println("ERROR");
    
    }
}
}
