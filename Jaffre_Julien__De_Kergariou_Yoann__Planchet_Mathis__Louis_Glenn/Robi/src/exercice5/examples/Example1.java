package exercice5.examples;

import exercice5.Exercice5_0;

public class Example1 {
	
	/* 
	 * Ajoute un rectangle robi avec ses propriétés par défaut
	 * On doit voir un rectangle bleu en (0,0)
	 * 
	 */
	String script = "(space add robi (Rect new))";
	
	
	public  void launch() {
		Exercice5_0 exo = new Exercice5_0();
		exo.oneShot(script);
	}
	
	public static void main(String[] args) {
		new Example1().launch();
	}
}
  