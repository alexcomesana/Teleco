import java.io.IOException;
import java.util.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class udpser {

	public static void main(String[] args) {
		try {
			//Comprobacion de argumentos
			if(args.length != 2) {
				System.out.println("La sintaxis correcta es: java <udpser> <num_puerto> <secreto>");
				System.exit(0);
			}
			
			//Construimos el socket a partir del puerto
			int puerto = Integer.parseInt(args[0]);
			int secreto = Integer.parseInt(args[1]);
			DatagramSocket entrada= new DatagramSocket(puerto);
			
			// Construimos el datagrama para recibir mensajes
		        DatagramPacket recibir = null;
		        
		        
				
			//Bucle de espera
		    while (true) {
		        int resultado = 0;
		        byte[] buf = new byte[2048];
		        recibir = new DatagramPacket(buf, buf.length); //recibimos los datos
		        
		    	
		        // Recibimos el mensaje
		        entrada.receive(recibir);
		        
		        String linea = new String(buf,0,buf.length);
		        
		        //linea=linea.trim(); //quita espacios
		        //separamos los datos recibidos.
		       StringTokenizer op = new StringTokenizer(linea);
		       String operando11 = op.nextToken();
		       String operacion = op.nextToken();
		       String operando21 = op.nextToken();
		       int operando1 = Integer.parseInt(operando11);
		       int operando2 = Integer.parseInt(operando21);
		      
		       
		        //Realizamos las operaciones
		       
		        if(operacion.trim().equals("+")){
		        System.out.println("La operación a realizar es: "+linea);
		        resultado = operando1 + operando2 + secreto;
		        System.out.println("El valor de la operación es: "+resultado);
		        }
		        else
		        if(operacion.trim().equals("-")){
		        System.out.println("La operación a realizar es: "+linea);
		        resultado = operando1 - operando2 + secreto;
		        System.out.println("El valor de la operación es: "+resultado);
		        }
		        else
		        if(operacion.trim().equals("x")){
		        System.out.println("La operación a realizar es: "+linea);
		        resultado = (operando1 * operando2) +secreto;
		        System.out.println("El valor de la operación es: "+resultado);
		        }
		        else
		        if(operacion.trim().equals("/")){
		        System.out.println("La operación a realizar es: "+linea);
		        resultado = (operando1 / operando2)+ secreto;
		        System.out.println("El valor de la operación es: "+resultado);
		        }
		        // Construimos el datagrama para enviar la respuesta
		        String s = ((Integer)resultado).toString();      
		        byte[] bufResp = s.getBytes();
		        DatagramPacket resp = new DatagramPacket(bufResp, bufResp.length, recibir.getAddress(), recibir.getPort());

		        // Se envia la respuesta
		        entrada.send(resp);
		      }
		} catch (SocketException e) {
			System.out.println("Puerto no valido.");
		} catch (IOException e) {
			System.out.println("Error al enviar o recibir el mensaje del cliente.");
		}

	}

}
