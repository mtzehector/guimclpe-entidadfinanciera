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
 * @author juanf.barragan
 */
@Data
public class UpdateAdminRequest extends BaseModel{
    private String curpNuevo;
    private String correoAnterior;
    private String correoNuevo;
    private String curpAnterior;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String rfc;
}
