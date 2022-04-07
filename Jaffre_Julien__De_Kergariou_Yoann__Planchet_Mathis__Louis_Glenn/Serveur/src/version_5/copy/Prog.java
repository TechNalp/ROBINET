package version_5.copy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Prog {
	public static void main(String[] args){
		System.out.println("Le Serveur FTP");
		boolean r=true;

		ServerSocket serveurFTP;
		int windw=0,windy=0;
		try {
			serveurFTP = new ServerSocket(8000);
			//boucle multi-threading
			while(r) {
				try {
					Socket socket = serveurFTP.accept();
					Serveur sct = new Serveur(socket,windw,windy);
					Thread th =new Thread(sct);
					//lancement d'un thread par client connecté
					th.start();
					//windw+=230;
				
				}catch(java.net.SocketException e) {System.out.println("client partie");}
			}
			serveurFTP.close(); 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
