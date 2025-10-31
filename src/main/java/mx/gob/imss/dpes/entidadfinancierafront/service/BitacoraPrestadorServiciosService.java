package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.PrestadorServiciosException;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoEFPSClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoEFPS;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class BitacoraPrestadorServiciosService extends ServiceDefinition<PrestadorServiciosModel, PrestadorServiciosModel>  {

    @Inject
    @RestClient
    private DocumentoEFPSClient bitacoraDocEFPSClient;

    private DocumentoEFPS crearDocumentoEFPS(Message<PrestadorServiciosModel> request,
        TipoPrestadorServiciosEnum tipoPrestadorServiciosEnum) {

        DocumentoEFPS documentoEFPS = new DocumentoEFPS();
        documentoEFPS.setCurp(request.getPayload().getCurp());

        if(
            TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServiciosEnum) &&
            request.getPayload().getArchivoPSCertificacion() != null &&
            request.getPayload().getPsCertificacionAlta().getId() != null &&
            request.getPayload().getRespuestaBovedaPSCertificacion() != null &&
            request.getPayload().getRespuestaBovedaPSCertificacion().getIdDocumento() != null &&
            request.getPayload().getDocumentoPSCertificacion() != null &&
            request.getPayload().getDocumentoPSCertificacion().getId() != null
        ) {
            documentoEFPS.setIdPrestadorServicios(request.getPayload().getPsCertificacionAlta().getId());
            documentoEFPS.setIdDocumento(request.getPayload().getDocumentoPSCertificacion().getId());
        } else if(
            TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA.equals(tipoPrestadorServiciosEnum) &&
            request.getPayload().getArchivoPSValidacionBiometrica() != null &&
            request.getPayload().getPsValidacionBiometricaAlta().getId() != null &&
            request.getPayload().getRespuestaBovedaPSValidacionBiometrica() != null &&
            request.getPayload().getRespuestaBovedaPSValidacionBiometrica().getIdDocumento() != null &&
            request.getPayload().getDocumentoPSValidacionBiometrica() != null &&
            request.getPayload().getDocumentoPSValidacionBiometrica().getId() != null
        ) {
            documentoEFPS.setIdPrestadorServicios(request.getPayload().getPsValidacionBiometricaAlta().getId());
            documentoEFPS.setIdDocumento(request.getPayload().getDocumentoPSValidacionBiometrica().getId());
        } else
            return null;

        return documentoEFPS;
    }

    private List<DocumentoEFPS> obtenerListaDocumentoEFPS(Message<PrestadorServiciosModel> request) {
        List<DocumentoEFPS> listaDocumentoEFPS = new ArrayList<DocumentoEFPS>();

        DocumentoEFPS documentoEFPSCertificado = this.crearDocumentoEFPS(request,
                TipoPrestadorServiciosEnum.CERTIFICADO);

        DocumentoEFPS documentoEFPSValidacionBiometrica = this.crearDocumentoEFPS(request,
                TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA);

        if(documentoEFPSCertificado != null)
            listaDocumentoEFPS.add(documentoEFPSCertificado);

        if(documentoEFPSValidacionBiometrica != null)
            listaDocumentoEFPS.add(documentoEFPSValidacionBiometrica);

        return listaDocumentoEFPS;
    }

    @Override
    public Message<PrestadorServiciosModel> execute(Message<PrestadorServiciosModel> request) throws BusinessException {
        try {
            List<DocumentoEFPS> listaDocumentoEFPS = this.obtenerListaDocumentoEFPS(request);

            if (listaDocumentoEFPS.isEmpty())
                return request;

            Response respuestaBitacoraDocEFPSClient = bitacoraDocEFPSClient.crearDocumentos(listaDocumentoEFPS);
            if (respuestaBitacoraDocEFPSClient != null && respuestaBitacoraDocEFPSClient.getStatus() == 200) {
                return request;
            }

        }
        catch (Exception e) {
            log.log(Level.SEVERE, "ERROR BitacoraPrestadorServiciosService.execute = {0}", e);
        }

        return response(
            null,
            ServiceStatusEnum.EXCEPCION,
            new PrestadorServiciosException(PrestadorServiciosException.ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA),
            null
        );
    }
}
