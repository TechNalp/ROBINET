package version_5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Prog {
	public static void main(String[] args){
		System.out.println("Le Serveur FTP");
		boolean r=true;

		ServerSocket serveurFTP;

		try {
			serveurFTP = new ServerSocket(8000);
			while(r) {
				try {
					Socket socket = serveurFTP.accept();
					Serveur sct = new Serveur(socket);
					Thread th =new Thread(sct);
					th.start();
				}catch(java.net.SocketException e) {System.out.println("bye");}
			}
			serveurFTP.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
}
