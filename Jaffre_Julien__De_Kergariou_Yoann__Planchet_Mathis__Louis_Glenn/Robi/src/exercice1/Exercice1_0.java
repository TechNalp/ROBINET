package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import graphicLayer.GRect;
import graphicLayer.GSpace;

public class Exercice1_0 {
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();

	public Exercice1_0() {
		space.addElement(robi);
		space.open();
		
		
		//D�placement de robi jusqu�au bord droit
		while(robi.getX()<space.getBounds().getWidth()-robi.getWidth()-1) {
			robi.translate(new Point(1,0));			
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {}
		}
		//D�placement jusqu�au bord bas
		while(robi.getY()<space.getBounds().getHeight()-robi.getHeight()-1) {
			robi.translate(new Point(0,1));			
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {}
		}
		//D�placement jusqu�au bord gauche
		while(robi.getX()>0) {
			robi.translate(new Point(-1,0));			
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {}
		}
		//D�placement jusqu�au bord haut
		
		while(robi.getY()>0) {
			robi.translate(new Point(0,-1));			
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {}
		}
		robi.setColor(new Color((int) (Math.random() * 0x1000000)));
		
	}

	public static void main(String[] args) {
		new Exercice1_0();
	}

}
