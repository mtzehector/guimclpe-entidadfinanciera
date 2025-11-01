package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

import java.util.Objects;

public class ConciliacionBean extends BaseModel {

    @Getter
    @Setter
    private String nss;

    @Getter
    @Setter
    private String grupoFamiliar;

    @Getter
    @Setter
    private String cveDelegacion;

    @Getter
    @Setter
    private String numeroFolio;

    @Getter
    @Setter
    private Double totalPagar;

    @Getter
    @Setter
    private Double saldoPendiente;

    @Getter
    @Setter
    private Double descuentoNomina;

    @Getter
    @Setter
    private Integer plazo;

    @Getter
    @Setter
    private Integer numeroMensualidad;

    @Getter
    @Setter
    private Double concepto;

    @Getter
    @Setter
    private Double importeRecuperado;

    @Getter
    @Setter
    private String cveRechazoSpes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConciliacionBean that = (ConciliacionBean) o;
        return Objects.equals(nss, that.nss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nss);
    }
}
