package entidades;

public class CPU {

	private Proceso proceso;
	private Integer clock;

	@Override
	public String toString() {
		return "CPU: proceso=" + proceso + ", clock=" + clock;
	}
//Inicializa el clock en cero.
	public CPU() {
		this.clock = 0;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public void incrementarClock() {
		this.clock++;
		if(null!=this.proceso) {
			this.proceso.decrementarTiempoIrupcion();
		}
	}

	public Integer getClock() {
		return clock;
	}

	public Boolean isEmpty() {
		return null == this.proceso;
	}
}
