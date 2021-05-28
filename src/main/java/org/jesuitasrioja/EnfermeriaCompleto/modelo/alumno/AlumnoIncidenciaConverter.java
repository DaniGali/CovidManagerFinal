package org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlumnoIncidenciaConverter {
	@Autowired
	private final ModelMapper modelMapper;

	public AlumnoIncidenciaConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public AlumnoIncidenciaDTO convertAlumnoToAlumnoIncidencia(Alumno alumno) {
		
		AlumnoIncidenciaDTO dto = modelMapper.map(alumno, AlumnoIncidenciaDTO.class);
		
		return dto;
	}

}
