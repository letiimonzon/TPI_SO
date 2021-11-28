package entidades;

import java.util.Objects;

public class Proceso {
	private Integer id;
	private Integer tamanio;
	private Integer tiempoArribo;
	private Integer tiempoIrrupcion;
	private Particion particion;

	public Proceso(Integer id, Integer tiempoArribo, Integer tiempoIrrupcion, Integer tamanioKB) {
		this.id = id;
		this.tiempoArribo = tiempoArribo;
		this.tiempoIrrupcion = tiempoIrrupcion;
		this.tamanio = tamanioKB;
	}

	public Proceso(String[] arrayL) {
		this.id = Integer.parseInt(arrayL[0]);
		this.tamanio = Integer.parseInt(arrayL[1]);
		this.tiempoArribo = Integer.parseInt(arrayL[2]);
		this.tiempoIrrupcion = Integer.parseInt(arrayL[3]);
	}

	public void decrementarTiempoIrupcion() {
		this.tiempoIrrupcion--;
	}
	@Override
	public String toString() {
		String ps = "---";
		if(null!=particion)
			ps = particion.getId().toString();
		return "id=" + id + ", tamanio=" + tamanio + ", tiempoArribo=" + tiempoArribo
				+ ", tiempoIrrupcion=" + tiempoIrrupcion + ", particion="+ps+"\n";
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proceso other = (Proceso) obj;
		return Objects.equals(id, other.id);
	}


	public Integer getTamanio() {
		return tamanio;
	}

	public void setTamanio(Integer tamanio) {
		this.tamanio = tamanio;
	}


	public Integer getId() {
		return id;
	}


	public Integer getTiempoArribo() {
		return tiempoArribo;
	}


	public Integer getTiempoIrrupcion() {
		return tiempoIrrupcion;
	}

	public Particion getParticion() {
		return particion;
	}

	protected void setParticion(Particion particion){
		this.particion = particion;
	}
	
	public void setEmptyParticion() {
		this.particion.setProceso(null);
		this.particion=null;
	}

}
