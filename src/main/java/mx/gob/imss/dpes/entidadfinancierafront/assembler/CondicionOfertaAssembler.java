/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CondicionOfertaAssembler extends BaseAssembler<CondicionPersistenciaModel, CondicionOfertaPersistenceRequest>{

    @Override
    public CondicionOfertaPersistenceRequest assemble(CondicionPersistenciaModel source) {
       CondicionOfertaPersistenceRequest out = new CondicionOfertaPersistenceRequest();
       out.setCondicionOferta(source.getCondicionOferta());
       return out;
    }

   

 
    
}
