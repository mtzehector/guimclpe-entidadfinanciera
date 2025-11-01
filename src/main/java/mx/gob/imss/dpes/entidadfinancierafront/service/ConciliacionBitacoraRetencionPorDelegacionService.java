package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class ConciliacionBitacoraRetencionPorDelegacionService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            List<Long> listEntidadesSinDocumento = new ArrayList<>();
            Long[] entidades = request.getPayload().getArregloIdEntidadFinanciera();

            for (Long cveEntidadFinanciera : entidades) {
                Response respuesta = this.obtenerDocumento(cveEntidadFinanciera, request.getPayload());
                if (respuesta != null && respuesta.getStatus() == 204) {
                    listEntidadesSinDocumento.add(cveEntidadFinanciera);
                }else{
                    DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
                    request.getPayload().getListRetencionPorDelegacion().add(documentoConciliacion.getDocumento());
                }
            }

            if (listEntidadesSinDocumento.size() > 0) {
                request.getPayload().setArregloIdEntidadFinanciera(listEntidadesSinDocumento.toArray(new Long[0]));
                request.getPayload().setEntidadesSinDocumento(listEntidadesSinDocumento.size());
            }

            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionBitacoraEFCuentaContableService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CONCILIACION);
        }
    }

    private Response obtenerDocumento(Long cveEntidadFinanciera, ConciliacionRequest req) {
        Response respuesta = bitacoraClient.obtenerDocumentoConciliacion(
                cveEntidadFinanciera,
                req.getCveTipoDocumento(),
                req.getPeriodo()
        );
        return respuesta;
    }
}
