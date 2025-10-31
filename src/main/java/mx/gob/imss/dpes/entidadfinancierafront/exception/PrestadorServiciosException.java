package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class PrestadorServiciosException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg030";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg031";
    public final static String ERROR_AL_INVOCAR_SERVICIO_PRESTADOR_DE_SERVCIOS = "msg042";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA = "msg043";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_BOVEDA = "msg044";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO = "msg045";



    public PrestadorServiciosException(String causa) {
        super(causa);
    }
}
