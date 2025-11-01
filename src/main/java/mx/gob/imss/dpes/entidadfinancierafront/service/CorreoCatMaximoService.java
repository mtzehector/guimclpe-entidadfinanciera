/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CatMaximoClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CorreoCatMaximoClient;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author helio.sampe
 */
@Provider
public class CorreoCatMaximoService extends ServiceDefinition<CatMaximoModel, CatMaximoModel> {

    @Inject
    @RestClient
    private CatMaximoClient catMaximoClient;

    @Inject
    private Config config;

    @Inject
    @RestClient
    private CorreoCatMaximoClient correoCatMaximoClient;

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia CorreoCatMaximoService *****");

        Response responseCatMaximoClient = null;
        try {
            responseCatMaximoClient = catMaximoClient.getAdmins();
        }
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CorreoCatMaximoService al obtener los destinatarios del correo = {0}", e);
            return request;
        }

        if (responseCatMaximoClient != null && responseCatMaximoClient.getStatus() == 200) {

            Correo correo = new Correo();
            correo.setAsunto("Actualización de CAT Máximo.");
            correo.getCorreoPara().addAll(responseCatMaximoClient.readEntity(new GenericType<List<String>>() {}));
            correo.setRemitente("noreply.prestamos@imss.gob.mx");

            correo.setCuerpoCorreo(
                    String.format(
                            config.getValue("correoCatMaximo", String.class),
                            request.getPayload().getCatAnterior().toString(),
                            request.getPayload().getCatIMSS().toString()
                    )
            );

            Adjunto adjunto = new Adjunto();
            adjunto.setNombreAdjunto("CATMaximo.pdf");
            adjunto.setAdjuntoBase64(request.getPayload().getArchivo());
            correo.getAdjuntos().add(adjunto);

            try {
                correoCatMaximoClient.create(correo);
                log.log(Level.INFO, "***** Finaliza CorreoCatMaximoService *****");
            } catch(Exception e) {
                log.log(Level.SEVERE, ">>>>ERROR!!! CorreoCatMaximoService = {0}", e);
            }
        }

        return request;
    }

}
