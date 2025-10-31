package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ConciliacionException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg030";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg031";
    public final static String NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO = "msg048";
    public final static String ERROR_AL_INVOCAR_SERVICIO_ESTATUS_CONCILIACION = "msg046";
    public final static String NO_SE_ENCONTRO_INFORMACION = "msg047";
    public final static String ERROR_AL_INVOCAR_SERVICIO_BITACORA_CONCILIACION = "msg049";
    public final static String ERROR_AL_INVOCAR_SERVICIOS_REPORTE_CONCILIACION = "msg50";
    public final static String ERROR_AL_GENERAR_REPORTE_DETALLE_CONCILIACION = "msg51";
    public final static String ERROR_AL_INVOCAR_SERVICIO_BOVEDA = "msg52";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DOCUMENTO = "msg53";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DOCUMENTO_CONCILIACION = "msg54";
    public final static String ERROR_AL_COPIAR_PROPIEDADES = "msg55";
    public final static String ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO = "msg56";
    public final static String ERROR_AL_GENERAR_REPORTE_CARTA_RECIBO = "msg58";
    public final static String ERROR_AL_INVOCAR_SERVICIO_SOLICITUD = "msg59";
    public final static String ERROR_AL_INVOCAR_SERVICIO_ENTIDAD_FINANCIERA = "msg60";
    public final static String ERROR_AL_GENERAR_REPORTE_CUENTA_CONTABLE= "msg61";
    public final static String ERROR_AL_GENERAR_REPORTE_RETENCION_POR_DELEGACION= "msg62";
    public final static String ERROR_AL_GENERAR_REPORTE_SOLICITUD_TRANSFERENCIA= "msg63";
    public final static String ERROR_AL_INVOCAR_SERVICIO_CONCILIACION = "msg64";
    public final static String ERROR_AL_GENERAR_REPORTE_RESUMEN_CONCILIACION= "msg61";
    public final static String ERROR_AL_GENERAR_TRAMITE_EROGACIONES= "msg66";
    public final static String ERROR_AL_OBTENER_PORCENTAJE_IVA= "msg1500";



    public ConciliacionException(String messageKey) {
        super(messageKey);
    }
}
