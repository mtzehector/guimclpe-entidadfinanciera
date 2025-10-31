package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.model.ResumenConciliacionBean;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ResumenConciliacion;
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
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Provider
public class GeneraReporteResumenConciliacionService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest>  {

    @Inject
    private ObtenerDatosResumenConciliacionService obtenerDatosService;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            List<ResumenConciliacion> listResumenConciliacion = obtenerDatosService.obtenerDatosResumenConciliacion(request.getPayload());
            Map<String, Object> parametros = this.parametrosReporte(listResumenConciliacion);

            JasperReport report = (JasperReport) JRLoader.loadObject(new ClassPathResource("/reports/reporteResumenConciliacion.jasper").getInputStream());
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            request.getPayload().getDocumento().setArchivo(JasperExportManager.exportReportToPdf(print));

            return request;
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteEFPorCuentaContableService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_RESUMEN_CONCILIACION);
    }

    private Map<String, Object> parametrosReporte(List<ResumenConciliacion> listaResumen){
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("RESUMEN_DATA_SOURCE", new JRBeanCollectionDataSource(listaResumen));
        return parametros;
    }
}
