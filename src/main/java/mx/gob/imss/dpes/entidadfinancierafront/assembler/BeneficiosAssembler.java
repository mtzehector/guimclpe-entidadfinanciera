/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.BeneficioRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BeneficiosAssembler extends BaseAssembler<CondicionPersistenciaModel, BeneficioRequest>{

    @Override
    public BeneficioRequest assemble(CondicionPersistenciaModel source) {
        BeneficioRequest beneficioRequest = new BeneficioRequest();
        beneficioRequest.setBeneficio(source.getMclcBeneficioCollection());
        return beneficioRequest;
    }

   

 
    
}
