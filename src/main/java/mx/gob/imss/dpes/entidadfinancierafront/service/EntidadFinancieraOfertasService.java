package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class EntidadFinancieraOfertasService
    extends ServiceDefinition<EntidadFinancieraRequest, EntidadFinancieraRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request)
        throws BusinessException {

        if(request.getPayload().getEntidadFinanciera() != null &&
            request.getPayload().getEntidadFinanciera().getId() != null) {

            try {
                log.log(Level.INFO, "Inicia EntidadFinancieraOfertasService: {0}",
                        request.getPayload().getEntidadFinanciera().getId());

                Response responseEntidadFinanciera = entidadFinancieraClient.
                        getOfertasEntidadFinanciera(request.getPayload().getEntidadFinanciera().getId());

                if (responseEntidadFinanciera != null && responseEntidadFinanciera.getStatus() == 200) {

                    request.getPayload().setCondicionesOfertaPrevias(
                            responseEntidadFinanciera.readEntity(
                                    new GenericType<List<CondicionOfertaPersistenciaModelRequest>>() {
                                    }));

                    return request;
                }

            } catch (Exception e) {
                log.log(Level.SEVERE, ">>>>ERROR!!! EntidadFinancieraOfertasService = {0}", e);
            }
        } else {
            return request;
        }

        return response(null,ServiceStatusEnum.EXCEPCION,
            new EntidadFinancieraException(
                EntidadFinancieraException.ENTIDAD_FINANCIERA_OFERTA_SERVICE_ERROR), null);
    }
}
