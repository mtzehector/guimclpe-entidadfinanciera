package mx.gob.imss.dpes.entidadfinancierafront.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CartaReciboExisteEFTodasService extends ServiceDefinition<PageRequestModelConciliacion, PageRequestModelConciliacion> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;
    @Inject
    @RestClient
    private PersonaClient personaClient;

    @Override
    public Message<PageRequestModelConciliacion> execute(Message<PageRequestModelConciliacion> request) throws BusinessException {
        try {
        	List<Long> listaEFFirmaOperador = operadorEFPuedeFirmar();
        	request.getPayload().getRequest().getModel().setFiltroEF(listaEFFirmaOperador);
        	 
            Response respuesta = bitacoraClient.obtenerListCartaReciboTodasEF(
                    request.getPayload().getRequest()
            );
            if (respuesta != null && respuesta.getStatus() == 200){
                return response(respuesta, request);
            }
            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboBitacoraService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    private List<Long> operadorEFPuedeFirmar() throws BusinessException {
        try {
            Response response = personaClient.obtenerOperadorEFFirma(0L);
            if (response != null && response.getStatus() == 200){
                List<Long> listEF = new ArrayList<>();
                List<PersonaUsuarioPerfil> lista = response.readEntity(new GenericType<List<PersonaUsuarioPerfil>>(){});
               
                for (PersonaUsuarioPerfil operadorEF: lista) {
                    if (operadorEF.getConteo() > 0) {
                        listEF.add(operadorEF.getCveEntidadFinanciera());
                    }
                }
                return listEF;
            }
            return new ArrayList<Long>();
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboExisteEFService.operadorEFPuedeFirmar()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    @Override
    protected Message<PageRequestModelConciliacion> onOk(Response response, Message<PageRequestModelConciliacion> request){
        PageModel<DocumentoConciliacionRequest> pageModel = response.readEntity(new GenericType<PageModel<DocumentoConciliacionRequest>>() {});
        List<DocumentoConciliacionRequest> lista = pageModel.getContent();
        List<DocumentoConciliacionRequest> listaNueva = new ArrayList<>();
        Long cveEntidad = 0L;
        Long aux = 0L;
        for (DocumentoConciliacionRequest documento: lista) {
            if (documento.getEntidadFinanciera().getId() != cveEntidad) {
                if (documento.getDocumento().getTipoDocumento().getTipo() == 36L) {
                    listaNueva.add(documento);
                }
                if (documento.getDocumento().getTipoDocumento().getTipo() == 35L) {
                    listaNueva.add(documento);
                }
                if (documento.getDocumento().getTipoDocumento().getTipo() == 29L) {
                    listaNueva.add(documento);
                }
            }
            cveEntidad = documento.getEntidadFinanciera().getId();
        }
        request.getPayload().setResponsePageModelCartaRecibo(pageModel);
        return request;
    }

}
