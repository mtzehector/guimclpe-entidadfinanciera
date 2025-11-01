package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PersonaClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.CartaRecibo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PermisoItineranteYCostoAdministrativo;
import mx.gob.imss.dpes.interfaces.persona.model.PersonaUsuarioPerfil;
import mx.gob.imss.dpes.support.util.ConvertirNumerosALetras;
import mx.gob.imss.dpes.support.util.DateUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.springframework.core.io.ClassPathResource;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Provider
public class GeneraReporteCartaReciboInicialService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    private ObtenerDatosCartaRecibo obtenerDatos;
    @Inject
    private ObtenerDatosRetencionPorDelegacionService obtenerDatosRetencionPorDelegacionService;
    @Inject
    private EntidadFinancieraService entidadFinancieraService;
    @Inject
    @RestClient
    private PersonaClient personaClient;
    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;
    
    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
        	request = entidadFinancieraService.datosEntidadFinanciera(request);

            PermisoItineranteYCostoAdministrativo permisoItineranteYCostoAdministrativo = obtenerDatosRetencionPorDelegacionService.obtenerTasasEntidadFinanciera(
                    request.getPayload().getCveEntidadFinanciera(),
                    request.getPayload().getPeriodo()
            );

            CartaRecibo cartaRecibo = obtenerDatos.obtenerDatosCartaRecibo(request.getPayload());
            
            if(request.getPayload().getCveTipoDocumento() == 35) {
            	request = this.recuperaFirmanteAdministrador(request);
            }
            
            Response response = personaClient.obtenerOperadorEFFirma(request.getPayload().getCveEntidadFinanciera());
            String rutaArchivoJasper = "";
            if (response != null && response.getStatus() == 200){
           		if(request.getPayload().getCveTipoDocumento() == 36) {
                	request = this.recuperaFirmanteAdministrador(request);
                	request = this.recuperaFirmanteOperadorEF(request);
                }
            	rutaArchivoJasper = "/reports/CartaReciboTitularDivAdminEFOperadorEF.jasper";
           	} else {
           		
           		if(request.getPayload().getCveTipoDocumento() == 36) {
                	request = this.recuperaFirmanteAdministrador(request);
                }
           		
           		rutaArchivoJasper = "/reports/CartaReciboTitularDivAdminEF.jasper";
           	}
            	  
            Map<String, Object> parametros = this.parametrosReporteEF(
                    cartaRecibo,
                    request.getPayload(),
                    permisoItineranteYCostoAdministrativo
            );
           	
            JasperReport report = (JasperReport) JRLoader.loadObject(
                    new ClassPathResource(rutaArchivoJasper).getInputStream()
            );										
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            request.getPayload().getDocumento().setArchivo(JasperExportManager.exportReportToPdf(print));

            return request;
        } catch (BusinessException e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteCartaReciboService.execute()", e);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteCartaReciboService.execute()", e);
        }

        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_CARTA_RECIBO);
    }
    
    private Map<String, Object> parametrosReporteEF(CartaRecibo cartaRecibo, ConciliacionRequest req, PermisoItineranteYCostoAdministrativo permisoItineranteYCostoAdministrativo) {
    	BigDecimal netoPagar = this.obtenerImporteNetoPagar(cartaRecibo);
        String importeLetra = this.obtenerImporteLetra(netoPagar);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("ENTIDADFINANCIERA", req.getRazonSocial());
        parametros.put("NOMBRECOMERCIAL", req.getNombreComercial());
        parametros.put("REPRESENTANTE", req.getNombreRespresentante());
        parametros.put("PERIODO", req.getPeriodo());
        parametros.put("PROVEEDOR", req.getNumeroProveedor());
        parametros.put("RFC", req.getRfc());
        parametros.put("MONTO", netoPagar == null ? 0.0 : netoPagar.doubleValue());
        parametros.put("MONTOLETRA", importeLetra == null || importeLetra.isEmpty() ? "" : importeLetra);
        parametros.put("MES", DateUtils.numeroDeMesALetra(Integer.valueOf(req.getPeriodo().substring(4, 6))));
        parametros.put("ANIO", Integer.valueOf(req.getPeriodo().substring(0, 4)));
        parametros.put("PORCENTAJEADMINISTRACION", permisoItineranteYCostoAdministrativo.getPorcentajeAdministracion());
        //parametros.put("PORCENTAJEITINERANTE", permisoItineranteYCostoAdministrativo.getPorcentajePermisoItinerante());
        parametros.put("IVA", req.getIva().doubleValue());
        parametros.put("IMPORTEPRIMAS", cartaRecibo.getPAGOTOTALPRIMAS() == null? 0.0 : cartaRecibo.getPAGOTOTALPRIMAS().doubleValue());
        parametros.put("IMPORTECOSTOSADMINISTRACION", cartaRecibo.getTASACOSTOADMIN() == null ? 0.0 : cartaRecibo.getTASACOSTOADMIN().doubleValue());
        parametros.put("IMPORTEIVA", cartaRecibo.getIVACOSTOADMIN() == null ? 0.0 : cartaRecibo.getIVACOSTOADMIN().doubleValue());
        //parametros.put("IMPORTEACCESOITINERANTE", cartaRecibo.getTASAPERMISOACCESOITINERANTE() == null ? 0.0 : cartaRecibo.getTASAPERMISOACCESOITINERANTE().doubleValue());
        //parametros.put("IMPORTEIVAITERANTE", cartaRecibo.getIVAPERMISOITINERANTE() == null ?  0.0 : cartaRecibo.getIVAPERMISOITINERANTE().doubleValue());
        parametros.put("IMPORTEPAGADODEMASIA", cartaRecibo.getIMPORTEPAGADODEFUNCIONES() == null ? 0.0 : cartaRecibo.getIMPORTEPAGADODEFUNCIONES().doubleValue());
        parametros.put("FIRMAADMINISTRADOREF", req.getFirmaAdministradorEF());
        parametros.put("FIRMATITULAR", req.getFirmaTitular());
        parametros.put("FIRMAOPERADOREF", req.getFirmaOperadorEF());
        parametros.put("OPERADOREF", req.getOperadorEF());
        parametros.put("TITULARIMSS", req.getTitularImss());
        return parametros;
    }
    
    private BigDecimal obtenerImporteNetoPagar(CartaRecibo cartaRecibo){
        if (cartaRecibo == null)
            return null;

        if (
                cartaRecibo.getPAGOTOTALPRIMAS() != null &&
                        cartaRecibo.getTASACOSTOADMIN() != null &&
                        cartaRecibo.getIVACOSTOADMIN() != null &&
                        //cartaRecibo.getTASAPERMISOACCESOITINERANTE() != null &&
                        //cartaRecibo.getIVAPERMISOITINERANTE() != null &&
                        cartaRecibo.getIMPORTEPAGADODEFUNCIONES() != null
        ) {
            return cartaRecibo.getPAGOTOTALPRIMAS()
                    .subtract(cartaRecibo.getTASACOSTOADMIN())
                    .subtract(cartaRecibo.getIVACOSTOADMIN())
                    //.subtract(cartaRecibo.getTASAPERMISOACCESOITINERANTE())
                    //.subtract(cartaRecibo.getIVAPERMISOITINERANTE())
                    .subtract(cartaRecibo.getIMPORTEPAGADODEFUNCIONES())
            ;
        }
        return null;
    }

    private String obtenerImporteLetra(BigDecimal netoPagar){

        if (netoPagar == null)
            return null;

        try {
            return ConvertirNumerosALetras.Convertir(
                    netoPagar.toString(),
                    "peso",
                    "pesos",
                    "",
                    "",
                    "",
                    true
            );
        }catch (Exception e){
            throw e;
        }

    }
    
    private Message<ConciliacionRequest> recuperaFirmanteAdministrador(Message<ConciliacionRequest> request){
    	Response respuesta = bitacoraClient.cartaReciboFirmadaAdministradorEF(request.getPayload().getCveEntidadFinanciera(), request.getPayload().getPeriodo(), TipoDocumentoEnum.CARTA_RECIBO_CON_FIRMA.toValue());
    	if (respuesta != null && respuesta.getStatus() == 200){
    		DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
    		 request.getPayload().setFirmaAdministradorEF(documentoConciliacion.getDocumento().getRefSello());
        }
    	return request;
    }
    
    private Message<ConciliacionRequest> recuperaFirmanteOperadorEF(Message<ConciliacionRequest> request){
    	Response respuesta = bitacoraClient.cartaReciboFirmadaAdministradorEF(request.getPayload().getCveEntidadFinanciera(), request.getPayload().getPeriodo(), TipoDocumentoEnum.CARTA_RECIBO_OPERADOR_EF.toValue());  	
    	if (respuesta != null && respuesta.getStatus() == 200){
    		DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
    		request.getPayload().setFirmaOperadorEF(documentoConciliacion.getDocumento().getRefSello());
            request.getPayload().setOperadorEF(documentoConciliacion.getDocumento().getRefDocumento());
        }
    	return request;
    }
    
}
