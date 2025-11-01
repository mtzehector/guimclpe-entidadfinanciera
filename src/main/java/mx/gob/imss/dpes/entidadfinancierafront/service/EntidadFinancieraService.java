/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.ResourceNotFoundException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CreateDocumentReq;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoObtenerClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.documento.model.DocumentoArchivo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class EntidadFinancieraService extends ServiceDefinition<EntidadFinancieraRequest, EntidadFinancieraRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraClient client;
    @Inject
    @RestClient
    private DocumentoObtenerClient documentoObtenerClient;

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {

        log.log(Level.INFO, "Inicia EntidadFinanciera: {0}", request.getPayload());
        //request.getPayload().getEntidadFinanciera().setMclcEstadoEf(1L);
        Response response = client.create(request.getPayload().getEntidadFinanciera());
        if (response.getStatus() == 200) {
            log.log(Level.INFO, "200 OK");
            EntidadFinancieraPersistenceRequest respuesta = response.readEntity(EntidadFinancieraPersistenceRequest.class);
            log.log(Level.INFO, "Repuesta :{0}", respuesta);
            Iterator<CondicionOfertaPersistenciaModelRequest> i
                    = request.getPayload().getMclcCondicionOfertaCollection().iterator();
            Collection<CondicionOfertaPersistenciaModelRequest> colecion
                    = new ArrayList();
            while (i.hasNext()) {
                CondicionOfertaPersistenciaModelRequest condicion = i.next();
                condicion.setMclcEntidadFinanciera(respuesta.getId());
                colecion.add(condicion);
            }
            request.getPayload().getEntidadFinanciera().setId(respuesta.getId());
            request.getPayload().setCondicionOferta(colecion);
            log.log(Level.INFO, "condiciones lista: {0}", request.getPayload().getCondicionOferta());
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

    public DocumentoArchivo getLogo(Long id) throws BusinessException {
        log.log(Level.INFO, ">>>entidadFinancieraFront EntidadFinancieraService.getLogo id:{0}", id);
        Documento documento = null;
        DocumentoArchivo documentoArchivo = null;
        try{
        Response response = client.getLogo(id);
        if (response.getStatus() == 200) {
            documento = response.readEntity(Documento.class);
            log.log(Level.INFO, ">>>entidadFinancieraFront EntidadFinancieraService.getLogo documento.getRefDocBoveda()=''{0}''" ,documento.getRefDocBoveda());
            log.log(Level.INFO, ">>>entidadFinancieraFront EntidadFinancieraService.getLogo documento.getRefDocBoveda().trim=''{0}''" ,documento.getRefDocBoveda().trim());
            CreateDocumentReq request = new CreateDocumentReq();
            Response load = documentoObtenerClient.obtenerImg(documento.getId());
            if (load.getStatus() == 200) {
                documentoArchivo = load.readEntity(DocumentoArchivo.class);
                
            } else{
                throw new EntidadFinancieraException(EntidadFinancieraException.LOGO_NOTFOUND);
            }
        } else {     
            throw new EntidadFinancieraException(EntidadFinancieraException.EF_NOTFOUND);
        }
        }
        catch(Exception e){
            log.log(Level.SEVERE, ">>>ERROR! entidadFinancieraFront EntidadFinancieraService sin logo");
            DocumentoArchivo sinLogo = new DocumentoArchivo();
            return sinLogo;
//            log.log(Level.SEVERE, ">>>ERROR! entidadFinancieraFront EntidadFinancieraService.getLogo={0}", e.getMessage());
//            throw new ResourceNotFoundException();
            
        }
        return documentoArchivo;
    }

	public Message<ConciliacionRequest> datosEntidadFinanciera(Message<ConciliacionRequest> request) throws BusinessException {
		try {
			Response response = client.getEntidadFinanciera(request.getPayload().getCveEntidadFinanciera());
			if (response.getStatus() == 200) {
				EntidadFinancieraPersistenceRequest respuesta = response.readEntity(EntidadFinancieraPersistenceRequest.class);
				
				request.getPayload().setRazonSocial(respuesta.getRazonSocial());
				request.getPayload().setNombreComercial( respuesta.getNombreComercial());
				String nombreRepresentante = respuesta.getNombreLegal() + " " + respuesta.getPrimerApeLegal() + " " + respuesta.getSegundoApeLegal();
				request.getPayload().setNombreRespresentante(nombreRepresentante);
				request.getPayload().setNumeroProveedor(Integer.parseInt(respuesta.getNumProveedor()));
				
				return request;
			} else {
				throw new EntidadFinancieraException(EntidadFinancieraException.EF_NOTFOUND);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, ">>>ERROR! entidadFinancieraFront EntidadFinancieraService con informacion incorrecta");
			return request;
		}
		

	}

}
