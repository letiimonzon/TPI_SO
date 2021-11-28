package entidades;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aux.Aux;

public class SistemaOperativo {
//Los valores que se definen son los correspondientes al enunciado del TPI
	private static Integer tamanioMemoria = 530;
	private static Integer tamanioParticionSO = 100;
	private Memoria memoria;
	private CPU cpu;
	/**
	 *  La cola LISTOS contiene los procesos que están cargados en memoria, esperando por CPU
	 */
	private List<Proceso> listos = new ArrayList<Proceso>();
	/**
	 * La cola SALIENTES contiene los procesos que terminaron su TI
	 */
	private List<Proceso> salientes = new ArrayList<Proceso>();
	/**
	 *  La cola NUEVOS contiene los procesos leidos de un archivo.
	 */
	private List<Proceso> nuevos = new ArrayList<Proceso>();
	/**
	 *  La cola LISTOSSUSPENDIDOS contiene los procesos que fueron admitidos pero NO están cargados en memoria.
	 */
 
	private List<Proceso> listoSuspendido = new ArrayList<Proceso>();

	public SistemaOperativo(File file) {
		inicializarMemoria();
		inicializarcpu();
		cargarProcesos(file);
		System.out.println("\n\n\n----------------------------------------------------\n\n\n");
		this.run();
	}

	public void run() {
		/**
		 *  Mientras haya procesos en CPU o en alguna de las colas seguirá ejecutándose los planificadores.
		 */
		while (!nuevos.isEmpty() || !listoSuspendido.isEmpty() || !listos.isEmpty() || !cpu.isEmpty()) {
			planificadorLargoPlazo();
			planificadorMedianoPlazo();
			planificadorCortoPlazo();

		}
	}

	/**
	 *  Mueve los procesos de la cola NUEVOS a ListosSuspendidos dependiendo del tiempo de arribo.
	 */
	public void planificadorLargoPlazo() {
		List<Proceso> tmp = new ArrayList<Proceso>();
		for (Proceso proceso : nuevos) {
			if (proceso.getTiempoArribo() == cpu.getClock()) {
				tmp.add(proceso);
			}
		}
		nuevos.removeAll(tmp);
		listoSuspendido.addAll(tmp);
	}

	/**
	 *  Mueve los procesos de ListosSuspendidos a LISTOS dependiendo si hay particiones libres. Selecciona la partición que genere menor FI
	 */
	
	public void planificadorMedianoPlazo() {
		Aux.ordenarTI(listoSuspendido);
		if (memoria.isParticionEmpty()) {
			// Hay particiones vacias. Intenta asignar los procesos que tienen TI menor a dichas particiones de memoria
			Boolean flag = false;
			List<Particion> particiones = this.memoria.getParticiones();
			int ixLyS = 0;
			List<Proceso> tmp = new ArrayList<Proceso>();
			while (memoria.isParticionEmpty() && listoSuspendido.size() > ixLyS) {
				Proceso p = listoSuspendido.get(ixLyS);
				Integer minDif = Integer.MAX_VALUE;
				int partIX = -1;
				for (Particion part : particiones) {
					if (!part.isSo() && part.isEmpty()) {
						if ((part.getTamanio() >= p.getTamanio()) && (part.getTamanio() - p.getTamanio() < minDif)) {
							minDif = part.getTamanio() - p.getTamanio();
							partIX = particiones.indexOf(part);
						}
					}
				}
				if (partIX >= 0) {
					particiones.get(partIX).setProceso(p);
					tmp.add(p);
					flag = true;
				}
				ixLyS++;
			}
			if (flag) {
				listos.addAll(tmp);
				listoSuspendido.removeAll(tmp);
			} else {
				VerificarCpuListos();
			}
		} else {
			// Todas las particiones están llenas
			VerificarCpuListos();
		}
	}

