package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraEstatusEFClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraEstatusEF;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class EntidadFinancieraBitacoraEstatusEFService
    extends ServiceDefinition<EntidadFinancieraRequest,EntidadFinancieraRequest> {
    @Inject
    @RestClient
    private BitacoraEstatusEFClient bitacoraEstatusEFClient;


    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia EntidadFinancieraBitacoraEstatusEFService = {0} *****", request);

        try {
            BitacoraEstatusEF bitacoraEstatusEF = request.getPayload().getBitacoraEstatusEF();
            if (bitacoraEstatusEF == null) {
                log.log(Level.INFO, "***** Finaliza EntidadFinancieraBitacoraEstatusEFService *****");
                return request;
            }
            else {
                if(bitacoraEstatusEF.getIdEntidadFinanciera() == null)
                    bitacoraEstatusEF.setIdEntidadFinanciera(request.getPayload().getEntidadFinanciera().getId());

                Response response = bitacoraEstatusEFClient.crearBitacoraEstatusEF(bitacoraEstatusEF);
                if(response != null && 200 == response.getStatus()) {
                    log.log(Level.INFO, "***** Finaliza EntidadFinancieraBitacoraEstatusEFService *****");
                    return request;
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! EntidadFinancieraBitacoraEstatusEFService = {0}", e);
        }

        return response(request.getPayload(),ServiceStatusEnum.EXCEPCION, null, null);
    }
    
}
