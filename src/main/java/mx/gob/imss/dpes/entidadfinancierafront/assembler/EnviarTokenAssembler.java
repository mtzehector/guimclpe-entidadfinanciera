/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.PerfilUsuarioEnum;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EnvioTokenModel;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class EnviarTokenAssembler extends BaseAssembler<EntidadFinancieraRequest, EnvioTokenModel> {
    
    @Override
    public EnvioTokenModel assemble(EntidadFinancieraRequest source) {
        
        EnvioTokenModel enviar = new EnvioTokenModel();
        
        enviar.setCorreo(source.getEntidadFinanciera().getCorreoAdmin());
        enviar.setCurp(source.getEntidadFinanciera().getCurpAdmin());
        enviar.setCvePerfil(PerfilUsuarioEnum.ADMINISTRADOR_EF.toValue());
        enviar.setRfc(source.getRfc());
        log.log(Level.INFO, ">>> ASSEMBLER ENVIO TOKEN: {0}", enviar);
        return enviar;
        
    }
}
