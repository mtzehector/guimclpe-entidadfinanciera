/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.EMailExistException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.EnviarTokenAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.UpdateAdminRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CorreoClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PersonaClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CorreoService extends ServiceDefinition<EntidadFinancieraRequest, EntidadFinancieraRequest> {
    
    @Inject
    @RestClient
    private CorreoClient client;
    
    @Inject
    @RestClient
    private PersonaClient personaClient;

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {
        
        log.log(Level.INFO, ">>>entidadFinancieraFront CorreoService.execute STEP ENVIO CORREO: {0}", request.getPayload());
        EnviarTokenAssembler assem = new EnviarTokenAssembler();
        Response response = null;
        Response resp = null;
        
        if (request.getPayload().getEnableModificar()) {
            if (
                    request.getPayload().getValueCorreModAdmin().equals(request.getPayload().getEntidadFinanciera().getCorreoAdmin()) &&
                            request.getPayload().getValueCurpModAdmin().equals(request.getPayload().getEntidadFinanciera().getCurpAdmin()) &&
                            request.getPayload().getValueRfcModAdmin().equals(request.getPayload().getEntidadFinanciera().getAdminRFC())
            ) {
                return new Message<>(request.getPayload());
            } else {
                try {
                    UpdateAdminRequest updateAdmin = new UpdateAdminRequest();
                    if(!request.getPayload().getValueCurpModAdmin().equals(request.getPayload().getEntidadFinanciera().getCurpAdmin())){
                        response = client.create(assem.assemble(request.getPayload()));
                        updateAdmin.setCurpAnterior(request.getPayload().getValueCurpModAdmin());
                        personaClient.inhabilitaAdmin(updateAdmin);
                    }else {
                        log.log(Level.INFO, "TERMINA EL ENVIO DE CORREO Y TOKEN AL NUEVO USUARIO");
                        log.log(Level.INFO, "Inicia la actualizacion del Administtrador en persona y usuario");
                        updateAdmin.setCurpNuevo(request.getPayload().getEntidadFinanciera().getCurpAdmin());
                        updateAdmin.setCorreoNuevo(request.getPayload().getEntidadFinanciera().getCorreoAdmin());
                        updateAdmin.setCorreoAnterior(request.getPayload().getValueCorreModAdmin());
                        updateAdmin.setCurpAnterior(request.getPayload().getValueCurpModAdmin());
                        updateAdmin.setRfc(request.getPayload().getRfc());
                        updateAdmin.setNombre(request.getPayload().getEntidadFinanciera().getNombreAdmin());
                        updateAdmin.setPrimerApellido(request.getPayload().getEntidadFinanciera().getPrimerApAdmin());
                        updateAdmin.setSegundoApellido(request.getPayload().getEntidadFinanciera().getSegundoApeAdmin());
                        log.log(Level.INFO, "Los datos actualizar son {0}", updateAdmin);
                        resp = personaClient.actualizaAdmin(updateAdmin);
                        log.log(Level.INFO, "Regresa de la actualizacion del admin", updateAdmin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
                    if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                        log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                        ExceptionUtils.throwServiceException("RegistroPensionadoFront");
                    }
                    if (e.getMessage().contains("Unknown error, status code 406")) {
                        log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR EMailExistException");
                        throw new EMailExistException();
                    }

                    throw new UnknowException();
                }
            }
        }else{
             try {
                    response = client.create(assem.assemble(request.getPayload()));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
                    if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                        log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                        ExceptionUtils.throwServiceException("RegistroPensionadoFront");
                    }
                    if (e.getMessage().contains("Unknown error, status code 406")) {
                        log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR EMailExistException");
                        throw new EMailExistException();
                    }

                    throw new UnknowException();
                }
        }

        if (response != null && response.getStatus() == 200) {
            return new Message<>(request.getPayload());
        }
        if (resp != null && resp.getStatus() == 200) {
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }
}
