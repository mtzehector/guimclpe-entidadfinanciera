package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageModel;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PersonaClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelConciliacion;
import mx.gob.imss.dpes.interfaces.persona.model.PersonaUsuarioPerfil;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CartaReciboExisteEFService extends ServiceDefinition<PageRequestModelConciliacion, PageRequestModelConciliacion> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;
    @Inject
    @RestClient
    private PersonaClient personaClient;

    @Override
    public Message<PageRequestModelConciliacion> execute(Message<PageRequestModelConciliacion> request) throws BusinessException {
        try {
            ConciliacionRequest req = request.getPayload().getRequest().getModel();
            Message<PageRequestModelConciliacion> pageRequest = this.existeDocumento(request);
            boolean firmaOperadorEF = false;

            if (pageRequest == null){
                //CASO MONTE DE PIEDAD
                firmaOperadorEF = this.operadorEFPuedeFirmar(req);
                switch (req.getCvePerfil().intValue()) {
                    //Titular(operadorIMSS)
                    case 6:
                        req.setCveTipoDocumento(
                                firmaOperadorEF? TipoDocumentoEnum.CARTA_RECIBO_OPERADOR_EF.getTipo() :
                                                 TipoDocumentoEnum.CARTA_RECIBO_CON_FIRMA.getTipo()
                        );
                        break;
                }
                pageRequest = this.existeDocumento(request);
            }

            return pageRequest == null? new Message<>(new PageRequestModelConciliacion()) : pageRequest;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboExisteEFService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    private Message<PageRequestModelConciliacion> existeDocumento(Message<PageRequestModelConciliacion> request) throws BusinessException {
        try {
            Response respuesta = bitacoraClient.obtenerCartaReciboEntidadFinanciera(
                    request.getPayload().getRequest()
            );
            if (respuesta != null && respuesta.getStatus() == 200)
                return response(respuesta, request);

            return null;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboExisteEFService.existeDocumento()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    private boolean operadorEFPuedeFirmar(ConciliacionRequest req) throws BusinessException {
        try {
            Response response = personaClient.obtenerOperadorEFFirma(
                    req.getCveEntidadFinanciera()
            );
            if (response != null && response.getStatus() == 200){
                List<PersonaUsuarioPerfil> lista = response.readEntity(new GenericType<List<PersonaUsuarioPerfil>>(){});
                PersonaUsuarioPerfil operador = lista.get(0);
                return operador.getConteo() > 0;
            }
            return false;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboExisteEFService.operadorEFPuedeFirmar()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    @Override
    protected Message<PageRequestModelConciliacion> onOk(Response response, Message<PageRequestModelConciliacion> request){
        PageModel<DocumentoConciliacionRequest> pageModel = response.readEntity(new GenericType<PageModel<DocumentoConciliacionRequest>>() {});
        request.getPayload().setResponsePageModelCartaRecibo(pageModel);
        return request;
    }
}
