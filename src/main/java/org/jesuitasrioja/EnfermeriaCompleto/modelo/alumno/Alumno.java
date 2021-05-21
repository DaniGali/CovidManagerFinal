package org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.clase.Clase;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.datosMedicos.DatosMedicos;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.profesor.Profesor;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.responsable.Responsable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Alumno")
public class Alumno implements Serializable{
	
	@Id
	@Include
	private String identificador;
	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	
	@Column(name = "fechaNacimiento")
	private String fechaNacimiento;
	private String telefono;
	private String direccion;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Responsable> responsables;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "tutor")
	private Profesor tutor;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "alumno")
	private List<Incidencia> incidencias;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "clase")
	private Clase clase;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_datosmedicos")
	private DatosMedicos datosMedicos;
}
