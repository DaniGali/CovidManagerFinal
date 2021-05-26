package org.jesuitasrioja.EnfermeriaCompleto.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PcrNoEncontradoException extends RuntimeException{
	public PcrNoEncontradoException(String idPcr) {
		super("pcr with " + idPcr + " can not be retrieved");
	}
}
