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
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
	@NotNull(message="Debe registrar al menos una lista de compra")
	@JoinColumn(name = "idLista", nullable=false)
	private Lista_Compra listaIncidente;

	

	@NotEmpty(message = "Ingresa el an\u00e1lisis de la factura")
	@Column(name = "analisisIncidente", nullable = false, length = 60)
	private String analisisIncidente;

	@NotNull(message = "La fecha es obligatoria")
	@Past(message = "La fecha debe estar en el pasado")
	@Temporal(TemporalType.DATE)
	@Column(name = "fechaIncidente")
	@DateTimeFormat(iso = ISO.DATE)
	private Date fechaIncidente;
	

	public Incidente(int idIncidente, Lista_Compra listaIncidente, String analisisIncidente,
			Date FechaIncidente) {
		super();
		this.idIncidente = idIncidente;
		this.listaIncidente = listaIncidente;
		this.analisisIncidente = analisisIncidente;
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


	
	public String getAnalisisIncidente() {
		return analisisIncidente;
	}

	public void setAnalisisIncidente(String analisisIncidente) {
		this.analisisIncidente = analisisIncidente;
	}

	public Date getFechaIncidente() {
		return fechaIncidente;
	}

	public void setFechaIncidente(Date FechaIncidente) {
		this.fechaIncidente = FechaIncidente;
	}


}
