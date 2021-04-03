package pe.edu.upc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "incidente")
public class Incidente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idIncidente;

	@ManyToOne
	@JoinColumn(name = "idLista")
	private Lista_Compra listaIncidente;

	

	@NotEmpty(message = "Ingresa el an\u00e1lisis de la factura")
	@Column(name = "analisis", nullable = false, length = 60)
	private String analisis;

	@NotNull(message = "La fecha es obligatoria")
	@Past(message = "La fecha debe estar en el pasado")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechaIncidente")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaIncidente;
	

	public Incidente(int idIncidente, Lista_Compra listaIncidente, String analisis,
			Date FechaIncidente) {
		super();
		this.idIncidente = idIncidente;
		this.listaIncidente = listaIncidente;
		this.analisis = analisis;
		this.fechaIncidente = FechaIncidente;
		
	}

	public Incidente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdIncidente() {
		return idIncidente;
	}

	public void setIdIncidente(int idIncidente) {
		this.idIncidente = idIncidente;
	}

	public Lista_Compra getListaIncidente() {
		return listaIncidente;
	}

	public void setListaIncidente(Lista_Compra listaIncidente) {
		this.listaIncidente = listaIncidente;
	}


	
	public String getanalisis() {
		return analisis;
	}

	public void setanalisis(String analisis) {
		this.analisis = analisis;
	}

	public Date getFechaIncidente() {
		return fechaIncidente;
	}

	public void setFechaIncidente(Date FechaIncidente) {
		this.fechaIncidente = FechaIncidente;
	}


}
