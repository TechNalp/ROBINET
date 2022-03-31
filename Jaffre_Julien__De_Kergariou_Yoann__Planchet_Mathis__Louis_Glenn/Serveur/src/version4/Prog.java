package version4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import version_ex3.Serveur;

public class Prog {

	public static void main(String[] args) throws Exception{
		System.out.println("Le Serveur FTP");
		boolean r=true;
		ServerSocket serveurFTP;

		serveurFTP = new ServerSocket(8000);
		while(r) {
			Socket socket = serveurFTP.accept();
			Serveur sct = new Serveur(socket);
			Thread th =new Thread(sct);
			th.start();
		}
		serveurFTP.close(); 
	}	
}

