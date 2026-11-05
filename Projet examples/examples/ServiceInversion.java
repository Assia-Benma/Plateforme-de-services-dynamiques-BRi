package bri.examples;

import java.io.*;
import java.net.*;

import bri.Service;

// rien � ajouter ici
public class ServiceInversion implements Service {
	
	private final Socket client;
	
	public ServiceInversion(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			out.println("Tapez un texte à inverser");
			String line = in.readLine();

			if (line != null) {
				out.println(new StringBuilder(line).reverse().toString());
			}
		} catch (IOException e) {
			System.err.println("Fin du service d'inversion : " + e);
		} finally {
			try { client.close(); } catch (IOException e) { }
		}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
