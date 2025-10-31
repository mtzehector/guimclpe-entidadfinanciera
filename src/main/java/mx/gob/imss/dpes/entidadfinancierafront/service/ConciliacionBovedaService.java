package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CreateDocumentReq;
import mx.gob.imss.dpes.entidadfinancierafront.model.RespuestaBoveda;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BovedaClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

@Provider
public class ConciliacionBovedaService  extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private BovedaClient bovedaClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {

        CreateDocumentReq bovedaIn = new CreateDocumentReq();
        bovedaIn.getDocumento().setArchivo(request.getPayload().getDocumento().getArchivo());
        bovedaIn.getDocumento().setNombreArchivo(
                this.obtenerNombreDocumento(
                    request.getPayload().getCveTipoDocumento(),
                    request.getPayload().getPeriodo()
                ));
        bovedaIn.getTramite().setFolioTramite("0");
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.getDocumento().setExtencion(".pdf");
        bovedaIn.setSesion(request.getPayload().getSesion());
        try {
            Response respuestaBoveda = bovedaClient.create(bovedaIn);
            if (respuestaBoveda != null && respuestaBoveda.getStatus() == 200){
                RespuestaBoveda documentoBoveda = respuestaBoveda.readEntity(CreateDocumentReq.class).getRespuestaBoveda();
                this.poblarRespuestaBovedaRequest(request, documentoBoveda);
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ConciliacionBovedaService.execute()", e);
        }
        //LA POSIBLE EXCEPCION QUE SE PUEDE PRESENTAR AQUI
        //ES QUE NO SE HAYA CONFIGURADO EL SERVIDOR NGINX PARA QUE ACEPTE EN EL BODY UN ARCHIVO DE TAMAÑO
        //GRANDE, POR LO QUE SE DEBEN REVISAR DICHAS CONFIGURACIONES
        throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BOVEDA);
    }

    private String obtenerNombreDocumento(Long cveTipoDocumento, String periodo) throws BusinessException{

        if (cveTipoDocumento == null)
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BOVEDA);

        DateFormat formatoFecha = new SimpleDateFormat(Constantes.FECHA_YYYYMMDDHHMMSS);
        StringBuilder nombreArchivo = new StringBuilder();

       switch (cveTipoDocumento.intValue()){
           case 31:
               nombreArchivo.append("TR");
               break;
           case 32:
               nombreArchivo.append("CC");
               break;
           case 33:
               nombreArchivo.append("DEL");
               break;
           default:
               nombreArchivo.append(
                       TipoDocumentoEnum.forValue(cveTipoDocumento).getDescripcion()
               );
               break;
       }

       return nombreArchivo.append("-")
               .append(periodo)
               .append("-")
               .append(formatoFecha.format(new Date()))
               .toString();

    }

    private void poblarRespuestaBovedaRequest(Message<ConciliacionRequest> req, RespuestaBoveda respBoveda){
        req.getPayload().getRespuestaBoveda().setIdDocumento(respBoveda.getIdDocumento());
        req.getPayload().getRespuestaBoveda().setClave(respBoveda.getClave());
        req.getPayload().getRespuestaBoveda().setDescripcion(respBoveda.getDescripcion());
        req.getPayload().getRespuestaBoveda().setExito(respBoveda.isExito());
        req.getPayload().getRespuestaBoveda().setCodigoError(respBoveda.getDescripcionError());
        req.getPayload().getRespuestaBoveda().setArchivo(respBoveda.getArchivo());
    }
}
