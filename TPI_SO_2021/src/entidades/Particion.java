package entidades;

public class Particion {
	private static Integer ultimoId = 1;
	private Integer id;
	private Integer tamanio;
	private Integer posicionInicio;
	private Integer posicionFin;
	private Integer fragmentacionInterna;
	private Proceso proceso;
	private Boolean so;

	public Particion(Integer tamanio, Integer posicionInicio, Integer posicionFin, Boolean so) {
		this.id = Particion.ultimoId;
		Particion.ultimoId++;
		this.tamanio = tamanio;
		this.posicionInicio = posicionInicio;
		this.posicionFin = posicionFin;
		this.proceso = null;
		this.fragmentacionInterna = 0;
		this.so = so;
	}

	@Override
	public String toString() {
		String np = "----";
		if (so)
			np = "SO";
		else if (null != proceso)
			np = proceso.getId().toString();
		return "id=" + id + ", tamanio=" + tamanio + ", posicionInicio=" + posicionInicio + ", posicionFin="
				+ posicionFin + ", fragmentacionInterna=" + fragmentacionInterna + ", proceso=" + np + "\n";
	}

	public Boolean isSo() {
		return so;
	}

	public Boolean isEmpty() {
		return null == this.proceso;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
		if (null != this.proceso) {
			this.proceso.setParticion(this);
		}
		this.calcularFragmentacionInterna();
	}

	public Integer getId() {
		return id;
	}

	public Integer getTamanio() {
		return tamanio;
	}

	public Integer getPosicionInicio() {
		return posicionInicio;
	}

	public Integer getPosicionFin() {
		return posicionFin;
	}

	public Integer getFragmentacionInterna() {
		return fragmentacionInterna;
	}

	private void calcularFragmentacionInterna() {
		this.fragmentacionInterna = 0;
		if (null != this.proceso) {
			this.fragmentacionInterna = this.tamanio - this.proceso.getTamanio();
		}
	}
}
