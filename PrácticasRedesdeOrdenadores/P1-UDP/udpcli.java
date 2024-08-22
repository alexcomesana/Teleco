import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class udpcli {

	public static void main(String[] args) {
		try {
			//Comprobacion de argumentos
			if(args.length != 5) {
				System.out.println("Sintaxis incorrecta. La sintaxis correcta es: <direccion_ip> <num_puerto> <operando1> <simbolo (sumar:+, restar:-,multiplicar:x o dividir:/)> <operando2>");
				System.out.println("Ejemplos:java udpcli 127.0.0.1 5000 12 + 4");
				System.out.println("java udpcli 127.0.0.1 8000 100 - 8");
				System.out.println("java udpcli 127.0.0.1 6000 10 x 6");
				System.out.println("java udpcli 127.0.0.1 7000 135 / 9");
				System.exit(0);
				
			}
			
			//Datos de entrada
			String dir = args[0].trim();
			InetAddress ip = InetAddress.getByName(dir);		
			int puerto = Integer.parseInt(args[1]);
			String operando1= args[2];
			String operando2 =args[4];
			String simbolo =args[3];
			String espacio = " ";
			String operacion = operando1+espacio+ simbolo +espacio+ operando2+espacio;
			
			byte[] outbuf = operacion.getBytes();
		        //Creamos el socket
		        
		        DatagramSocket socket = new DatagramSocket();
		        
			//Se contruye el datagrama para enviar el mensaje
			DatagramPacket out = new DatagramPacket(outbuf, outbuf.length,ip, puerto);
			socket.send(out);
			
			//Datagrama de la respuesta
			byte[] buf = new byte[1024];
			DatagramPacket resp = new DatagramPacket(buf, buf.length);
			
			//Se establecen 10 segundos como tiempo de espera de respuesta del servidor (temporizador)
			socket.setSoTimeout(10000);
			try {
				socket.receive(resp);
			} catch (IOException e) {
				System.out.println("Temporizador agotado el tiempo de espera de recepcion del mensaje  ha finalizado.");
				socket.close();
				System.exit(0);
			}
			
			//Finaliza el programa mostrando el valor final del acumulador
			System.out.println("El valor de la operacion es: " + new String(buf,0,resp.getLength()) + ".");	
			socket.close();
		
		} catch (SocketException e) {
		      System.out.println("Puerto o direccion IP no validos.");
		} catch (IOException e) {
		      System.out.println("Error al enviar el mensaje al servidor.");
		}	
	}

}
