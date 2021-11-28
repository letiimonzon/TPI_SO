package aux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import entidades.Particion;
import entidades.Proceso;
import entidades.SistemaOperativo;

public class Aux {
	private static Boolean isDebug = false;
	
	//Lee el archivo de procesos y devuelve una lista de procesos.
	public static List<Proceso> LoadFile(File file) {
		List<Proceso> procesos = new ArrayList<Proceso>();
		try {
			Aux.pathOut = file.getAbsolutePath();
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine() && procesos.size()<10) {
				String l = myReader.nextLine();
				if (!l.startsWith("#")) {
					String[] arrayL = l.split(",");
					Proceso proceso = new Proceso(arrayL);
					procesos.add(proceso);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return procesos;
	}

	// ordena la cola recibida por parámetro, por tiempo de arribo de menor a mayor.
	public static void ordenarTA(List<Proceso> procesos) {
		Collections.sort(procesos, new Comparator<Proceso>() {
			@SuppressWarnings("deprecation")
			@Override
			public int compare(Proceso p1, Proceso p2) {
				return new Integer(p1.getTiempoArribo()).compareTo(new Integer(p2.getTiempoArribo()));
			}
		});
	}

	// Ordena la cola recibida por parámetro por tiempo de irrupcion de menor a mayor.
	public static void ordenarTI(List<Proceso> procesos) {
		Collections.sort(procesos, new Comparator<Proceso>() {
			@SuppressWarnings("deprecation")
			@Override
			public int compare(Proceso p1, Proceso p2) {
				return new Integer(p1.getTiempoIrrupcion()).compareTo(new Integer(p2.getTiempoIrrupcion()));
			}
		});
	}		
	
//Muestra por pantalla lo requerido en el enunciado del TPI
	public static void imprimir(SistemaOperativo so) {
		if (!isDebug) {
			System.out.println("\n\n---------------------------------------------------");
			//El estado del procesador
			System.out.println("Clock:\t" + so.getCPU().getClock() + "\n");
			System.out.println("CPU:\tProceso: " + so.getCPU().getProceso().getId() + " TI restante: "
					+ so.getCPU().getProceso().getTiempoIrrupcion() + "\n");
			//La tabla de particiones de memoria
			System.out.println("Tabla de particiones:");
			for (Particion p : so.getMemoria().getParticiones()) {
				String idProceso = "---";
				if (null != p.getProceso())
					idProceso = p.getProceso().getId().toString();
				System.out.println("\t idParticion: " + p.getId() + " inicio: " + p.getPosicionInicio() + " tamanio: "
						+ p.getTamanio() + " idProceso: " + idProceso + " fragInterna: " + p.getFragmentacionInterna());
			}
			//El estado de la cola de procesos listos
			System.out.println("\nCola de listos:");
			for (Proceso p : so.getListos()) {
				System.out.println("\t idProceso: " + p.getId() + " TI restante: " + p.getTiempoIrrupcion());
			}
		}

	}

	
	public static void debug(SistemaOperativo so) {
		printFile("nuevoss: " + so.getNuevos());
		printFile("LyS: " + so.getListosSuspendidos());
		printFile("Listos: " + so.getListos());
		printFile(so.getMemoria().toString());
		printFile(so.getCPU().toString());
		printFile("Salientes: " + so.getSalientes());
		printFile("\n\n\n----------------------------------------------------\n\n\n");
	}
	
	private static String pathOut=null;
	private static String out = null;
	private static void printFile(String line) { 
		if(null == Aux.out)
			out = Aux.pathOut+"_"+System.currentTimeMillis()+".txt";
		try {
			FileWriter fw = new FileWriter(out, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    if(isDebug)
		    	System.out.println(line);
		    bw.write(line);
		    bw.newLine();
		    bw.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
