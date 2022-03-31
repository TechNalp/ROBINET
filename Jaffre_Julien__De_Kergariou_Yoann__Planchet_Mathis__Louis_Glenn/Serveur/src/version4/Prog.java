package version4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import version_ex3.Serveur;

public class Prog {
	
	public static void main(String[] args){
		System.out.println("Le Serveur FTP");
		boolean r=true;
	
			ServerSocket serveurFTP;
			try {
				serveurFTP = new ServerSocket(8000);
				while(r) {
					Socket socket = serveurFTP.accept();
					Serveur sct = new Serveur(socket);
					Thread th =new Thread(sct);
					th.start();
				}
				serveurFTP.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
			
			
	}

}