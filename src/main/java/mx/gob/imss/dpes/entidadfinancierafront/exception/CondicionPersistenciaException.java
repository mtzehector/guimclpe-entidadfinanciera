package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CondicionPersistenciaException extends BusinessException{
  public final static String CONDICION_ENTIDAD_FINANCIERA_OFERTA_SERVICE_ERROR = "msg038";
  public CondicionPersistenciaException(String resource) {
    super(resource);
  }
}

