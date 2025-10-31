/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author Diego
 */
public class DescuentoException extends BusinessException{
  private final static String KEY = "MSG021-MSG027-MSG070";
  
  public DescuentoException() {
    super(KEY);
  }
  public DescuentoException(String resource) {
    super(resource);
  }
}

