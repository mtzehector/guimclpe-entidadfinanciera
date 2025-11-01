package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.model.ConciliacionBean;
import mx.gob.imss.dpes.entidadfinancierafront.model.ConciliacionImportes;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionSolicitudClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PermisoItineranteYCostoAdministrativo;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.springframework.core.io.ClassPathResource;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


@Provider
public class GeneraReporteDetalleConciliacionService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private ConciliacionSolicitudClient conciliacionSolicitudClient;

    @Inject
    private ObtenerDatosConciliacion obtenerDatos;
    @Inject
    private ObtenerDatosRetencionPorDelegacionService obtenerDatosRetencionPorDelegacionService;
    @Inject
    private EntidadFinancieraService entidadFinancieraService;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
        	request = entidadFinancieraService.datosEntidadFinanciera(request);
        	
            List<ConciliacionBean> listCasosRecuperados = obtenerDatos.obtenerCasosRecuperados(request.getPayload());
            List<ConciliacionBean> listCasosNoRecuperados = obtenerDatos.obtenerCasosNoRecuperados(request.getPayload());
            List<ConciliacionBean> listCasosDefuncion = obtenerDatos.obtenerCasosDefuncion(request.getPayload());

            PermisoItineranteYCostoAdministrativo permisoItineranteYCostoAdministrativo =
                    obtenerDatosRetencionPorDelegacionService.
                            obtenerTasasEntidadFinanciera(request.getPayload().getCveEntidadFinanciera(),
                                    request.getPayload().getPeriodo());

            ConciliacionImportes importes = obtenerDatos.obtenerImportes(
                    request.getPayload().getIva(),
                    BigDecimal.valueOf(permisoItineranteYCostoAdministrativo.getPorcentajeAdministracion()),
                    BigDecimal.valueOf(permisoItineranteYCostoAdministrativo.getPorcentajePermisoItinerante())
                );

            Map<String, Object> parametros = this.obtenerParametrosDataSource(
                    listCasosRecuperados,
                    listCasosNoRecuperados,
                    listCasosDefuncion,
                    request.getPayload(),
                    importes,
                    permisoItineranteYCostoAdministrativo
            );

            JasperReport report = (JasperReport) JRLoader.loadObject(
                new ClassPathResource("/reports/reporteConciliacion.jasper").getInputStream());
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            request.getPayload().getDocumento().setArchivo(JasperExportManager.exportReportToPdf(print));

            return request;
        } catch (BusinessException e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteDetalleConciliacionService.execute()", e);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteDetalleConciliacionService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_DETALLE_CONCILIACION);
    }

    private Map<String, Object> obtenerParametrosDataSource(List<ConciliacionBean> casosRecuperados,
        List<ConciliacionBean> casosNoRecuperados, List<ConciliacionBean> casosDefuncion, ConciliacionRequest req,
        ConciliacionImportes importesDetalle,
        PermisoItineranteYCostoAdministrativo permisoItineranteYCostoAdministrativo) {

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("casosRecuperadosDataSource", new JRBeanCollectionDataSource(casosRecuperados));
        parametros.put("casosNoRecuperadosDataSource", new JRBeanCollectionDataSource(casosNoRecuperados));
        parametros.put("casosDefuncionDataSource", new JRBeanCollectionDataSource(casosDefuncion));
        parametros.put("razonSocial", req.getRazonSocial());
        parametros.put("numeroProveedor", req.getNumeroProveedor());
        parametros.put("nomina", req.getPeriodo());
        parametros.put("fechaActual", new Date());
        parametros.put("casosRecuperados", casosRecuperados.size());
        parametros.put("importeTotalRecuperado", importesDetalle.getSumas()[0].doubleValue());
        parametros.put("casosNoRecuperados", casosNoRecuperados.size());
        parametros.put("porcentajeCostoAdministrativo", permisoItineranteYCostoAdministrativo.getPorcentajeAdministracion());
        parametros.put("porcentajePermisoItinerante", permisoItineranteYCostoAdministrativo.getPorcentajePermisoItinerante());
        parametros.put("importeCostoAdministrativo", importesDetalle.getImporteCostoAdministrativo() == null ? 0.0 : importesDetalle.getImporteCostoAdministrativo().doubleValue());
        parametros.put("importePermisoItinerante", importesDetalle.getImportePermisoItinerante() == null ? 0.0 : importesDetalle.getImportePermisoItinerante().doubleValue());
        parametros.put("iva", req.getIva().multiply(new BigDecimal("100")).doubleValue());
        parametros.put("importeIVACostoAdministrativo", importesDetalle.getImporteIVACostoAdministrativo() == null ? 0.0 : importesDetalle.getImporteIVACostoAdministrativo().doubleValue());
        parametros.put("importeIVAPermisoItinerante", importesDetalle.getImporteIVAPermisoItinerante() == null ? 0.0 : importesDetalle.getImporteIVAPermisoItinerante().doubleValue());
        parametros.put("importeNetoRecuperado", importesDetalle.getImporteNetoRecuperado() == null ? 0.0 : importesDetalle.getImporteNetoRecuperado().doubleValue());
        parametros.put("importeTotalCobradoDemasia", importesDetalle.getSumas()[2].doubleValue());
        parametros.put("importeBruto", importesDetalle.getImporteNetoRecuperado() == null ? 0.0 : importesDetalle.getImporteNetoRecuperado().doubleValue());
        parametros.put("importeNeto", importesDetalle.getImporteNetoDefuncion() == null ? 0.0 : importesDetalle.getImporteNetoDefuncion().doubleValue());
        return parametros;
    }

}
