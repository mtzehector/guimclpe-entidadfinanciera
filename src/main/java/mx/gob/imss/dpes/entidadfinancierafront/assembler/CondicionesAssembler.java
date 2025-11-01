/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionFederativaRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CondicionesAssembler extends BaseAssembler<CondicionPersistenciaModel, CondicionFederativaRequest>{

    @Override
    public CondicionFederativaRequest assemble(CondicionPersistenciaModel source) {
        CondicionFederativaRequest out = new CondicionFederativaRequest();
        out.setCondicionFederativa(source.getMclcCondicionEntfedCollection());
        return out;
    }

 
    
}
