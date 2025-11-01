package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.PrestadorServiciosException;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CargaDocumentoPrestadorServicioService extends ServiceDefinition<PrestadorServiciosModel, PrestadorServiciosModel>{

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    private Documento crearDocumento(Message<PrestadorServiciosModel> request,
         TipoPrestadorServiciosEnum tipoPrestadorServiciosEnum) {

        Documento documento = new Documento();
        documento.setCveSolicitud(0L);

        if(
            TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServiciosEnum) &&
            request.getPayload().getArchivoPSCertificacion() != null &&
            request.getPayload().getPsCertificacionAlta().getId() != null &&
            request.getPayload().getRespuestaBovedaPSCertificacion() != null &&
            request.getPayload().getRespuestaBovedaPSCertificacion().getIdDocumento() != null
        ) {
            documento.setTipoDocumento(TipoDocumentoEnum.CON_ENT_FIN_PRESTADOR_SERV_CERT);
            documento.setRefDocBoveda(request.getPayload().getRespuestaBovedaPSCertificacion().getIdDocumento());
            documento.setDescTipoDocumento(request.getPayload().getRespuestaBovedaPSCertificacion().getDescripcion());

        } else if(
                TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA.equals(tipoPrestadorServiciosEnum) &&
                request.getPayload().getArchivoPSValidacionBiometrica() != null &&
                request.getPayload().getPsValidacionBiometricaAlta().getId() != null &&
                request.getPayload().getRespuestaBovedaPSValidacionBiometrica() != null &&
                request.getPayload().getRespuestaBovedaPSValidacionBiometrica().getIdDocumento() != null
        ) {
            documento.setTipoDocumento(TipoDocumentoEnum.CON_ENT_FIN_PRESTADOR_SERV_VAL_BIO);
            documento.setRefDocBoveda(request.getPayload().getRespuestaBovedaPSValidacionBiometrica().
                getIdDocumento());
            documento.setDescTipoDocumento(request.getPayload().getRespuestaBovedaPSValidacionBiometrica().
                getDescripcion());

        } else
            return null;

        return documento;
    }

    private List<Documento> obtenerListaDocumentos(Message<PrestadorServiciosModel> request) {
        List<Documento> listaDocumentos = new ArrayList<Documento>();

        Documento documentoCertificado = this.crearDocumento(request,
                TipoPrestadorServiciosEnum.CERTIFICADO);

        Documento documentoValidacionBiometrica = this.crearDocumento(request,
                TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA);

        if(documentoCertificado != null)
            listaDocumentos.add(documentoCertificado);

        if(documentoValidacionBiometrica != null)
            listaDocumentos.add(documentoValidacionBiometrica);

        return listaDocumentos;
    }

    @Override
    public Message<PrestadorServiciosModel> execute(Message<PrestadorServiciosModel> request) throws BusinessException {

        try {
            List<Documento> listaDocumentos = this.obtenerListaDocumentos(request);

            if (listaDocumentos.isEmpty())
                return request;

            Response respuestaDocumentoClient = null;
            for(Documento documento : listaDocumentos) {
                respuestaDocumentoClient = documentoClient.create(documento);

                if (respuestaDocumentoClient != null && respuestaDocumentoClient.getStatus() == 200) {
                    if(TipoDocumentoEnum.CON_ENT_FIN_PRESTADOR_SERV_CERT.equals(documento.getTipoDocumento()))
                        request.getPayload().setDocumentoPSCertificacion(respuestaDocumentoClient.readEntity(Documento.class));
                    else if(TipoDocumentoEnum.CON_ENT_FIN_PRESTADOR_SERV_VAL_BIO.equals(documento.getTipoDocumento()))
                        request.getPayload().setDocumentoPSValidacionBiometrica(respuestaDocumentoClient.readEntity(Documento.class));
                }
            }

            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR CargaDocumentoPrestadorServicioService.execute = {0}", e);
        }

        return response(
            null,
            ServiceStatusEnum.EXCEPCION,
            new PrestadorServiciosException(PrestadorServiciosException.ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO),
            null
        );
    }
}
