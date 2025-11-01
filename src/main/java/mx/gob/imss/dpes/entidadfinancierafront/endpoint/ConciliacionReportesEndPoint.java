package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.service.*;
import mx.gob.imss.dpes.entidadfinancierafront.validation.ValidaConciliacionRequest;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelConciliacion;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@RequestScoped
@Path("/conciliacion")
public class ConciliacionReportesEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    private ValidaConciliacionRequest validaConciliacionRequest;
    @Inject
    private ConciliacionService periodoService;
    @Inject
    private EstatusPeriodoConciliacionService estatusPeriodoService;
    @Inject
    private ConciliacionBitacoraService existeDocumentoService;
    @Inject
    private ConciliacionBitacoraRetencionPorDelegacionService existeRetencionPorDelegacionService;
    @Inject
    private CartaReciboBitacoraService existeCartaReciboService;
    @Inject
    private ConciliacionBitacoraEFPorCuentaContableService existeDocumentoPorEFService;
    @Inject
    private GeneraReporteDetalleConciliacionService generaDetalleConciliacionService;
    @Inject
    private GeneraReporteCartaReciboInicialService generaCartaReciboInicialService;
    @Inject
    private GeneraReporteEFPorCuentaContableService generaCuentaContableService;
    @Inject
    private GeneraReporteRetencionPorDelegacionService generaRetencionPorDelegacionService;
    @Inject
    private GeneraReporteSolicitudTransferenciaService generaSolicitudTransferenciaService;
    @Inject
    private GeneraReporteResumenConciliacionService generaResumenConciliacionService;
    @Inject
    private ConciliacionBovedaService guardarEnBovedaService;
    @Inject
    private ConciliacionDocumentoService guardarMcltDocumentoService;
    @Inject
    private ConciliacionDocumentoConciliacionService guardarMcltDocumentoConciliacionService;
    @Inject
    private ConciliacionMultipleDocumentoConciliacionService guardarMultipleMcltDocumentoConciliacionService;
    @Inject
    private ConciliacionResumenBitacoraService resumenBitacoraService;
    @Inject
    private GeneraTramiteErogacionesTransferenciaService erogacionesTransferenciaService;
    @Inject
    private GeneraTramiteErogacionesContableService erogacionesContableService;
    @Inject
    private GeneraTramiteErogacionesDelegacionService erogacionesDelegacionService;
    @Inject
    private ActualizarTramiteErogacionesService erogacionesActualizarService;
    @Inject
    private ObtencionIVAService obtencionIVAService;
    @Inject
    private CartaReciboExisteEFTodasService existeEFTodasService;
    @Inject
    private CartaReciboExisteEFService existeEFService;

    @POST
    @Path("/detalleConciliacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaDetalleConciliacion(ConciliacionRequest request) throws BusinessException {
        try {
            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            Message<ConciliacionRequest> respuestaDocumento = existeDocumentoService.execute(new Message<>(request));
            if (respuestaDocumento.getPayload().getDocumento().getId() != null && respuestaDocumento.getPayload().getDocumento().getRefDocBoveda() != null)
                return Response.ok(respuestaDocumento.getPayload().getDocumento()).build();

            ServiceDefinition[] steps = {
                    obtencionIVAService,
                    generaDetalleConciliacionService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    guardarMcltDocumentoConciliacionService
            };
            Message<ConciliacionRequest> respuestaCreaDocumento = obtencionIVAService.executeSteps(steps, new Message<>(request));
            return Response.ok(respuestaCreaDocumento.getPayload().getDocumento()).build();

        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR EntidadFinancieraConciliacionEndPoint.generaDetalleConciliacion()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    @POST
    @Path("/existenCartasReciboPerfilesImss")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response existenCartasReciboPerfilesImss(PageRequestModel<ConciliacionRequest> request) throws BusinessException {
        try {
            PageRequestModelConciliacion pageRequest = new PageRequestModelConciliacion();
            pageRequest.setRequest(request);

            Message<Boolean> respuestaEstatus = estatusPeriodoService.execute(new Message<>(pageRequest));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            Message<PageRequestModelConciliacion> respuesta = null;
            if (request.getModel().getCveEntidadFinanciera().equals(0L)) {
                respuesta = existeEFTodasService.execute(new Message<>(pageRequest));
            }else{
                respuesta = existeEFService.execute(new Message<>(pageRequest));
            }
            return Response.ok(
                    respuesta.getPayload().getResponsePageModelCartaRecibo()
            ).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.existeCartaRecibo()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    @POST
    @Path("/existeCartaRecibo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response existeCartaRecibo(ConciliacionRequest request) throws BusinessException {
        try {
            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            Message<ConciliacionRequest> respuestaDocumento = existeCartaReciboService.execute(new Message<>(request));
            if (respuestaDocumento.getPayload().getDocumento().getId() != null && respuestaDocumento.getPayload().getDocumento().getRefDocBoveda() != null)
                return Response.ok(respuestaDocumento.getPayload().getDocumento()).build();

            return Response.ok(new Documento()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.existeCartaRecibo()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    @POST
    @Path("/existeCartaReciboEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response existeCartaReciboEF(ConciliacionRequest request) throws BusinessException {
        try {
            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            Message<ConciliacionRequest> respuestaDocumento = existeCartaReciboService.executeEF(new Message<>(request));
            if (respuestaDocumento.getPayload().getDocumento().getId() != null && respuestaDocumento.getPayload().getDocumento().getRefDocBoveda() != null)
                return Response.ok(respuestaDocumento.getPayload().getDocumento()).build();

            return Response.ok(new Documento()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.existeCartaRecibo()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }
    
    @POST
    @Path("/cartaReciboInicial")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarCartaReciboInicial(ConciliacionRequest request) throws BusinessException {
        try {
            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);
            
            ServiceDefinition[] steps = {
            		obtencionIVAService,
            		generaCartaReciboInicialService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    guardarMcltDocumentoConciliacionService
            };
            Message<ConciliacionRequest> respuestaCreaDocumento = obtencionIVAService.executeSteps(
                    steps, new Message<>(request)
            );
            return Response.ok(
                    respuestaCreaDocumento.getPayload().getDocumento()
            ).build();

        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generarCartaRecibo()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));

    }
    
    @POST
    @Path("/resumenConciliacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteResumenConciliacion(ConciliacionRequest request) throws BusinessException{
        try{
            if (!validaConciliacionRequest.validaCurpTipoDocumentoPeriodo(request))
                throw new ConciliacionException(ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO);

            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            Message<ConciliacionRequest> respuestaDocumento = resumenBitacoraService.execute(new Message<>(request));
            if (respuestaDocumento.getPayload().getDocumento().getId() != null && respuestaDocumento.getPayload().getDocumento().getRefDocBoveda() != null)
                return Response.ok(respuestaDocumento.getPayload().getDocumento()).build();

            ServiceDefinition[] steps = {
                    generaResumenConciliacionService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    guardarMcltDocumentoConciliacionService
            };

            Message<ConciliacionRequest> reporteResumenConciliacion = generaResumenConciliacionService.executeSteps(
                    steps,
                    new Message<>(request)
            );
            return Response.ok(
                    reporteResumenConciliacion.getPayload().getDocumento()
            ).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generaReporteResumenConciliacion()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));

    }

    @POST
    @Path("/tramiteErogaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteTramiteErogaciones(ConciliacionRequest request) throws BusinessException {
        try {
            if (!validaConciliacionRequest.validaCurpEntidadesPeriodo(request))
                throw new ConciliacionException(
                        ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO
                );

            Message<Boolean> respuestaEstatus = periodoService.execute(new Message<>(request));
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(
                        ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO
                );

            ServiceDefinition[] steps = {
                    erogacionesTransferenciaService,
                    erogacionesContableService,
                    erogacionesDelegacionService,
                    erogacionesActualizarService
            };

            Message<ConciliacionRequest> respuestaTramiteErogaciones = erogacionesTransferenciaService.executeSteps(
                    steps, new Message<>(request)
            );

            return Response.ok(
                    respuestaTramiteErogaciones.getPayload().getListDocumentos()
            ).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generaReporteTramiteErogaciones()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    @POST
    @Path("/solicitudTransferencia")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteSolicitudTransferencia(ConciliacionRequest request) throws BusinessException {
        try {
            Message<ConciliacionRequest> respuestaDocumento = existeDocumentoPorEFService.execute(new Message<>(request));
            if (respuestaDocumento.getPayload().getEntidadesSinDocumento() == 0)
                return Response.ok(respuestaDocumento.getPayload().getListDocumentos()).build();
            else
                respuestaDocumento.getPayload().setEntidadesSinDocumento(0);

            ServiceDefinition[] steps = {
                    generaSolicitudTransferenciaService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    //guardarMcltDocumentoConciliacionService,
                    guardarMultipleMcltDocumentoConciliacionService
            };
            Message<ConciliacionRequest> reporteCuentaContable = generaSolicitudTransferenciaService.executeSteps(
                    steps, new Message<>(request)
            );
            reporteCuentaContable.getPayload().getListSolicitudTransferencia().add(
                    reporteCuentaContable.getPayload().getDocumento()
            );
            return Response.ok(
                    reporteCuentaContable.getPayload().getListSolicitudTransferencia()
            ).build();

        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generaReporteSolicitudTransferencia()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    @POST
    @Path("/reportePorCuentaContable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarReporteEFPorCuentaContable(ConciliacionRequest request) throws BusinessException {
        try {
            Message<ConciliacionRequest> respuestaDocumento = existeDocumentoPorEFService.execute(new Message<>(request));
            if (respuestaDocumento.getPayload().getEntidadesSinDocumento() == 0)
                return Response.ok(respuestaDocumento.getPayload().getListDocumentos()).build();
            else
                respuestaDocumento.getPayload().setEntidadesSinDocumento(0);

            ServiceDefinition[] steps = {
                    generaCuentaContableService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    //guardarMcltDocumentoConciliacionService,
                    guardarMultipleMcltDocumentoConciliacionService
            };

            Message<ConciliacionRequest> reporteCuentaContable = generaCuentaContableService.executeSteps(
                    steps, new Message<>(request)
            );
            reporteCuentaContable.getPayload().getListCuentaContable().add(
                    reporteCuentaContable.getPayload().getDocumento()
            );
            return Response.ok(
                    reporteCuentaContable.getPayload().getListCuentaContable()
            ).build();

        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generarReporteEFPorCuentaContable()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));

    }

    @POST
    @Path("/reportePorDelegacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteRetencionPorDelegacion(ConciliacionRequest request) throws BusinessException {
        try {
            Message<ConciliacionRequest> repuesta = existeRetencionPorDelegacionService.execute(new Message<>(request));
            if (repuesta.getPayload().getEntidadesSinDocumento() == 0)
                return Response.ok(repuesta.getPayload().getListRetencionPorDelegacion()).build();
            else
                repuesta.getPayload().setEntidadesSinDocumento(0);

            Long[] entidades = request.getArregloIdEntidadFinanciera();
            for (Long cveEF : entidades) {
                request.setCveEntidadFinanciera(cveEF);
                Documento documentoGenerado = this.procesoReporteRetencionPorDelegacion(request);
                request.getListRetencionPorDelegacion().add(documentoGenerado);
            }
            return Response.ok(request.getListRetencionPorDelegacion()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.generaReporteRetencionPorDelegacion()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

    public Documento procesoReporteRetencionPorDelegacion(ConciliacionRequest request) throws BusinessException {
        try {
            ServiceDefinition[] steps = {
                    generaRetencionPorDelegacionService,
                    guardarEnBovedaService,
                    guardarMcltDocumentoService,
                    guardarMcltDocumentoConciliacionService
            };

            Message<ConciliacionRequest> reporteRetencionPorDelegacion = generaRetencionPorDelegacionService.executeSteps(
                    steps, new Message<>(request)
            );
            return reporteRetencionPorDelegacion.getPayload().getDocumento();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionReportesEndPoint.procesoReporteRetencionPorDelegacion()", e);
            throw e;
        }
    }







}
