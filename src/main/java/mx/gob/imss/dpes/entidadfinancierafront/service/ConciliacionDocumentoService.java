package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.documento.model.TipoDocumentoFront;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.DatosAdicionalesCartaRecibo;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Date;
import java.util.logging.Level;

@Provider
public class ConciliacionDocumentoService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest>  {

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {

        Documento documento = new Documento();
        documento.setCveSolicitud(0L);
        documento.setTipoDocumento(
                TipoDocumentoEnum.forValue(request.getPayload().getCveTipoDocumento())
        );
        documento.setRefDocBoveda(request.getPayload().getRespuestaBoveda().getIdDocumento());
        documento.setAltaRegistro(new Date());
        documento.setCveEntidadFinanciera(
                request.getPayload().getCveEntidadFinanciera() == null ? 0 : request.getPayload().getCveEntidadFinanciera()
        );
        documento.setIndDocumentoHistorico(false);
        
        if(documento.getTipoDocumento() == TipoDocumentoEnum.CARTA_RECIBO_CON_FIRMA) {
        	documento.setRefSello(request.getPayload().getFirmaAdministradorEF());
        	documento.setRefDocumento(request.getPayload().getNombreRespresentante());
        }
        
        if(documento.getTipoDocumento() == TipoDocumentoEnum.CARTA_RECIBO_OPERADOR_EF) {
        	documento.setRefSello(request.getPayload().getFirmaOperadorEF());
        	documento.setRefDocumento(request.getPayload().getOperadorEF());
        }
        
        if(documento.getTipoDocumento() == TipoDocumentoEnum.CARTA_RECIBO_TITULAR_DIVISION) {
        	documento.setRefSello(request.getPayload().getFirmaTitular());
        	documento.setRefDocumento(request.getPayload().getTitularImss());
        }
        
        try {
        	Response respuestaDocumento = documentoClient.create(documento);
            if (respuestaDocumento != null && respuestaDocumento.getStatus() == 200){
                Documento documentoGuardado = respuestaDocumento.readEntity(Documento.class);
                documentoGuardado.setTipoDocumentoEnum(new TipoDocumentoFront(documento.getTipoDocumento()));
                request.getPayload().setDocumento(documentoGuardado);
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ConciliacionDocumentoService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_DOCUMENTO);
    }
}
