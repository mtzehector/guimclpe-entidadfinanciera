package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.PrestadorServiciosException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CreateDocumentReq;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BovedaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Provider
public class BovedaPrestadorServiciosService extends ServiceDefinition<PrestadorServiciosModel, PrestadorServiciosModel>{
    private static final String CONTRATO_PS_CERTIFICACION = "ContratoPSCertificacion";
    private static final String CONTRATO_PS_VALIDACION_BIOMETRICA = "ContratoPSValidacionBiometrica";
    @Inject
    @RestClient
    private BovedaClient bovedaClient;

    private CreateDocumentReq crearDocumentoBoveda(Message<PrestadorServiciosModel> request,
        TipoPrestadorServiciosEnum tipoPrestadorServiciosEnum) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        CreateDocumentReq documentoBoveda = new CreateDocumentReq();
        documentoBoveda.getTramite().setFolioTramite("0");
        documentoBoveda.getUsuario().setIdUsr("1");
        documentoBoveda.getDocumento().setExtencion(".pdf");
        documentoBoveda.setSesion(request.getPayload().getSesion());

        if(
            TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServiciosEnum) &&
            request.getPayload().getArchivoPSCertificacion() != null &&
            request.getPayload().getPsCertificacionAlta().getId() != null
        ) {
            documentoBoveda.getDocumento().setArchivo(request.getPayload().getArchivoPSCertificacion());
            documentoBoveda.getDocumento().setNombreArchivo(CONTRATO_PS_CERTIFICACION +
                    dateFormat.format(new Date()));

        } else if(
            TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA.equals(tipoPrestadorServiciosEnum) &&
            request.getPayload().getArchivoPSValidacionBiometrica() != null &&
            request.getPayload().getPsValidacionBiometricaAlta().getId() != null
        ) {
            documentoBoveda.getDocumento().setArchivo(request.getPayload().getArchivoPSValidacionBiometrica());
            documentoBoveda.getDocumento().setNombreArchivo(CONTRATO_PS_VALIDACION_BIOMETRICA +
                    dateFormat.format(new Date()));
        } else
            return null;

        return documentoBoveda;
    }

    private List<CreateDocumentReq> obtenerListaDocumentosBoveda(Message<PrestadorServiciosModel> request) {
        List<CreateDocumentReq> listaDocumentosBoveda = new ArrayList<CreateDocumentReq>();

        CreateDocumentReq documentoBovedaCertificado = this.crearDocumentoBoveda(request,
            TipoPrestadorServiciosEnum.CERTIFICADO);

        CreateDocumentReq documentoBovedaValidacionBiometrica = this.crearDocumentoBoveda(request,
            TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA);

        if(documentoBovedaCertificado != null)
            listaDocumentosBoveda.add(documentoBovedaCertificado);

        if(documentoBovedaValidacionBiometrica != null)
            listaDocumentosBoveda.add(documentoBovedaValidacionBiometrica);

        return listaDocumentosBoveda;
    }

    @Override
    public Message<PrestadorServiciosModel> execute(Message<PrestadorServiciosModel> request) throws BusinessException {
        try {
            List<CreateDocumentReq> listaDocumentosBoveda = this.obtenerListaDocumentosBoveda(request);

            if (listaDocumentosBoveda.isEmpty())
                return request;

            Response responseBovedaClient = null;
            for(CreateDocumentReq documentoBoveda : listaDocumentosBoveda) {
                responseBovedaClient = bovedaClient.create(documentoBoveda);

                if (responseBovedaClient != null && responseBovedaClient.getStatus() == 200) {
                    if(documentoBoveda.getDocumento().getNombreArchivo().contains(CONTRATO_PS_CERTIFICACION))
                        request.getPayload().setRespuestaBovedaPSCertificacion(
                            responseBovedaClient.readEntity(CreateDocumentReq.class).getRespuestaBoveda());
                    else if(documentoBoveda.getDocumento().getNombreArchivo().contains(CONTRATO_PS_VALIDACION_BIOMETRICA))
                        request.getPayload().setRespuestaBovedaPSValidacionBiometrica(
                                responseBovedaClient.readEntity(CreateDocumentReq.class).getRespuestaBoveda());
                }
            }

            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR BovedaPrestadorServiciosService.execute = {0}", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new PrestadorServiciosException(PrestadorServiciosException.ERROR_AL_INVOCAR_SERVICIO_DE_BOVEDA),
                null
        );
    }
}
