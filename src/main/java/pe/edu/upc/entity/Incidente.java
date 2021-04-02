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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "factura")
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
	private Lista_Compra listaCompras;

	

	@NotEmpty(message = "Ingresa el an\u00e1lisis de la factura")
	@Column(name = "analisis", nullable = false, length = 60)
	private String analisis;

	@NotNull(message = "La fecha es obligatoria")
	@Past(message = "La fecha debe estar en el pasado")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechaIncidente")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaIncidente;
	
	@Min(0)
	@Max(4000)
	@Column(name = "precio", nullable = false)
	private float precio;
	public Incidente(int idIncidente, Lista_Compra listaCompras, String analisis,
			Date FechaIncidente, float precio) {
		super();
		this.idIncidente = idIncidente;
		this.listaCompras = listaCompras;
		this.analisis = analisis;
		this.fechaIncidente = FechaIncidente;
		this.precio = precio;
		
	}

	public Incidente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getidIncidente() {
		return idIncidente;
	}

	public void setidIncidente(int idIncidente) {
		this.idIncidente = idIncidente;
	}

	public Lista_Compra getlistaCompras() {
		return listaCompras;
	}

	public void setlistaCompras(Lista_Compra listaCompras) {
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
	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

}
