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
public class EntidadFinancieraException extends BusinessException{
  private final static String KEY = "err002";
  public final static String EF_NOTFOUND = "err003";
  public final static String LOGO_NOTFOUND = "err002";
  public final static String ENTIDAD_FINANCIERA_OFERTA_SERVICE_ERROR = "msg037";

  public EntidadFinancieraException() {
    super(KEY);
  }
  public EntidadFinancieraException(String resource) {
    super(resource);
  }
}

