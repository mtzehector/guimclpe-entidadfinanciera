package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraOTException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoEFPSClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoEFPS;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.EntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class DocumentoEFOTService extends ServiceDefinition<EntidadFinanciera, EntidadFinanciera> {

    @Inject
    @RestClient
    private DocumentoEFPSClient documentoEFPSClient;

    private Message<EntidadFinanciera> obtenerDocumentosPorTipoPrestadorServicios(Message<EntidadFinanciera> request,
            TipoPrestadorServiciosEnum tipoPrestadorServicios) throws BusinessException {

        try {
            Response response = documentoEFPSClient.obtenerListaDeDocumentos(request.getPayload().getId(),
                    tipoPrestadorServicios.getTipo());

            if(response != null) {
                if (200 == response.getStatus()) {
                    if (TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServicios))
                        request.getPayload().setDocumentosPSCertificado(
                                response.readEntity(new GenericType<List<DocumentoEFPS>>(){}));
                    else
                        request.getPayload().setDocumentosPSValidacionBiometrica(
                                response.readEntity(new GenericType<List<DocumentoEFPS>>(){}));

                    return request;
                } else if (204 == response.getStatus())
                    return request;
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE,
                    ">>>>ERROR!!! DocumentoEFOTService.obtenerDocumentosPorTipoPrestadorServicios = {0}", e);
        }

        throw new EntidadFinancieraOTException(
                EntidadFinancieraOTException.ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO_EF_PS);
    }

    private void validaExistenciaDocumentoEFPS(Message<EntidadFinanciera> request,
       TipoPrestadorServiciosEnum tipoPrestadorServicios) {

        boolean existe = false;
        PrestadorServiciosEF prestadorServiciosEF = null;
        List<DocumentoEFPS> documentos = null;

        if (TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServicios)) {
            prestadorServiciosEF = request.getPayload().getPrestadorServicioCertificado();
            documentos = request.getPayload().getDocumentosPSCertificado();
        }
        else {
            prestadorServiciosEF = request.getPayload().getPrestadorServicioValidacionBiometrica();
            documentos = request.getPayload().getDocumentosPSValidacionBiometrica();
        }

        if (prestadorServiciosEF != null && documentos != null && !documentos.isEmpty()) {
            Long idPrestadorServicios = prestadorServiciosEF.getId();
            for(DocumentoEFPS documento : documentos) {
                if(idPrestadorServicios.equals(documento.getIdPrestadorServicios())) {
                    existe = true;
                    break;
                }
            }
        }

        if (TipoPrestadorServiciosEnum.CERTIFICADO.equals(tipoPrestadorServicios))
            request.getPayload().setExisteDocPSCertificado(existe);
        else
            request.getPayload().setExisteDocPSValidacionBiometrica(existe);
    }

    @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        try {
            this.obtenerDocumentosPorTipoPrestadorServicios(request,
                    TipoPrestadorServiciosEnum.CERTIFICADO);
            this.validaExistenciaDocumentoEFPS(request,
                    TipoPrestadorServiciosEnum.CERTIFICADO);
            this.obtenerDocumentosPorTipoPrestadorServicios(request,
                    TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA);
            this.validaExistenciaDocumentoEFPS(request,
                    TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA);
            return request;
        }
        catch (EntidadFinancieraOTException e) {}
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! DocumentoEFOTService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
                new EntidadFinancieraOTException(
                        EntidadFinancieraOTException.ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO_EF_PS), null);
    }

}
