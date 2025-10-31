package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboConFirmaService;
import mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboFechaDescargaService;
import mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionService;
import mx.gob.imss.dpes.entidadfinancierafront.service.EstatusConciliacionPersistenciaService;
import mx.gob.imss.dpes.entidadfinancierafront.service.EstatusConciliacionService;
import mx.gob.imss.dpes.entidadfinancierafront.service.EstatusPeriodoConciliacionService;
import mx.gob.imss.dpes.entidadfinancierafront.service.EstatusPeriodoTramiteErogacionService;
import mx.gob.imss.dpes.entidadfinancierafront.service.TramiteErogacionService;
import mx.gob.imss.dpes.entidadfinancierafront.validation.ValidacionEstatusConciliacion;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelConciliacion;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelTramiteErogacion;
import mx.gob.imss.dpes.interfaces.estatusConciliacion.model.EstatusConciliacionRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@RequestScoped
@Path("/conciliacion")
public class ConciliacionEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    private EstatusConciliacionService obtenerEstatusService;
    @Inject
    private EstatusConciliacionPersistenciaService guardarEstatusService;
    @Inject
    private ValidacionEstatusConciliacion validaEstatus;
    @Inject
    private ConciliacionService periodoService;
    @Inject
    private CartaReciboConFirmaService cartaReciboService;
    @Inject
    private CartaReciboFechaDescargaService fechaDescargaService;
    @Inject
    private EstatusPeriodoConciliacionService estatusConciliacionService;
    @Inject
    private TramiteErogacionService tramiteErogacionService;
    @Inject
    private EstatusPeriodoTramiteErogacionService estatusErogacionService;

    @GET
    @Path("/estatusConciliacion/{periodo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstatusConciliacionPorPeriodo(@PathParam("periodo") String periodo) {

        EstatusConciliacionRequest request = new EstatusConciliacionRequest();
        request.setPeriodo(periodo);
        ServiceDefinition[] steps = {obtenerEstatusService};
        try {
            Message<EstatusConciliacionRequest> resultado = obtenerEstatusService.executeSteps(steps, new Message<>(request));
            return Response.ok(resultado.getPayload()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionEndPoint.obtenerEstatusConciliacionPorPeriodo() ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @POST
    @Path("/estatusConciliacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarEstatusConciliacion(EstatusConciliacionRequest request) throws BusinessException {
        if (!validaEstatus.isEstatusValido(request))
            return toResponse(
                    new Message(
                            null,
                            ServiceStatusEnum.EXCEPCION,
                            new ConciliacionException(ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                            null
                    ));
        try {
            ServiceDefinition[] steps = {guardarEstatusService};
            Message<EstatusConciliacionRequest> resultado = guardarEstatusService.executeSteps(steps, new Message<>(request));
            return Response.ok(resultado.getPayload()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionEndPoint.guardarEstatusConciliacion() ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }

    @POST
    @Path("/obtenerListCartaReciboFirmada")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListCartaReciboConFirma(PageRequestModel<ConciliacionRequest> request) throws BusinessException {
        try {
            if (!(
                request != null && request.getModel() != null &&
                request.getModel().getPeriodo() != null && !request.getModel().getPeriodo().trim().isEmpty() &&
                request.getModel().getCveEntidadFinanciera() != null && request.getModel().getCveEntidadFinanciera() >= 0
            ))
                throw new ConciliacionException(ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO);

            PageRequestModelConciliacion pageRequest = new PageRequestModelConciliacion();
            pageRequest.setRequest(request);

            Message<Boolean> respuestaEstatus = estatusConciliacionService.execute(
                    new Message<>(pageRequest)
            );
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            ServiceDefinition[] steps = {cartaReciboService};
            Message<PageRequestModelConciliacion> respuesta = cartaReciboService.executeSteps(
                    steps, new Message<>(pageRequest)
            );

            return Response.ok(
                    respuesta.getPayload().getResponse()
            ).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionEndPoint.obtenerListCartaReciboConFirma()", e);
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
    @Path("/cartaRecibo/fechaDescarga")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarFechaDescarga(DocumentoConciliacionRequest request) throws BusinessException {

        if (request.getId() == null)
            throw new ConciliacionException(ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO);

        try {
            ServiceDefinition[] steps = {fechaDescargaService};
            Message<DocumentoConciliacionRequest> respuesta = fechaDescargaService.executeSteps(steps, new Message<>(request));
            if (respuesta == null)
                return Response.noContent().build();

            return Response.ok(respuesta.getPayload()).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionEndPoint.guardarFechaDescarga()", e);
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
    @Path("/obtenerListTramiteErogaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListTramiteErogaciones(PageRequestModel<ConciliacionRequest> request) throws BusinessException {
        try {

            if (!(
                    request != null && request.getModel() != null &&
                            request.getModel().getPeriodo() != null && !request.getModel().getPeriodo().trim().isEmpty() &&
                            request.getModel().getCveEntidadFinanciera() != null && request.getModel().getCveEntidadFinanciera() >= 0
            ))
                throw new ConciliacionException(ConciliacionException.MENSAJE_DE_SOLICITUD_INCORRECTO);

            PageRequestModelTramiteErogacion pageRequest = new PageRequestModelTramiteErogacion();
            pageRequest.setRequest(request);

            Message<Boolean> respuestaEstatus = estatusErogacionService.execute(
                    new Message<>(pageRequest)
            );
            if (!respuestaEstatus.getPayload())
                throw new ConciliacionException(ConciliacionException.NO_SE_ENCUENTRA_HABILITADO_EL_PERIODO);

            ServiceDefinition[] steps = {tramiteErogacionService};
            Message<PageRequestModelTramiteErogacion> respuesta = tramiteErogacionService.executeSteps(
                    steps, new Message<>(pageRequest)
            );

            return Response.ok(
                    respuesta.getPayload().getResponse()
            ).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionEndPoint.obtenerListTramiteErogaciones()", e);
        }
        return toResponse(
                new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new ConciliacionException(ConciliacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                        null
                ));
    }

}
