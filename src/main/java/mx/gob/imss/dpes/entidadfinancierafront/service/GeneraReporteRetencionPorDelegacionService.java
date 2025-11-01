package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PermisoItineranteYCostoAdministrativo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.RetencionDelegacionImporteFallecidos;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.RetencionPorDelegacion;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;


@Provider
public class GeneraReporteRetencionPorDelegacionService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    private ObtenerDatosRetencionPorDelegacionService obtenerDatosReporte;


    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            String path = null;
            PermisoItineranteYCostoAdministrativo permisoItinerante = obtenerDatosReporte.obtenerTasasEntidadFinanciera(
                    request.getPayload().getCveEntidadFinanciera(),
                    request.getPayload().getPeriodo()
            );
            List<RetencionPorDelegacion> lista = obtenerDatosReporte.obtenerDatosReporte(request.getPayload());
            RetencionDelegacionImporteFallecidos importeFallecidos =
                obtenerDatosReporte.obtenerImporteFallecidos(request.getPayload());

            Map<String, Object> parametros = parametros = this.parametrosReporte(
                    permisoItinerante,
                    lista,
                    request.getPayload().getPeriodo(),
                    importeFallecidos
            );

            //if (permisoItinerante.getPorcentajePermisoItinerante() != null && permisoItinerante.getPorcentajePermisoItinerante() > 0)
                //path = "/reports/reporteRetencionPorDelegacionCostoAdminYPermisoItinerante.jasper";
            //else
                path= "/reports/reporteRetencionPorDelegacionCostoAdmin.jasper";

            JasperReport report = (JasperReport) JRLoader.loadObject(new ClassPathResource(path).getInputStream());
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            request.getPayload().getDocumento().setArchivo(JasperExportManager.exportReportToPdf(print));

            return request;
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteRetencionPorDelegacionService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_RETENCION_POR_DELEGACION);
    }

    private Map<String, Object> parametrosReporte(PermisoItineranteYCostoAdministrativo porcentajes,
        List<RetencionPorDelegacion> lista, String periodo,
        RetencionDelegacionImporteFallecidos importeFallecidos) throws BusinessException {

        try {
            SimpleDateFormat formatoRecibido = new SimpleDateFormat(Constantes.FECHA_YYYY_MM);
            SimpleDateFormat formatoEnviado = new SimpleDateFormat(
                    Constantes.FECHA_MMMMM_YYYY,
                    new Locale("es", "ES")
            );

            Date fecha = formatoRecibido.parse(periodo);
            Map<String, Object> parametros = new HashMap<>();
            BigDecimal sumaNetoPagar = null;
            BigDecimal costoAdministrativo = null;
            BigDecimal ivaCostoAdministrativo = null;
            Double netoPagar = null;

            parametros.put("MES_ANIO", formatoEnviado.format(fecha).toUpperCase());

            parametros.put(
                    "ENTIDAD_FINANCIERA",
                    porcentajes != null && porcentajes.getNombreComercial() != null ?
                            porcentajes.getNombreComercial() : ""
            );
            parametros.put(
                    "NUMERO_PROVEEDOR",
                    porcentajes != null && porcentajes.getNumeroProveedor() != null ?
                            porcentajes.getNumeroProveedor() : 0
            );

            parametros.put("ENTIDADES_DATA_SOURCE", new JRBeanCollectionDataSource(lista));

            parametros.put(
                    "PORCENTAJE_COSTO_ADMIN",
                    porcentajes != null && porcentajes.getPorcentajeAdministracion() != null ?
                            porcentajes.getPorcentajeAdministracion() : 0.0
            );
            parametros.put("PORCENTAJE_IVA", Constantes.IVA.doubleValue());

            sumaNetoPagar = this.obtenerSumaNetoPagar(lista);

            costoAdministrativo = this.obtenerCostoAdministrativo(sumaNetoPagar,
                    porcentajes.getPorcentajeAdministracion());

            ivaCostoAdministrativo = this.obtenerIvaCostoAdministrativo(costoAdministrativo);

            sumaNetoPagar = sumaNetoPagar.setScale(2, RoundingMode.HALF_UP);
            costoAdministrativo = costoAdministrativo.setScale(2, RoundingMode.HALF_UP);
            ivaCostoAdministrativo = ivaCostoAdministrativo.setScale(2, RoundingMode.HALF_UP);

            parametros.put("SUMA_NETO_PAGAR", sumaNetoPagar.doubleValue());
            parametros.put("COSTO_ADMINISTRATIVO", costoAdministrativo.doubleValue());
            parametros.put("IVA_COSTO", ivaCostoAdministrativo.doubleValue());

            //if (porcentajes.getPorcentajePermisoItinerante() != null && porcentajes.getPorcentajePermisoItinerante() > 0)
            //    parametros.put("PORCENTAJE_ITINERANTE", porcentajes.getPorcentajePermisoItinerante());

            parametros.put(
                    "RECUPERACION_FALLECIDOS",
                    importeFallecidos != null && importeFallecidos.getImporteFallecidos() != null ?
                            importeFallecidos.getImporteFallecidos() : 0.0
            );

            netoPagar = this.obtenerNetoPagar(
                    sumaNetoPagar,
                    costoAdministrativo,
                    ivaCostoAdministrativo,
                    importeFallecidos.getImporteFallecidos()
            );
            parametros.put("NETO_PAGAR", netoPagar);

            return parametros;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR GeneraReporteRetencionPorDelegacionService.parametrosReporte()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_RETENCION_POR_DELEGACION);
    }

    private BigDecimal obtenerSumaNetoPagar(List<RetencionPorDelegacion> lista) {
        BigDecimal sumaNetoPagar = BigDecimal.ZERO;
        for (RetencionPorDelegacion retencion: lista) {
            sumaNetoPagar = sumaNetoPagar.add(
                retencion.getNetoPagar() == null? BigDecimal.ZERO : new BigDecimal(retencion.getNetoPagar().toString())
            );
        }
        return sumaNetoPagar;
    }

    private BigDecimal obtenerCostoAdministrativo(BigDecimal sumaNetoPagar, Double porcentajeCostoAdmin){
        BigDecimal porcentaje = porcentajeCostoAdmin == null ?
            BigDecimal.ZERO : new BigDecimal(porcentajeCostoAdmin.toString());

        return sumaNetoPagar.multiply(porcentaje);
    }

    private BigDecimal obtenerIvaCostoAdministrativo(BigDecimal costoAdmin){
        BigDecimal iva = new BigDecimal(Constantes.IVA.toString());
        iva = iva.divide(new BigDecimal("100"));

        return costoAdmin.multiply(iva);
    }

    private Double obtenerNetoPagar(BigDecimal netoPagar, BigDecimal costoAdmin, BigDecimal ivaCosto,
        Double fallecidos) {

        BigDecimal defunsiones = fallecidos == null ? BigDecimal.ZERO :
            new BigDecimal(fallecidos.toString()).setScale(2, RoundingMode.HALF_UP);

        return netoPagar.subtract(costoAdmin)
                   .subtract(ivaCosto)
                   .subtract(defunsiones)
                   .doubleValue();
    }
}
