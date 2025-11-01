package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class EntidadFinancieraOTException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg030";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg031";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_ENTIDAD_FINANCIERA = "msg039";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_PRESTADOR_SERVICIO = "msg040";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO_EF_PS = "msg041";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_PERSONA = "msg67";

    public EntidadFinancieraOTException(String causa) {
        super(causa);
    }

}
