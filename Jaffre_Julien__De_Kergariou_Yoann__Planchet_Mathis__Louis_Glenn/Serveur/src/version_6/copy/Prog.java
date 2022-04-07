package version_6.copy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;



public class Prog {
	
	static Set<Integer> clientIds = new HashSet<Integer>();
	
	
	public static void main(String[] args){
		System.out.println("Serveur ROBI lancé");

		ServerSocket serveur;
		
	
		try {
			serveur = new ServerSocket(8000);
			//boucle multi-threading
			while(true) {
				try {
					Socket socket = serveur.accept();
					Serveur sct = new Serveur(socket);
					int i = 0;
					while(Prog.clientIds.contains(i)) {
						i++;
					}
					
					Thread th =new Thread(sct,""+i);
					Prog.clientIds.add(i);
					//lancement d'un thread par client connecté
					th.start();
				}catch(java.net.SocketException e) {System.out.println("bye");}
			}
			//serveur.close(); 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
