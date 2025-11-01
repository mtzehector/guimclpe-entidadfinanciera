/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author eduardo.loyo
 */
public class CatMaximoException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg030";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg031";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_BOVEDA = "msg032";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO = "msg033";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_CAT_MAXIMO = "msg034";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA_CAT_MAXIMO = "msg035";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA_CAT_EF = "msg036";

    public CatMaximoException(String causa) {
        super(causa);
    }

}
