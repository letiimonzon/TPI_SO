package entidades;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	private Integer tamanioMemoria;
	private List<Particion> particiones = new ArrayList<Particion>();

	public Memoria(Integer tamanioMemoria, Integer tamanioSO) {
		this.tamanioMemoria = tamanioMemoria;
		Integer posicionInicio = 0;
		Integer posicionFin = tamanioSO - 1;
		Particion particionSO = new Particion(tamanioSO, posicionInicio, posicionFin, true);
		this.particiones.add(particionSO);
	}

	public void nuevaParticion(Integer tamañoKB) {
		Integer posicionInicio = particiones.get(0).getPosicionFin() + 1;
		Integer posicionFin = posicionInicio + tamañoKB - 1;
		Particion particion = new Particion(tamañoKB, posicionInicio, posicionFin, false);
		particiones.add(0, particion);
	}

	public Boolean isParticionEmpty() {
		Boolean empty = false;
		for (Particion particion : particiones) {
			empty = (!particion.isSo() && particion.isEmpty()) || empty;
		}
		return empty;
	}

	public Integer getTamanioMemoria() {
		return tamanioMemoria;
	}

	public List<Particion> getParticiones() {
		return particiones;
	}

	@Override
	public String toString() {
		return "Memoria: tamanioMemoria=" + tamanioMemoria + " particiones=\n" + particiones;
	}

}
