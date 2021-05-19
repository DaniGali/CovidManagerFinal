package org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoDTO implements Serializable{
	
	private String identificador;	
	private String dni;
	private String nombre;	
	private String apellido1;
	private String apellido2;
	private String telefono;
	private String direccion;

}
