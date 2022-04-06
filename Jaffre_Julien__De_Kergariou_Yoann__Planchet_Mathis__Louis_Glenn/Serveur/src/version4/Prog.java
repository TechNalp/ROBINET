package version4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import version_ex3.Serveur;


public class Prog {
	public static void main(String[] args){
		System.out.println("Le Serveur FTP");
		boolean r=true;

		ServerSocket serveurFTP=null;

		try {
			serveurFTP = new ServerSocket(8000);
			
		} catch (IOException e) {e.printStackTrace();}
			//boucle multi-threading
			while(r) {
				try {
					Socket socket = serveurFTP.accept();
					Serveur sct = new Serveur(socket);
					Thread th =new Thread(sct);
					//lancement d'un thread par client connecté
					th.start();
				}catch(java.net.SocketException e) {System.out.println("bye");}
				catch(IOException e) {e.printStackTrace();}
			}
		//	serveurFTP.close(); 
			
		
	}
}
