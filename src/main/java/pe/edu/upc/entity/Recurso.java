package pe.edu.upc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "recurso")
public class Recurso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idRecurso;

	@NotEmpty(message = "Ingresa el nombre del recurso")
	@Column(name = "nombreRecurso", nullable = false, length = 30)
	private String nombreRecurso;

	@NotEmpty(message = "Ingresa el tipo de unidad")
	@Column(name = "tipoUnidadRecurso", nullable = false, length = 20)
	private String tipoUnidadRecurso;

	@Min(1)
	@Max(1000)
	@Column(name = "precioRecurso", nullable = false)
	private int precioRecurso;

	private String foto;

	public Recurso(int idRecurso, String nombreRecurso, String tipoUnidadRecurso, int precioRecurso, String foto) {
		super();
		this.idRecurso = idRecurso;
		this.nombreRecurso = nombreRecurso;
		this.tipoUnidadRecurso = tipoUnidadRecurso;
		this.precioRecurso = precioRecurso;
		this.foto = foto;
	}

	public Recurso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(int idRecurso) {
		this.idRecurso = idRecurso;
	}

	public String getNombreRecurso() {
		return nombreRecurso;
	}

	public void setNombreRecurso(String nombreRecurso) {
		this.nombreRecurso = nombreRecurso;
	}

	public String getTipoUnidadRecurso() {
		return tipoUnidadRecurso;
	}

	public void setTipoUnidadRecurso(String tipoUnidadRecurso) {
		this.tipoUnidadRecurso = tipoUnidadRecurso;
	}

	public int getPrecioRecurso() {
		return precioRecurso;
	}

	public void setPrecioRecurso(int precioRecurso) {
		this.precioRecurso = precioRecurso;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}
