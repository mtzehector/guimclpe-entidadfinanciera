/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.common.enums.TipoSimulacionEnum;


/**
 *
 * @author Diego Velazquez
 */
public class PlazosRequest extends BaseModel{
    
    @Getter @Setter Pensionado pensionado;
    @Getter @Setter Double monto;
    @Getter @Setter Double capacidadCredito;
    @Getter @Setter TipoSimulacionEnum tipoSimulacion;
    @Getter @Setter Double descuentoMensual;
    @Getter @Setter Long sesion;
    
}
