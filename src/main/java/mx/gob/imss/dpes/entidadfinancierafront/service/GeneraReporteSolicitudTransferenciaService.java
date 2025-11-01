package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.SolicitudTransferencia;
import mx.gob.imss.dpes.support.util.ConvertirNumerosALetras;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

@Provider
public class GeneraReporteSolicitudTransferenciaService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    private ObtenerDatosSolicitudTransferenciaService obtenerDatosService;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            SolicitudTransferencia totales = obtenerDatosService.obtenerDatosReporte(request.getPayload());
            Map<String, Object> parametros = this.parametrosReporte(totales, request.getPayload());

            JasperReport report = (JasperReport) JRLoader.loadObject(new ClassPathResource("/reports/reporteSolicitudTransferencia.jasper").getInputStream());
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            request.getPayload().getDocumento().setArchivo(JasperExportManager.exportReportToPdf(print));

            return request;
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteSolicitudTransferenciaService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_SOLICITUD_TRANSFERENCIA);
    }

    private Map<String, Object> parametrosReporte(SolicitudTransferencia totales, ConciliacionRequest req) throws BusinessException {
        try {
            List<SolicitudTransferencia> listDataSource = new ArrayList<>();
            listDataSource.add(totales);
            SimpleDateFormat formatoRecibido = new SimpleDateFormat(Constantes.FECHA_YYYY_MM);
            SimpleDateFormat formatoEnviado = new SimpleDateFormat(
                    Constantes.FECHA_MMMMM_YYYY,
                    new Locale("es", "ES")
            );
            Date fecha = formatoRecibido.parse(req.getPeriodo());

            String cantidadLetra = "";
            String leyendaEF = "";
            if(req.getArregloIdEntidadFinanciera().length == 1) {
                cantidadLetra = "Una";
                leyendaEF = "entidad financiera";
            }
            else {
                cantidadLetra = ConvertirNumerosALetras.Convertir(
                        Integer.toString(req.getArregloIdEntidadFinanciera().length),
                        "",
                        "",
                        "",
                        "",
                        "",
                        false
                );

                cantidadLetra = cantidadLetra.replace("00/100 m.n.", "");
                leyendaEF = "entidades financieras";
            }
            InputStream is = new ClassPathResource("/reports/imagen/pieCarta.jpg").getInputStream();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("IMAGEN",is);
            parametros.put("NUMERO_ENTIDADES", req.getArregloIdEntidadFinanciera().length);
            parametros.put("NUMERO_ENTIDADES_LETRA", cantidadLetra);
            parametros.put("LEYENDA_EF", leyendaEF);
            parametros.put("MES_ANIO", formatoEnviado.format(fecha));
            parametros.put("TOTALES_DATA_SOURCE", new JRBeanCollectionDataSource(listDataSource));
            return parametros;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteSolicitudTransferenciaService.parametrosReporte()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_SOLICITUD_TRANSFERENCIA);
    }

}
