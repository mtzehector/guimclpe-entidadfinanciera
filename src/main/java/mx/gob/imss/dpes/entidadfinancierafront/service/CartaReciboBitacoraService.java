package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageModel;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionCartaReciboClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelConciliacion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CartaReciboBitacoraService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {
    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;
    @Inject
    @RestClient
    private ConciliacionCartaReciboClient cartaReciboClient;
    
    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            if (request.getPayload().getCveTipoDocumento() == 29 && request.getPayload().getFirma() != null && !request.getPayload().getFirma().isEmpty()){
                return request;
            }

            DocumentoConciliacionRequest documento = this.existeDocumento(
                    request.getPayload(),
                    request.getPayload().getCveTipoDocumento()
            );
            if (documento == null){
                request.getPayload().setCveTipoDocumento(TipoDocumentoEnum.CARTA_RECIBO_SIN_FIRMA.getTipo());
                documento = this.existeDocumento(
                        request.getPayload(),
                        request.getPayload().getCveTipoDocumento()
                );
            }
            if (documento != null)
                request.getPayload().setDocumento(documento.getDocumento());

            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboBitacoraService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    private DocumentoConciliacionRequest existeDocumento(ConciliacionRequest req, Long cveTipoDocumento) throws BusinessException {
        try {
            Response respuesta = bitacoraClient.obtenerDocumentoConciliacion(
                    req.getCveEntidadFinanciera(),
                    cveTipoDocumento,
                    req.getPeriodo()
            );
            if (respuesta != null && respuesta.getStatus() == 200){
                DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
                return documentoConciliacion;
            }
            return null;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboBitacoraService.existeDocumento()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }
    
    
    public Message<ConciliacionRequest> executeEF(Message<ConciliacionRequest> request) throws BusinessException {
        try {
        	DocumentoConciliacionRequest carta = this.ultimaCarta(request.getPayload().getCveEntidadFinanciera(),request.getPayload().getPeriodo());
        	if(carta != null) {
        	  request.getPayload().setDocumento(carta.getDocumento());
        	}
        	
        	return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboBitacoraService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }
    
    private DocumentoConciliacionRequest ultimaCarta(Long idEntidadFinanciera, String periodo) throws BusinessException {
        try {
        	
            Response respuesta = bitacoraClient.obtenerDocumentoFinal(idEntidadFinanciera, periodo);
            if (respuesta != null && respuesta.getStatus() == 200){
                DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
                return documentoConciliacion;
            }
            return null;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboBitacoraService.existeDocumento()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }
}
