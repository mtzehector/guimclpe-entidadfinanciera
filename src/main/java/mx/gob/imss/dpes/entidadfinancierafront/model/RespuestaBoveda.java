/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
@Data
public class RespuestaBoveda extends BaseModel {

    private boolean exito;
    private String clave;
    private String descripcion;
    private String idDocumento;
    private String codigoError;
    private String descripcionError;
    private byte[] archivo;
}
