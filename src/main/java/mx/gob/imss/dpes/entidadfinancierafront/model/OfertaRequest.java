/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.enums.TipoSimulacionEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author Diego Velazquez
 */
public class OfertaRequest extends BaseModel{
    
    @Getter @Setter Pensionado pensionado;
    @Getter @Setter double monto;
    @Getter @Setter double capacidadCredito;
    @Getter @Setter Long plazo;
    @Getter @Setter TipoSimulacionEnum tipoSimulacion;
    @Getter @Setter Double descuentoMensual;
    @Getter @Setter PrestamoRecuperacion prestamoRecuperacion;
}
