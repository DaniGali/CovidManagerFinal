package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jesuitasrioja.EnfermeriaCompleto.modelo.actuacion.Actuacion;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.Alumno;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.alumno.AlumnoDTO;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.Incidencia;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.IncidenciaDTO;
import org.jesuitasrioja.EnfermeriaCompleto.modelo.incidencia.IncidenciaDTOConverter;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.AlumnoService;
import org.jesuitasrioja.EnfermeriaCompleto.persistencia.services.IncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class IncidenciaController {
	
	@Autowired
	private IncidenciaService is;
	
	@Autowired
	private AlumnoService as;
	
	@Autowired
	IncidenciaDTOConverter incidenciaDTOConverter;
	
	/*
	 * 
	 * POST incidencias
	 * 
	 * */
	
	@ApiOperation(value = "Añadir un alumno",
			 notes = "Con este metodo conseguimos añadir un Alumno.")
	@PostMapping("/incidencias")
	public String postIncidencias(@RequestBody Incidencia incidencia) {
		return is.save(incidencia).toString();
	}
	
	/*
	 * 
	 * GET incidencias: 
	 * 
	 * */
	
	@ApiOperation(value = "Obtener todos los alumnos paginados",
			 notes = "Con este metodo conseguimos mandar todos los alumnos de 10 en 10. Así la Web podrá recoger los datos mas facilmente.")
	@GetMapping("/incidencias")
	public ResponseEntity<?> allIncidencias(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Incidencia> pagina = is.findAll(pageable);
		
		// transformar elementos de la pagina a DTO
				Page<IncidenciaDTO> paginaDTO = pagina.map(new Function<Incidencia, IncidenciaDTO>() {
					@Override
					public IncidenciaDTO apply(Incidencia i) {
						return incidenciaDTOConverter.convertIncidenciaToIncidenciaDTO(i);
					}
				});
		
		return ResponseEntity.status(HttpStatus.OK).body(paginaDTO);

	}
	
	/*
	 * 
	 * POST incidencia/alumno/{idAlumno}
	 * 
	 * */
	
	@ApiOperation(value = "Añadir una incidencia",
			 notes = "Con este metodo conseguimos añadir una incidencia para un alumno.")
	@PostMapping("/incidencia/alumno/{id}")
	public ResponseEntity<Alumno> postIncidencia(@RequestBody Incidencia nuevaIncidencia, @PathVariable String id) {
		List<Incidencia> incidencias = new ArrayList<Incidencia>();
		incidencias.add(nuevaIncidencia);
		Optional<Alumno> alumnoOptional = as.findById(id);
		if (alumnoOptional.isPresent()) {
			Alumno alumno=alumnoOptional.get();
			alumno.setIncidencias(incidencias);
			Alumno alunoSAved = as.save(alumno);
			return ResponseEntity.status(HttpStatus.OK).body(alunoSAved);
		} else {
			throw new AlumnoNoEncontradoException(id);
		}
	}
	
	/*
	 * 
	 * DELETE incidencia/{idIncidencia}
	 * 
	 **/
	
	@ApiOperation(value = "Borrar una incidencia",
			 notes = "Con este metodo conseguimos borrar una Incidencia por identificador. De esta forma conseguiremos borrar una Incidencia específica.")
	@DeleteMapping("/incidencia/{id}")
	public String deleteIncidencia(@PathVariable String id) {
		is.deleteById(id);
		return "OK";
	}
	
	/*
	 * 
	 * GET incidencia/{idIncidencia}
	 * 
	 * */
	
	@ApiOperation(value = "Obtener una incidencia por identificador",
			 notes = "Con este metodo conseguimos recoger la información de una incidencia específica.")
	@GetMapping("/incidencia/{id}")
	public ResponseEntity<Incidencia> getIncidencia(@PathVariable String id) {

		Optional<Incidencia> incidenciaOptional = is.findById(id);
		if (incidenciaOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(incidenciaOptional.get());
		} else {
			throw new IncidenciaNoEncontradaException(id);
		}
	}
	
	/*
	 * 
	 * GET incidencias/alumno/{idAlumno}
	 * 
	 * */
	
	@ApiOperation(value = "Obtener una clase por identificador",
			 notes = "Con este metodo conseguimos recoger la información de una clase específica.")
	@GetMapping("/incidencias/alumno/{id}")
	public ResponseEntity<?> getIncidenciasAlumno(@PageableDefault(size = 10, page = 0) Pageable pageable, @PathVariable String id) {

		Optional<Alumno> alumno = as.findById(id);
		
		List<Incidencia> incidencias = is.findAll();
		List<Incidencia> incidenciasAlumno = new ArrayList<Incidencia>();
		Page<Incidencia> pagina = null;
		
		for (Incidencia incidencia : incidencias) {
			if(incidencia.getAlumno().equals(alumno.get())) {
				incidenciasAlumno.add(incidencia);
				pagina = new PageImpl<Incidencia>(incidenciasAlumno, pageable, incidenciasAlumno.size());
			}
		}	
		return ResponseEntity.status(HttpStatus.OK).body(incidenciasAlumno);
	}
	
	/*
	 * 
	 * GET incidencias?from=<fechaInicio>&to=<fechafin>
	 * 
	 * */
	
	@ApiOperation(value = "Obtener una clase por identificador",
			 notes = "Con este metodo conseguimos recoger la información de una clase específica.")
	@GetMapping("/incidencia")
	public ResponseEntity<?> getIncidenciasRango(@PageableDefault(size = 10, page = 0) Pageable pageable, @RequestParam(name = "from") Date from, @RequestParam(name = "to") Date to) {
		
		Page<Incidencia> incidencias = is.encontrarPorRangoFechas(from, to, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(incidencias.get());
		
	}

}