	/**
	 * Criterio de expropiación: https://github.com/letiimonzon/TPI_SO/blob/28f90690019bfd4f2bc2c68e6e04cf78b68189d9/Criterio%20de%20expropiaci%C3%B3n.jpeg
	 */
	public void VerificarCpuListos() {
		if (listoSuspendido.size() > 0) {
			Proceso plYs = listoSuspendido.get(0);
			/**
			 * Compara el tiempo de irrupción del proceso que quiere entrar con el que se encuentra en CPU. Si es menor procede a buscar una partición
			 */
			if (null != cpu.getProceso() && plYs.getTiempoIrrupcion() < cpu.getProceso().getTiempoIrrupcion()) {
				Aux.ordenarTI(listos);
				Integer n = listos.size() - 1;
				Integer nEncontrado = -1;
				/**
				 * Busca una posicion de memoria en la cola de LISTOS 
				 */
				while (n > -1 && nEncontrado < 0) {
					Proceso pn = listos.get(n);
					if (plYs.getTamanio() <= pn.getParticion().getTamanio()) {
						nEncontrado = n;
					}
					n--;
				}
				/**
				 * Se encontró una particion en la cola de LISTOS y hace la expropiación del proceso que esta en dicha partición
				 */
				if (nEncontrado > -1) {
					Proceso saliente = listos.remove((int) nEncontrado);
					Particion part = saliente.getParticion();
					saliente.setEmptyParticion();
					listoSuspendido.add(saliente);
					part.setProceso(plYs);
					listoSuspendido.remove(plYs);
					listos.add(plYs);
				} else {
					/**
					 * si no encuentró comprueba que la partición del proceso que está en CPU sea igual o mayor al tamaño que requiere el proceso
					 */
					
					if (plYs.getTamanio() <= cpu.getProceso().getParticion().getTamanio()) {
						Proceso saliente = cpu.getProceso();
						cpu.setProceso(null);
						Particion part = saliente.getParticion();
						saliente.setEmptyParticion();
						listoSuspendido.add(saliente);
						part.setProceso(plYs);
						listoSuspendido.remove(plYs);
						listos.add(plYs);
					}

				}
			} else {
				/**
				 * Busca una posición de memoria en la cola de LISTOS comparando tiempo de irrupción y tamaño de partición
				 */
				Aux.ordenarTI(listos);
				Integer n = listos.size() - 1;
				Integer nEncontrado = -1;
				while (n > -1 && nEncontrado < 0) {
					Proceso pp = listos.get(n);
					if (plYs.getTiempoIrrupcion() < pp.getTiempoIrrupcion()) {
						if (plYs.getTamanio() <= pp.getParticion().getTamanio()) {
							nEncontrado = n;
						}
					}
					n--;
				}
				/**
				 * Se encontró una particion en la cola de LISTOS y hace la expropiación del proceso que esta en dicha partición
				 */
				if (nEncontrado > -1) {
					Proceso saliente = listos.remove((int) nEncontrado);
					Particion part = saliente.getParticion();
					saliente.setEmptyParticion();
					listoSuspendido.add(saliente);
					part.setProceso(plYs);
					listoSuspendido.remove(plYs);
					listos.add(plYs);
				}
			}
		}
	}

	/**
	 *  Selecciona el proceso a ejecutarse aplicando SRTF. (Comparando los Tiempos de Irrupción). 
	 */
	
	public void planificadorCortoPlazo() {
		Aux.ordenarTI(listos);
		this.terminarProceso();
		if (listos.size() > 0) {
			Proceso victima = listos.remove((int) 0);
			if (cpu.isEmpty()) {
				// El procesador está Vacio
				cpu.setProceso(victima);
			} else {
				// procesador tiene algún proceso
				if (cpu.getProceso().getTiempoIrrupcion() > victima.getTiempoIrrupcion()) {
					this.listos.add(cpu.getProceso());
					cpu.setProceso(victima);
				} else {
					listos.add(victima);
				}
			}
		}
		Aux.debug(this);
		cpu.incrementarClock();
	}

	/**
	 * Termina el proceso actual (TI=0) moviéndolo a la cola SALIENTES y liberando la partición que ocupaba.
	 */
	
	public void terminarProceso() {
		if (null != cpu.getProceso()) {
			if (cpu.getProceso().getTiempoIrrupcion() == 0) {
				Aux.imprimir(this);
				salientes.add(cpu.getProceso());
				cpu.getProceso().getParticion().setProceso(null);
				cpu.getProceso().setEmptyParticion();
				cpu.setProceso(null);
				planificadorMedianoPlazo();
				Aux.ordenarTI(listos);
			}
		}
	}

/**
 * Crea las particiones fijas en memoria
 */
	private void inicializarMemoria() {
		System.out.print("Iniciando memoria..........");
		this.memoria = new Memoria(tamanioMemoria, tamanioParticionSO);
		this.memoria.nuevaParticion(250);
		this.memoria.nuevaParticion(120);
		this.memoria.nuevaParticion(60);
		System.out.println("OK");
		System.out.println(this.memoria.getParticiones());
	}

	private void inicializarcpu() {
		System.out.print("Iniciando cpu..........");
		this.cpu = new CPU();
		System.out.println("OK");
		System.out.println(this.cpu);
	}

/**
 * Lee los procesos del archivo pasado como parámetro
 */
	private void cargarProcesos(File file) {
		System.out.print("CargandoProcesos..........");
		this.nuevos = Aux.LoadFile(file);
		Aux.ordenarTA(nuevos);
		System.out.println("OK");
		System.out.println(nuevos);
	}

	public CPU getCPU() {
		return this.cpu;
	}

	public Memoria getMemoria() {
		return this.memoria;
	}

	public List<Proceso> getListos() {
		return this.listos;
	}

	public List<Proceso> getListosSuspendidos() {
		return this.listoSuspendido;
	}

	public List<Proceso> getNuevos() {
		return this.nuevos;
	}

	public List<Proceso> getSalientes() {
		return this.salientes;
	}

}
